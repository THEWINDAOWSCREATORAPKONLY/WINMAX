package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.model.OsFamily
import com.example.model.OsVersionId
import com.example.model.WindowState
import kotlin.math.roundToInt

@Composable
fun WindowFrame(
    windowState: WindowState,
    currentOs: OsVersionId,
    isActive: Boolean,
    onClose: () -> Unit,
    onMinimize: () -> Unit,
    onToggleMaximize: () -> Unit,
    onFocus: () -> Unit,
    onDrag: (Float, Float) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (windowState.isMinimized) return

    val isMac = currentOs.family == OsFamily.MACOS
    val isRetro = currentOs.releaseYear < 2002
    val isModernRound = currentOs == OsVersionId.WIN_11 || isMac || currentOs == OsVersionId.LINUX_UBUNTU

    val cornerRadius = when {
        isModernRound -> 12.dp
        isRetro -> 0.dp
        else -> 6.dp
    }

    val headerColor = if (isActive) {
        Color(currentOs.windowHeaderHex)
    } else {
        Color(0xFF71717A)
    }

    val headerTextColor = Color(currentOs.windowHeaderTextHex)
    val borderColor = if (isRetro) Color(0xFFC0C0C0) else Color(0x33000000)

    val frameModifier = if (windowState.isMaximized) {
        modifier
            .fillMaxSize()
            .zIndex(windowState.zIndex)
    } else {
        modifier
            .offset { IntOffset(windowState.offsetX.roundToInt(), windowState.offsetY.roundToInt()) }
            .width(windowState.width.dp)
            .height(windowState.height.dp)
            .zIndex(windowState.zIndex)
    }

    Surface(
        modifier = frameModifier
            .testTag("window_${windowState.id}")
            .shadow(if (isActive) 14.dp else 4.dp, shape = RoundedCornerShape(cornerRadius))
            .border(
                width = if (isRetro) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .clickable { onFocus() },
        color = Color(0xFFF4F4F5)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Title Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .background(headerColor)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            if (!windowState.isMaximized) {
                                onDrag(
                                    windowState.offsetX + dragAmount.x,
                                    windowState.offsetY + dragAmount.y
                                )
                            }
                        }
                    }
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isMac) {
                    // Mac Traffic Lights on the Left (Red Close, Yellow Minimize, Green Zoom)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Close (Red)
                        Box(
                            modifier = Modifier
                                .size(13.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFF5F56))
                                .border(0.5.dp, Color(0xFFE0443E), CircleShape)
                                .clickable { onClose() }
                                .testTag("btn_close_${windowState.id}")
                        )
                        // Minimize (Yellow)
                        Box(
                            modifier = Modifier
                                .size(13.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFBD2E))
                                .border(0.5.dp, Color(0xFFDEA123), CircleShape)
                                .clickable { onMinimize() }
                                .testTag("btn_minimize_${windowState.id}")
                        )
                        // Maximize / Zoom (Green)
                        Box(
                            modifier = Modifier
                                .size(13.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF27C93F))
                                .border(0.5.dp, Color(0xFF1AAB29), CircleShape)
                                .clickable { onToggleMaximize() }
                                .testTag("btn_maximize_${windowState.id}")
                        )
                    }

                    // Centered Window Title for macOS
                    Text(
                        text = windowState.title,
                        color = headerTextColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(36.dp))
                } else {
                    // Windows / Linux Layout: Title on Left, Buttons on Right
                    Text(
                        text = windowState.title,
                        color = headerTextColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = if (isRetro) FontFamily.Monospace else FontFamily.SansSerif,
                        modifier = Modifier.weight(1f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Minimize
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .testTag("btn_minimize_${windowState.id}")
                                .clickable { onMinimize() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Minimize,
                                contentDescription = "Minimize",
                                tint = headerTextColor,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(2.dp))

                        // Maximize
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .testTag("btn_maximize_${windowState.id}")
                                .clickable { onToggleMaximize() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CropSquare,
                                contentDescription = "Maximize",
                                tint = headerTextColor,
                                modifier = Modifier.size(13.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(2.dp))

                        // Close
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (isActive && !isRetro) Color(0xFFE11D48) else Color.Transparent)
                                .testTag("btn_close_${windowState.id}")
                            .clickable { onClose() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = if (isActive && !isRetro) Color.White else headerTextColor,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }

            // Window Client Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                content()
            }
        }
    }
}
