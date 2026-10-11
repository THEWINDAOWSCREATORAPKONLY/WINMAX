package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppType
import com.example.model.OsFamily
import com.example.model.OsVersionId
import com.example.ui.apps.FileExplorerApp
import com.example.ui.apps.MinesweeperApp
import com.example.ui.apps.NotepadApp
import com.example.ui.apps.PackageManagerApp
import com.example.ui.apps.PaintApp
import com.example.ui.apps.SysInfoApp
import com.example.ui.apps.TerminalApp
import com.example.ui.components.BootScreen
import com.example.ui.components.BsodCrashScreen
import com.example.ui.components.DesktopIconGrid
import com.example.ui.components.OsSwitcherDialog
import com.example.ui.components.StartMenuPopup
import com.example.ui.components.Taskbar
import com.example.ui.components.WindowFrame
import com.example.viewmodel.OsSimulatorViewModel

@Composable
fun MainScreen(
    viewModel: OsSimulatorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Notification toast auto-clear
    LaunchedEffect(uiState.notificationToast) {
        if (uiState.notificationToast != null) {
            kotlinx.coroutines.delay(2600)
            viewModel.clearToast()
        }
    }

    // BSOD / Crash Screen handler
    if (uiState.isBsod) {
        BsodCrashScreen(
            currentOs = uiState.currentOs,
            message = uiState.bsodMessage,
            onReboot = { viewModel.recoverFromBsod() }
        )
        return
    }

    // Boot Screen handler
    if (uiState.isBooting) {
        BootScreen(
            targetOs = uiState.currentOs,
            progress = uiState.bootProgress,
            logs = uiState.bootLogs
        )
        return
    }

    val currentOs = uiState.currentOs

    // Era-tailored Wallpaper Background
    val wallpaperModifier = Modifier
        .fillMaxSize()
        .background(
            when (currentOs) {
                OsVersionId.WIN_XP -> Brush.verticalGradient(
                    listOf(Color(0xFF245EDC), Color(0xFF68B25B), Color(0xFF4C933F))
                )
                OsVersionId.WIN_7 -> Brush.radialGradient(
                    listOf(Color(0xFF38BDF8), Color(0xFF0F172A))
                )
                OsVersionId.WIN_11 -> Brush.radialGradient(
                    listOf(Color(0xFF60A5FA), Color(0xFF1E3A8A))
                )
                OsVersionId.MAC_OS_X_CHEETAH -> Brush.verticalGradient(
                    listOf(Color(0xFF64B5F6), Color(0xFF1976D2), Color(0xFF0D47A1))
                )
                OsVersionId.MAC_OS_BIG_SUR -> Brush.linearGradient(
                    listOf(Color(0xFFE65100), Color(0xFFD81B60), Color(0xFF3949AB))
                )
                OsVersionId.MAC_OS_SONOMA -> Brush.verticalGradient(
                    listOf(Color(0xFF0284C7), Color(0xFF065F46))
                )
                OsVersionId.MAC_OS_SEQUOIA -> Brush.verticalGradient(
                    listOf(Color(0xFF166534), Color(0xFF052E16))
                )
                OsVersionId.LINUX_UBUNTU -> Brush.linearGradient(
                    listOf(Color(0xFF7C2D12), Color(0xFF581C87))
                )
                OsVersionId.LINUX_KALI -> Brush.verticalGradient(
                    listOf(Color(0xFF0F172A), Color(0xFF020617))
                )
                else -> Brush.verticalGradient(
                    listOf(Color(currentOs.defaultWallHex), Color(currentOs.defaultWallHex))
                )
            }
        )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Taskbar(
                currentOs = currentOs,
                openWindows = uiState.openWindows,
                activeWindowId = uiState.activeWindowId,
                systemTime = uiState.systemTime,
                isStartMenuOpen = uiState.isStartMenuOpen,
                onStartClick = { viewModel.toggleStartMenu() },
                onOsSwitchClick = { viewModel.toggleOsSwitcher() },
                onWindowClick = { winId -> viewModel.bringToFront(winId) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = wallpaperModifier
                .padding(innerPadding)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    viewModel.dismissOverlays()
                }
        ) {
            // Desktop Icons Grid
            DesktopIconGrid(
                currentOs = currentOs,
                onOpenApp = { viewModel.openApp(it) },
                onOpenOsSwitcher = { viewModel.toggleOsSwitcher() },
                modifier = Modifier.fillMaxSize()
            )

            // Render all Open Windows
            uiState.openWindows.forEach { win ->
                WindowFrame(
                    windowState = win,
                    currentOs = currentOs,
                    isActive = win.id == uiState.activeWindowId,
                    onClose = { viewModel.closeWindow(win.id) },
                    onMinimize = { viewModel.minimizeWindow(win.id) },
                    onToggleMaximize = { viewModel.toggleMaximizeWindow(win.id) },
                    onFocus = { viewModel.bringToFront(win.id) },
                    onDrag = { newX, newY -> viewModel.updateWindowPosition(win.id, newX, newY) }
                ) {
                    when (win.type) {
                        AppType.MINESWEEPER -> {
                            MinesweeperApp()
                        }
                        AppType.TERMINAL -> {
                            TerminalApp(
                                currentOs = currentOs,
                                history = uiState.terminalHistory,
                                onRunCommand = { viewModel.handleTerminalCommand(it) }
                            )
                        }
                        AppType.FILE_EXPLORER -> {
                            FileExplorerApp(
                                currentOs = currentOs,
                                files = uiState.virtualFiles,
                                onOpenFile = { file ->
                                    viewModel.openApp(AppType.NOTEPAD)
                                    viewModel.updateNotepadContent(file.content)
                                }
                            )
                        }
                        AppType.PAINT -> {
                            PaintApp(
                                onDrawn = {}
                            )
                        }
                        AppType.NOTEPAD -> {
                            NotepadApp(
                                initialText = uiState.activeNotepadFile,
                                onContentChange = { viewModel.updateNotepadContent(it) }
                            )
                        }
                        AppType.SYS_INFO -> {
                            SysInfoApp(
                                currentOs = currentOs
                            )
                        }
                        AppType.PACKAGE_MGR -> {
                            PackageManagerApp(
                                currentOs = currentOs,
                                onPackageInstalled = {
                                    viewModel.showToast("Package installed successfully!")
                                }
                            )
                        }
                    }
                }
            }

            // Start / Apple Menu Popup (Bottom-Left)
            StartMenuPopup(
                currentOs = currentOs,
                isOpen = uiState.isStartMenuOpen,
                onAppClick = { appType -> viewModel.openApp(appType) },
                onOsSwitchClick = { viewModel.toggleOsSwitcher() },
                onTriggerCrash = { viewModel.triggerBsod() },
                onDismiss = { viewModel.dismissOverlays() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 8.dp, bottom = 4.dp)
                    .zIndex(100f)
            )

            // OS Switcher Dialog Modal (All 100% Free)
            if (uiState.isOsSwitcherOpen) {
                OsSwitcherDialog(
                    currentOs = currentOs,
                    onSelectOs = { selectedOs -> viewModel.switchOs(selectedOs) },
                    onDismiss = { viewModel.toggleOsSwitcher() }
                )
            }

            // Notification In-App Toast
            AnimatedVisibility(
                visible = uiState.notificationToast != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .zIndex(200f)
            ) {
                uiState.notificationToast?.let { msg ->
                    Surface(
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(20.dp))
                            .testTag("notification_toast"),
                        color = Color(0xFF0F172A),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = msg,
                            color = Color(0xFFF8FAFC),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
