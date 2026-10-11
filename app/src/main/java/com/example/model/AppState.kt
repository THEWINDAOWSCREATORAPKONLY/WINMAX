package com.example.model

enum class AppType(
    val title: String,
    val iconName: String
) {
    MINESWEEPER("Minesweeper", "mine"),
    TERMINAL("Terminal / Shell", "terminal"),
    FILE_EXPLORER("File Explorer / Finder", "folder"),
    NOTEPAD("TextEdit / Notepad", "note"),
    PAINT("Paint / Canvas", "brush"),
    SYS_INFO("System Specs / About Mac", "cpu"),
    PACKAGE_MGR("App Store / Homebrew", "download")
}

data class WindowState(
    val id: String,
    val type: AppType,
    val title: String,
    val isMinimized: Boolean = false,
    val isMaximized: Boolean = false,
    val offsetX: Float = 40f,
    val offsetY: Float = 40f,
    val width: Float = 330f,
    val height: Float = 380f,
    val zIndex: Float = 1f
)

data class SimFile(
    val name: String,
    val isDir: Boolean,
    val content: String = "",
    val sizeStr: String = "1 KB"
)
