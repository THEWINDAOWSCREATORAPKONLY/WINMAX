package com.example.ui.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OsFamily
import com.example.model.OsVersionId
import com.example.model.SimFile

@Composable
fun FileExplorerApp(
    currentOs: OsVersionId,
    files: List<SimFile>,
    onOpenFile: (SimFile) -> Unit,
    modifier: Modifier = Modifier
) {
    val isWin = currentOs.family == OsFamily.WINDOWS
    val pathHeader = if (isWin) "C:\\WINDOWS\\System32" else "/home/guest/documents"

    var selectedFile by remember { mutableStateOf<SimFile?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // Explorer Address Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE4E4E7))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Storage,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = pathHeader,
                color = Color.Black,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium
            )
        }

        Divider(color = Color(0xFFD4D4D8))

        // File List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            items(files) { file ->
                val isSelected = selectedFile?.name == file.name
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSelected) Color(0xFFBFDBFE) else Color.Transparent)
                        .clickable {
                            selectedFile = file
                            onOpenFile(file)
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("file_item_${file.name}"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (file.isDir) Icons.Default.Folder else Icons.Default.Description,
                            contentDescription = null,
                            tint = if (file.isDir) Color(0xFFF59E0B) else Color(0xFF3B82F6),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = file.name,
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Text(
                        text = file.sizeStr,
                        color = Color.Gray,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Status bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF4F4F5))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${files.size} objects", color = Color.Gray, fontSize = 11.sp)
            Text("Free space: 14.8 GB", color = Color.Gray, fontSize = 11.sp)
        }
    }
}
