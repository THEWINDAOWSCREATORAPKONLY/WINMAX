package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppType
import com.example.model.OsFamily
import com.example.model.OsVersionId
import com.example.model.WindowState

@Composable
fun Taskbar(
    currentOs: OsVersionId,
    openWindows: List<WindowState>,
    activeWindowId: String?,
    systemTime: String,
    isStartMenuOpen: Boolean,
    onStartClick: () -> Unit,
    onOsSwitchClick: () -> Unit,
    onWindowClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isWin = currentOs.family == OsFamily.WINDOWS
    val isMac = currentOs.family == OsFamily.MACOS
    val isRetro = currentOs.releaseYear < 2002
    val isWin11 = currentOs == OsVersionId.WIN_11

    val taskbarBg = Color(currentOs.taskbarHex)
    val taskbarTextColor = Color(currentOs.taskbarTextHex)

    val startLabel = when {
        isMac -> " Apple"
        isWin11 -> "⊞ Start"
        isWin -> "Start"
        currentOs == OsVersionId.LINUX_UBUNTU -> "Activities"
        currentOs == OsVersionId.LINUX_KALI -> "Kali"
        currentOs == OsVersionId.LINUX_ARCH -> "Arch"
        else -> "Applications"
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(6.dp),
        color = taskbarBg
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Start / Apple Menu & OS Time Machine button
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Primary Start / Apple Button
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .clip(if (isRetro) RoundedCornerShape(0.dp) else RoundedCornerShape(8.dp))
                        .background(
                            if (isStartMenuOpen) Color(currentOs.accentHex).copy(alpha = 0.8f)
                            else if (currentOs == OsVersionId.WIN_XP) Color(0xFF22C55E)
                            else if (isMac) Color.White.copy(alpha = 0.2f)
                            else Color(currentOs.accentHex)
                        )
                        .clickable { onStartClick() }
                        .padding(horizontal = 12.dp)
                        .testTag("taskbar_start_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = startLabel,
                            color = if (isMac) taskbarTextColor else Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = if (isRetro) FontFamily.Monospace else FontFamily.SansSerif
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // OS Switcher button
                Box(
                    modifier = Modifier
                        .height(34.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.2f))
                        .clickable { onOsSwitchClick() }
                        .padding(horizontal = 8.dp)
                        .testTag("taskbar_os_switcher_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Switch OS",
                            tint = taskbarTextColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "OS Hub (All Free)",
                            color = taskbarTextColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Middle: Active Window Tabs (Scrollable)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                openWindows.forEach { win ->
                    val isActive = win.id == activeWindowId && !win.isMinimized
                    Box(
                        modifier = Modifier
                            .height(34.dp)
                            .width(115.dp)
                            .padding(horizontal = 2.dp)
                            .clip(if (isRetro) RoundedCornerShape(0.dp) else RoundedCornerShape(6.dp))
                            .background(
                                if (isActive) Color.White.copy(alpha = 0.35f)
                                else Color.Black.copy(alpha = 0.15f)
                            )
                            .clickable { onWindowClick(win.id) }
                            .padding(horizontal = 6.dp)
                            .testTag("taskbar_window_${win.id}"),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = win.title,
                            color = taskbarTextColor,
                            fontSize = 11.sp,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Right: Clock & Battery/Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = systemTime,
                    color = taskbarTextColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun StartMenuPopup(
    currentOs: OsVersionId,
    isOpen: Boolean,
    onAppClick: (AppType) -> Unit,
    onOsSwitchClick: () -> Unit,
    onTriggerCrash: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRetro = currentOs.releaseYear < 2002
    val isWin = currentOs.family == OsFamily.WINDOWS
    val isMac = currentOs.family == OsFamily.MACOS

    AnimatedVisibility(
        visible = isOpen,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .width(280.dp)
                .shadow(16.dp, RoundedCornerShape(if (isRetro) 0.dp else 12.dp))
                .border(
                    width = 1.dp,
                    color = Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(if (isRetro) 0.dp else 12.dp)
                )
                .testTag("start_menu_popup"),
            color = Color(0xFF1E293B),
            shape = RoundedCornerShape(if (isRetro) 0.dp else 12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Header Banner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(currentOs.accentHex), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isMac) "" else if (isWin) "⊞" else "🐧",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = currentOs.displayName,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Release: ${currentOs.releaseYear} • All Versions 100% Free",
                            color = Color(0xFF38BDF8),
                            fontSize = 11.sp
                        )
                    }
                }

                Divider(
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = Color.DarkGray
                )

                // Menu items
                StartMenuItem(
                    icon = Icons.Default.Terminal,
                    title = if (isWin) "Command Prompt (CMD)" else if (isMac) "Terminal (zsh)" else "Terminal (Bash)",
                    subtitle = "Run commands, neofetch, matrix",
                    onClick = { onAppClick(AppType.TERMINAL) }
                )

                StartMenuItem(
                    icon = Icons.Default.Apps,
                    title = "Minesweeper Classic",
                    subtitle = "Retro logic puzzle game",
                    onClick = { onAppClick(AppType.MINESWEEPER) }
                )

                StartMenuItem(
                    icon = Icons.Default.Folder,
                    title = if (isMac) "Finder" else "File Explorer",
                    subtitle = "Browse virtual drive and files",
                    onClick = { onAppClick(AppType.FILE_EXPLORER) }
                )

                StartMenuItem(
                    icon = Icons.Default.Code,
                    title = if (isMac) "TextEdit" else "Notepad / Editor",
                    subtitle = "Document writing and notes",
                    onClick = { onAppClick(AppType.NOTEPAD) }
                )

                StartMenuItem(
                    icon = Icons.Default.Palette,
                    title = "Paint & Canvas",
                    subtitle = "Pixel canvas doodling",
                    onClick = { onAppClick(AppType.PAINT) }
                )

                StartMenuItem(
                    icon = Icons.Default.ShoppingBag,
                    title = if (isMac) "Mac App Store & Brew" else "Package / App Manager",
                    subtitle = "Install software packages",
                    onClick = { onAppClick(AppType.PACKAGE_MGR) }
                )

                StartMenuItem(
                    icon = Icons.Default.Info,
                    title = if (isMac) "About This Mac" else "System Specs",
                    subtitle = "Specs, kernel & architecture",
                    onClick = { onAppClick(AppType.SYS_INFO) }
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.DarkGray
                )

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Switch OS
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(currentOs.accentHex))
                            .clickable { onOsSwitchClick() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Switch OS",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Switch OS",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Crash / Panic trigger
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFDC2626))
                            .clickable { onTriggerCrash() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("btn_trigger_crash"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Crash",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isWin) "BSOD" else "Panic",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StartMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF60A5FA),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
    }
}
