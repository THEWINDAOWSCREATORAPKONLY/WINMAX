package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AppType
import com.example.model.OsFamily
import com.example.model.OsVersionId
import com.example.model.SimFile
import com.example.model.WindowState
import com.example.util.AudioSynth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class OsSimulatorUiState(
    val currentOs: OsVersionId = OsVersionId.WIN_95,
    val openWindows: List<WindowState> = emptyList(),
    val activeWindowId: String? = null,
    val isStartMenuOpen: Boolean = false,
    val isOsSwitcherOpen: Boolean = false,
    val isBsod: Boolean = false,
    val bsodMessage: String = "FATAL EXCEPTION 0E: SYSTEM_THREAD_EXCEPTION_NOT_HANDLED",
    val isBooting: Boolean = false,
    val bootProgress: Float = 0f,
    val bootLogs: List<String> = emptyList(),
    val terminalHistory: List<String> = listOf(
        "Microsoft(R) Windows 95",
        "(C)Copyright Microsoft Corp 1981-1995.",
        "Type 'help' to view available commands.",
        "C:\\>"
    ),
    val terminalCurrentInput: String = "",
    val virtualFiles: List<SimFile> = listOf(
        SimFile("autoexec.bat", false, "@ECHO OFF\nPROMPT \$P\$G\nPATH C:\\WINDOWS;C:\\DOS\n", "120 B"),
        SimFile("config.sys", false, "DEVICE=C:\\WINDOWS\\HIMEM.SYS\nDOS=HIGH,UMB\n", "85 B"),
        SimFile("secret_passwords.txt", false, "ROOT_ADMIN_KEY: TuxAndBillAndSteve_1995\nPORT_SCAN: 8080 open\n", "64 B"),
        SimFile("doom.wad", false, "BINARY_DATA_PK3_DOOM_LEVEL_SHAREWARE_OK", "4.2 MB"),
        SimFile("kernel.log", false, "[0.000000] Booting Winux Hybrid Kernel 6.1...\n[0.104] CPU0 OK\n", "512 B"),
        SimFile("welcome_mac.txt", false, "Welcome to Macintosh!\nFinder 7.0 / macOS Darwin Kernel Mach-O\nThink Different.\n", "95 B")
    ),
    val activeNotepadFile: String = "Welcome to the Free All-in-One OS Simulator!\n\nAll Windows, Linux, and macOS versions are 100% unlocked and free to boot instantly.\n\nClick 'OS Hub' in the bar to jump into any OS era!",
    val systemTime: String = "12:00 PM",
    val notificationToast: String? = null
)

class OsSimulatorViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(OsSimulatorUiState())
    val uiState: StateFlow<OsSimulatorUiState> = _uiState.asStateFlow()

    private var zIndexCounter = 1f

    init {
        updateClock()
        // Open welcome notepad on first launch
        openApp(AppType.NOTEPAD)
    }

    private fun updateClock() {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        _uiState.update { it.copy(systemTime = sdf.format(Date())) }
    }

    fun switchOs(targetOs: OsVersionId) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isBooting = true,
                    bootProgress = 0f,
                    bootLogs = emptyList(),
                    isStartMenuOpen = false,
                    isOsSwitcherOpen = false,
                    isBsod = false
                )
            }

            // Simulate authentic boot screen based on OS family
            val logs = when (targetOs.family) {
                OsFamily.WINDOWS -> listOf(
                    "Starting ${targetOs.displayName}...",
                    "Loading SYSTEM.DAT registry...",
                    "Mounting FAT32 / NTFS Virtual Drive...",
                    "Initializing Windows GUI Subsystem...",
                    "Preparing Desktop..."
                )
                OsFamily.MACOS -> listOf(
                    "Welcome to Macintosh.",
                    "Loading Apple Mach kernel (Darwin 23.4.0)...",
                    "Mounting APFS / HFS+ Container...",
                    "Starting WindowServer & Quartz Compositor...",
                    "Launching Finder & Dock..."
                )
                OsFamily.LINUX -> listOf(
                    "[  0.000000] Linux version 6.8.0-winux (gcc-13)",
                    "[  0.154210] Command line: BOOT_IMAGE=/vmlinuz initrd=/initrd.img",
                    "[  0.421500] systemd[1]: Reached target Graphical Interface.",
                    "[  0.781200] Starting LightDM / GDM Display Manager...",
                    "[  0.994200] Starting ${targetOs.displayName} Desktop Environment..."
                )
            }

            for ((index, log) in logs.withIndex()) {
                delay(180)
                _uiState.update {
                    it.copy(
                        bootProgress = (index + 1) / logs.size.toFloat(),
                        bootLogs = it.bootLogs + log
                    )
                }
            }

            AudioSynth.playBootChime(targetOs.family)
            delay(120)

            _uiState.update {
                it.copy(
                    currentOs = targetOs,
                    isBooting = false,
                    terminalHistory = when (targetOs.family) {
                        OsFamily.WINDOWS -> listOf(
                            "${targetOs.displayName} [Version ${targetOs.releaseYear}.0]",
                            "(c) Microsoft Corporation. All rights reserved.",
                            "C:\\Users\\Guest>"
                        )
                        OsFamily.MACOS -> listOf(
                            "Last login: Sun Oct 11 12:00:00 on ttys000",
                            "macOS ${targetOs.displayName} (Darwin Kernel)",
                            "guest@macbook ~ % "
                        )
                        OsFamily.LINUX -> listOf(
                            "Welcome to ${targetOs.displayName} (${targetOs.codename})!",
                            "Documentation: https://docs.winux.org",
                            "guest@winux-pc:~$ "
                        )
                    },
                    notificationToast = "Booted ${targetOs.displayName} successfully!"
                )
            }
        }
    }

    fun openApp(type: AppType) {
        AudioSynth.playClickSound()
        val currentWindows = _uiState.value.openWindows
        val existing = currentWindows.find { it.type == type }

        if (existing != null) {
            bringToFront(existing.id)
            _uiState.update { state ->
                state.copy(
                    openWindows = state.openWindows.map {
                        if (it.id == existing.id) it.copy(isMinimized = false) else it
                    },
                    isStartMenuOpen = false
                )
            }
            return
        }

        zIndexCounter += 1f
        val offset = (currentWindows.size * 25f) % 150f
        val os = _uiState.value.currentOs
        val newWindow = WindowState(
            id = "win_${System.currentTimeMillis()}_${type.name}",
            type = type,
            title = when (type) {
                AppType.TERMINAL -> when (os.family) {
                    OsFamily.WINDOWS -> "Command Prompt"
                    OsFamily.MACOS -> "Terminal — zsh — 80x24"
                    OsFamily.LINUX -> "bash terminal"
                }
                AppType.SYS_INFO -> when (os.family) {
                    OsFamily.MACOS -> "About This Mac — ${os.displayName}"
                    OsFamily.WINDOWS -> "System Properties — ${os.displayName}"
                    OsFamily.LINUX -> "System Info — ${os.displayName}"
                }
                AppType.NOTEPAD -> when (os.family) {
                    OsFamily.MACOS -> "TextEdit — Untitled"
                    OsFamily.WINDOWS -> "Notepad — Untitled"
                    OsFamily.LINUX -> "gedit — Text Editor"
                }
                AppType.FILE_EXPLORER -> when (os.family) {
                    OsFamily.MACOS -> "Finder — Macintosh HD"
                    OsFamily.WINDOWS -> "File Explorer"
                    OsFamily.LINUX -> "Files (Nautilus)"
                }
                AppType.PACKAGE_MGR -> when (os.family) {
                    OsFamily.MACOS -> "App Store & Homebrew"
                    OsFamily.WINDOWS -> "Windows Store / Winget"
                    OsFamily.LINUX -> "Synaptic Package Manager"
                }
                else -> type.title
            },
            offsetX = 20f + offset,
            offsetY = 30f + offset,
            zIndex = zIndexCounter
        )

        _uiState.update {
            it.copy(
                openWindows = it.openWindows + newWindow,
                activeWindowId = newWindow.id,
                isStartMenuOpen = false
            )
        }
    }

    fun closeWindow(windowId: String) {
        AudioSynth.playClickSound()
        _uiState.update { state ->
            val remaining = state.openWindows.filterNot { it.id == windowId }
            state.copy(
                openWindows = remaining,
                activeWindowId = remaining.maxByOrNull { it.zIndex }?.id
            )
        }
    }

    fun minimizeWindow(windowId: String) {
        AudioSynth.playClickSound()
        _uiState.update { state ->
            state.copy(
                openWindows = state.openWindows.map {
                    if (it.id == windowId) it.copy(isMinimized = true) else it
                },
                activeWindowId = state.openWindows.filter { it.id != windowId && !it.isMinimized }
                    .maxByOrNull { it.zIndex }?.id
            )
        }
    }

    fun toggleMaximizeWindow(windowId: String) {
        AudioSynth.playClickSound()
        _uiState.update { state ->
            state.copy(
                openWindows = state.openWindows.map {
                    if (it.id == windowId) it.copy(isMaximized = !it.isMaximized) else it
                }
            )
        }
    }

    fun updateWindowPosition(windowId: String, newX: Float, newY: Float) {
        _uiState.update { state ->
            state.copy(
                openWindows = state.openWindows.map {
                    if (it.id == windowId) it.copy(offsetX = newX.coerceAtLeast(0f), offsetY = newY.coerceAtLeast(0f)) else it
                }
            )
        }
    }

    fun bringToFront(windowId: String) {
        zIndexCounter += 1f
        _uiState.update { state ->
            state.copy(
                openWindows = state.openWindows.map {
                    if (it.id == windowId) it.copy(zIndex = zIndexCounter, isMinimized = false) else it
                },
                activeWindowId = windowId
            )
        }
    }

    fun toggleStartMenu() {
        AudioSynth.playClickSound()
        _uiState.update { it.copy(isStartMenuOpen = !it.isStartMenuOpen, isOsSwitcherOpen = false) }
    }

    fun toggleOsSwitcher() {
        AudioSynth.playClickSound()
        _uiState.update { it.copy(isOsSwitcherOpen = !it.isOsSwitcherOpen, isStartMenuOpen = false) }
    }

    fun dismissOverlays() {
        _uiState.update { it.copy(isStartMenuOpen = false, isOsSwitcherOpen = false) }
    }

    fun triggerBsod(customMsg: String? = null) {
        AudioSynth.playErrorChord()
        val os = _uiState.value.currentOs
        val msg = customMsg ?: when (os.family) {
            OsFamily.WINDOWS -> "A problem has been detected and Windows has been shut down to prevent damage to your computer.\n\n*** STOP: 0x000000D1 (0x0000000C, 0x00000002, 0x00000000, 0xF86B5A89)\n*** WINUX_DRIVER.SYS - Address F86B5A89 base at F86B5000\n\nTap anywhere to reboot."
            OsFamily.MACOS -> "You need to restart your computer. Hold down the Power button for several seconds or touch anywhere to restart.\n\nVeuillez redémarrer votre ordinateur.\nSie müssen Ihren Computer neu starten.\nmacOS Kernel Panic: cpu 0 caller 0xffffff800049f579"
            OsFamily.LINUX -> "Kernel panic - not syncing: Fatal exception in interrupt\n[ 1.482012] Kernel Offset: 0x24000000 from 0xffffffff81000000\n[ 1.482910] ---[ end Kernel panic - not syncing: Fatal exception ]---\n\nTouch anywhere to recover system."
        }

        _uiState.update {
            it.copy(
                isBsod = true,
                bsodMessage = msg,
                isStartMenuOpen = false
            )
        }
    }

    fun recoverFromBsod() {
        _uiState.update { it.copy(isBsod = false) }
        switchOs(_uiState.value.currentOs)
    }

    fun handleTerminalCommand(cmd: String) {
        val trimmed = cmd.trim()
        val currentHistory = _uiState.value.terminalHistory
        val os = _uiState.value.currentOs
        val prompt = when (os.family) {
            OsFamily.WINDOWS -> "C:\\>"
            OsFamily.MACOS -> "guest@macbook ~ %"
            OsFamily.LINUX -> "guest@winux:~$ "
        }

        val output = mutableListOf<String>()
        output.add("$prompt $trimmed")

        val parts = trimmed.split(" ")
        val mainCmd = parts.firstOrNull()?.lowercase() ?: ""

        when (mainCmd) {
            "help" -> {
                output.add("Available Terminal commands:")
                output.add("  help              - Show this manual")
                output.add("  dir / ls          - List directory files")
                output.add("  cat / type <file> - Read a file")
                output.add("  matrix            - Enter the Matrix stream")
                output.add("  neofetch          - Print system ASCII art & specs")
                output.add("  ping <host>       - Ping network host")
                output.add("  sw_vers / uname   - Show OS kernel information")
                output.add("  cls / clear       - Clear terminal screen")
                output.add("  bsod / panic      - Trigger crash test")
                output.add("  sudo rm -rf /     - DANGEROUS hacker root test")
                output.add("  hack              - Run automated cyber sequence")
            }
            "cls", "clear" -> {
                _uiState.update { it.copy(terminalHistory = emptyList()) }
                return
            }
            "dir", "ls" -> {
                output.add(if (os.family == OsFamily.WINDOWS) " Volume in drive C has no label." else "drwxr-xr-x 4 guest staff 4096")
                _uiState.value.virtualFiles.forEach { f ->
                    val type = if (f.isDir) "<DIR>" else f.sizeStr
                    output.add(String.format(Locale.US, "  %-22s %8s", f.name, type))
                }
            }
            "cat", "type" -> {
                val target = parts.getOrNull(1)
                val found = _uiState.value.virtualFiles.find { it.name.equals(target, ignoreCase = true) }
                if (found != null) {
                    output.addAll(found.content.lines())
                } else {
                    output.add("File not found: ${target ?: "<filename>"}")
                }
            }
            "ver", "uname", "sw_vers" -> {
                output.add("ProductName:    ${os.displayName}")
                output.add("ProductVersion: ${os.releaseYear}.1")
                output.add("BuildVersion:   24A348")
            }
            "neofetch" -> {
                if (os.family == OsFamily.MACOS) {
                    output.add("                    'c.          OS: ${os.displayName}")
                    output.add("                 ,xNMM.          Host: Mac Studio M3 Ultra")
                    output.add("               .OMMMMo           Kernel: Darwin 23.4.0")
                    output.add("               OMMM0,            Uptime: 1 hour 42 mins")
                    output.add("     .;loddo:' loolloddol;.      Shell: zsh 5.9")
                    output.add("   cKMMMMMMMMMMNWMMMMMMMMMM0:    Memory: 8192MB / 65536MB")
                } else {
                    output.add("   .---.       OS: ${os.displayName}")
                    output.add("  /     \\      Host: Winux Virtual Box PC")
                    output.add(" () ()   |     Kernel: 6.8.0-winux")
                    output.add("  (_     _)    Uptime: 42 mins")
                    output.add("   /   /       Memory: 4096MB / 16384MB")
                }
            }
            "matrix" -> {
                output.add("01010101 W A K E   U P   N E O 01010101")
                output.add("The Matrix has you...")
                output.add("Follow the white rabbit. 01001000 01100001")
            }
            "ping" -> {
                val host = parts.getOrNull(1) ?: "apple.com"
                output.add("PING $host (17.253.144.10): 56 data bytes")
                output.add("64 bytes from 17.253.144.10: icmp_seq=0 ttl=58 time=12.4 ms")
                output.add("64 bytes from 17.253.144.10: icmp_seq=1 ttl=58 time=11.9 ms")
                output.add("--- $host ping statistics: 2 packets, 0% loss ---")
            }
            "bsod", "panic" -> {
                triggerBsod()
                return
            }
            "sudo" -> {
                if (trimmed.contains("rm -rf")) {
                    output.add("DELETING SYSTEM FILES / ROOT FS...")
                    triggerBsod("CRITICAL: ROOT DIRECTORY DELETED BY SUDO COMMAND")
                    return
                } else {
                    output.add("[sudo] password for guest: *******")
                    output.add("guest is not in sudoers file.")
                }
            }
            "hack" -> {
                output.add("[+] Initializing Nmap 7.94 scan on 127.0.0.1...")
                output.add("[+] Found open port 22/tcp (ssh), 80/tcp (http), 443/tcp (https)")
                output.add("[+] Exploiting buffer overflow... SUCCESS")
                output.add("[***] ROOT ACCESS GRANTED!")
                AudioSynth.playSuccessFanfare()
            }
            "" -> {}
            else -> {
                output.add("'$trimmed' is not recognized as an internal or external command.")
            }
        }

        _uiState.update {
            it.copy(
                terminalHistory = currentHistory + output,
                terminalCurrentInput = ""
            )
        }
    }

    fun showToast(msg: String) {
        _uiState.update { it.copy(notificationToast = msg) }
    }

    fun clearToast() {
        _uiState.update { it.copy(notificationToast = null) }
    }

    fun updateNotepadContent(content: String) {
        _uiState.update { it.copy(activeNotepadFile = content) }
    }
}
