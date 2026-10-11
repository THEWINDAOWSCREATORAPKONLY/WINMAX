package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppType
import com.example.model.OsFamily
import com.example.model.OsVersionId

@Composable
fun DesktopIconGrid(
    currentOs: OsVersionId,
    onOpenApp: (AppType) -> Unit,
    onOpenOsSwitcher: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRetro = currentOs.releaseYear < 2002
    val isMac = currentOs.family == OsFamily.MACOS

    val desktopItems = listOf(
        DesktopItemData("OS Hub (Free)", Icons.Default.SwapHoriz, Color(0xFF38BDF8)) { onOpenOsSwitcher() },
        DesktopItemData(if (isMac) "Macintosh HD" else "Computer", Icons.Default.Folder, Color(0xFFFBBF24)) { onOpenApp(AppType.FILE_EXPLORER) },
        DesktopItemData(if (isMac) "Terminal" else if (currentOs.family == OsFamily.WINDOWS) "CMD" else "Terminal", Icons.Default.Terminal, Color(0xFF4ADE80)) { onOpenApp(AppType.TERMINAL) },
        DesktopItemData("Minesweeper", Icons.Default.Apps, Color(0xFFF87171)) { onOpenApp(AppType.MINESWEEPER) },
        DesktopItemData("Paint", Icons.Default.Brush, Color(0xFFA78BFA)) { onOpenApp(AppType.PAINT) },
        DesktopItemData(if (isMac) "TextEdit" else "Notepad", Icons.Default.Description, Color(0xFF93C5FD)) { onOpenApp(AppType.NOTEPAD) },
        DesktopItemData(if (isMac) "About Mac" else "System Specs", Icons.Default.Info, Color(0xFF34D399)) { onOpenApp(AppType.SYS_INFO) },
        DesktopItemData(if (isMac) "App Store" else "Packages", Icons.Default.ShoppingBag, Color(0xFFFB923C)) { onOpenApp(AppType.PACKAGE_MGR) }
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 75.dp),
        modifier = modifier.padding(8.dp),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(desktopItems) { item ->
            Column(
                modifier = Modifier
                    .width(72.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { item.onClick() }
                    .padding(4.dp)
                    .testTag("desktop_icon_${item.title.lowercase().replace(" ", "_").replace("(", "").replace(")", "")}"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(if (isRetro) 2.dp else 10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = item.tint,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = if (isRetro) FontFamily.Monospace else FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    modifier = Modifier.shadow(2.dp)
                )
            }
        }
    }
}

data class DesktopItemData(
    val title: String,
    val icon: ImageVector,
    val tint: Color,
    val onClick: () -> Unit
)
