package com.example.ui.apps

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.AudioSynth

data class PaintStroke(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun PaintApp(
    onDrawn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strokes = remember { mutableStateListOf<PaintStroke>() }
    var currentColor by remember { mutableStateOf(Color.Black) }
    var currentStrokeWidth by remember { mutableStateOf(8f) }
    var currentPoints = remember { mutableStateListOf<Offset>() }

    val palette = listOf(
        Color.Black,
        Color(0xFFEF4444), // Red
        Color(0xFF3B82F6), // Blue
        Color(0xFF10B981), // Green
        Color(0xFFF59E0B), // Yellow
        Color(0xFF8B5CF6), // Purple
        Color.White        // Eraser
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE4E4E7))
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD4D4D8))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Colors
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                palette.forEach { color ->
                    val isSelected = currentColor == color
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Color.Blue else Color.Gray,
                                shape = CircleShape
                            )
                            .clickable {
                                AudioSynth.playClickSound()
                                currentColor = color
                            }
                    )
                }
            }

            // Clear Canvas
            IconButton(
                onClick = {
                    strokes.clear()
                    currentPoints.clear()
                    AudioSynth.playClickSound()
                },
                modifier = Modifier.size(28.dp).testTag("paint_clear_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Drawing Canvas
        Canvas(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.White)
                .testTag("paint_canvas")
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPoints.clear()
                            currentPoints.add(offset)
                            onDrawn()
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            currentPoints.add(change.position)
                        },
                        onDragEnd = {
                            if (currentPoints.isNotEmpty()) {
                                strokes.add(PaintStroke(currentPoints.toList(), currentColor, currentStrokeWidth))
                                currentPoints.clear()
                            }
                        }
                    )
                }
        ) {
            strokes.forEach { stroke ->
                for (i in 0 until stroke.points.size - 1) {
                    drawLine(
                        color = stroke.color,
                        start = stroke.points[i],
                        end = stroke.points[i + 1],
                        strokeWidth = stroke.strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }

            for (i in 0 until currentPoints.size - 1) {
                drawLine(
                    color = currentColor,
                    start = currentPoints[i],
                    end = currentPoints[i + 1],
                    strokeWidth = currentStrokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}
