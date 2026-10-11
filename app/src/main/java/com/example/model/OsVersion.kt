package com.example.model

enum class OsFamily {
    WINDOWS,
    LINUX,
    MACOS
}

enum class OsVersionId(
    val id: String,
    val family: OsFamily,
    val displayName: String,
    val releaseYear: Int,
    val codename: String,
    val tagline: String,
    val defaultWallHex: Long,
    val accentHex: Long,
    val taskbarHex: Long,
    val taskbarTextHex: Long,
    val windowHeaderHex: Long,
    val windowHeaderTextHex: Long
) {
    // --- Windows Family ---
    WIN_95(
        id = "win95",
        family = OsFamily.WINDOWS,
        displayName = "Windows 95",
        releaseYear = 1995,
        codename = "Chicago",
        tagline = "Where do you want to go today?",
        defaultWallHex = 0xFF008080, // Teal classic
        accentHex = 0xFF000080,
        taskbarHex = 0xFFC0C0C0,
        taskbarTextHex = 0xFF000000,
        windowHeaderHex = 0xFF000080,
        windowHeaderTextHex = 0xFFFFFFFF
    ),
    WIN_98(
        id = "win98",
        family = OsFamily.WINDOWS,
        displayName = "Windows 98",
        releaseYear = 1998,
        codename = "Memphis",
        tagline = "Works Better. Plays Better.",
        defaultWallHex = 0xFF008080,
        accentHex = 0xFF1084D0,
        taskbarHex = 0xFFC0C0C0,
        taskbarTextHex = 0xFF000000,
        windowHeaderHex = 0xFF0A246A,
        windowHeaderTextHex = 0xFFFFFFFF
    ),
    WIN_2000(
        id = "win2000",
        family = OsFamily.WINDOWS,
        displayName = "Windows 2000",
        releaseYear = 2000,
        codename = "NT 5.0",
        tagline = "Built on NT Technology for Reliability",
        defaultWallHex = 0xFF3A6EA5,
        accentHex = 0xFF003366,
        taskbarHex = 0xFFD4D0C8,
        taskbarTextHex = 0xFF000000,
        windowHeaderHex = 0xFF0A246A,
        windowHeaderTextHex = 0xFFFFFFFF
    ),
    WIN_XP(
        id = "winxp",
        family = OsFamily.WINDOWS,
        displayName = "Windows XP",
        releaseYear = 2001,
        codename = "Whistler",
        tagline = "Experience the Best of the Digital Age",
        defaultWallHex = 0xFF245EDC, // Bliss sky blue
        accentHex = 0xFF3C8127,      // Green start button
        taskbarHex = 0xFF245EDC,
        taskbarTextHex = 0xFFFFFFFF,
        windowHeaderHex = 0xFF0055EA,
        windowHeaderTextHex = 0xFFFFFFFF
    ),
    WIN_VISTA(
        id = "winvista",
        family = OsFamily.WINDOWS,
        displayName = "Windows Vista",
        releaseYear = 2006,
        codename = "Longhorn",
        tagline = "The Clear Choice (Aero Glass)",
        defaultWallHex = 0xFF1A385C,
        accentHex = 0xFF00A2E8,
        taskbarHex = 0xDD111827,
        taskbarTextHex = 0xFFFFFFFF,
        windowHeaderHex = 0xDD1F2937,
        windowHeaderTextHex = 0xFFE5E7EB
    ),
    WIN_7(
        id = "win7",
        family = OsFamily.WINDOWS,
        displayName = "Windows 7",
        releaseYear = 2009,
        codename = "Blackcomb",
        tagline = "Simplified Everyday Tasks & Aero Glow",
        defaultWallHex = 0xFF1E3A8A,
        accentHex = 0xFF38BDF8,
        taskbarHex = 0xDD0F172A,
        taskbarTextHex = 0xFFF8FAFC,
        windowHeaderHex = 0xDD1E293B,
        windowHeaderTextHex = 0xFFFFFFFF
    ),
    WIN_8(
        id = "win8",
        family = OsFamily.WINDOWS,
        displayName = "Windows 8.1",
        releaseYear = 2012,
        codename = "Blue",
        tagline = "Metro UI & Modern Live Tiles",
        defaultWallHex = 0xFF4C1D95, // Purple tile vibe
        accentHex = 0xFF7C3AED,
        taskbarHex = 0xFF1F2937,
        taskbarTextHex = 0xFFFFFFFF,
        windowHeaderHex = 0xFF6D28D9,
        windowHeaderTextHex = 0xFFFFFFFF
    ),
    WIN_10(
        id = "win10",
        family = OsFamily.WINDOWS,
        displayName = "Windows 10",
        releaseYear = 2015,
        codename = "Threshold",
        tagline = "The OS that works seamlessly across devices",
        defaultWallHex = 0xFF0284C7, // Blue laser window hero
        accentHex = 0xFF0284C7,
        taskbarHex = 0xFF000000,
        taskbarTextHex = 0xFFFFFFFF,
        windowHeaderHex = 0xFF18181B,
        windowHeaderTextHex = 0xFFFFFFFF
    ),
    WIN_11(
        id = "win11",
        family = OsFamily.WINDOWS,
        displayName = "Windows 11",
        releaseYear = 2021,
        codename = "Sun Valley",
        tagline = "Centered Start & Acrylic Bloom Aesthetics",
        defaultWallHex = 0xFF3B82F6, // Bloom blue
        accentHex = 0xFF2563EB,
        taskbarHex = 0xE6F8FAFC,     // Light acrylic taskbar
        taskbarTextHex = 0xFF0F172A,
        windowHeaderHex = 0xF2FFFFFF,
        windowHeaderTextHex = 0xFF1E293B
    ),

    // --- Linux Family ---
    LINUX_DEBIAN(
        id = "debian",
        family = OsFamily.LINUX,
        displayName = "Debian GNU/Linux",
        releaseYear = 1993,
        codename = "Bookworm",
        tagline = "The Universal Operating System",
        defaultWallHex = 0xFF831843, // Debian crimson
        accentHex = 0xFFBE185D,
        taskbarHex = 0xFF1C1917,
        taskbarTextHex = 0xFFF5F5F4,
        windowHeaderHex = 0xFF292524,
        windowHeaderTextHex = 0xFFFFFFFF
    ),
    LINUX_UBUNTU(
        id = "ubuntu",
        family = OsFamily.LINUX,
        displayName = "Ubuntu Linux",
        releaseYear = 2004,
        codename = "Noble Numbat",
        tagline = "Linux for Human Beings (GNOME / Yaru)",
        defaultWallHex = 0xFF7C2D12, // Aubergine / Warm Orange
        accentHex = 0xFFEA580C,      // Canonical Orange
        taskbarHex = 0xFF111827,
        taskbarTextHex = 0xFFF97316,
        windowHeaderHex = 0xFF1F2937,
        windowHeaderTextHex = 0xFFFFFFFF
    ),
    LINUX_ARCH(
        id = "arch",
        family = OsFamily.LINUX,
        displayName = "Arch Linux",
        releaseYear = 2002,
        codename = "Rolling",
        tagline = "A lightweight and flexible Linux distribution",
        defaultWallHex = 0xFF0369A1, // Arch cyan/blue
        accentHex = 0xFF0284C7,
        taskbarHex = 0xFF0F172A,
        taskbarTextHex = 0xFF38BDF8,
        windowHeaderHex = 0xFF0B1120,
        windowHeaderTextHex = 0xFF38BDF8
    ),
    LINUX_KALI(
        id = "kali",
        family = OsFamily.LINUX,
        displayName = "Kali Linux",
        releaseYear = 2013,
        codename = "Dragon",
        tagline = "Our Most Advanced Penetration Testing Distro",
        defaultWallHex = 0xFF090D16, // Dark Dragon theme
        accentHex = 0xFF22C55E,      // Matrix hacker green
        taskbarHex = 0xFF050505,
        taskbarTextHex = 0xFF22C55E,
        windowHeaderHex = 0xFF111827,
        windowHeaderTextHex = 0xFF4ADE80
    ),
    LINUX_MINT(
        id = "mint",
        family = OsFamily.LINUX,
        displayName = "Linux Mint",
        releaseYear = 2006,
        codename = "Victoria",
        tagline = "From Freedom came Elegance (Cinnamon)",
        defaultWallHex = 0xFF064E3B, // Forest mint green
        accentHex = 0xFF10B981,
        taskbarHex = 0xFF1F2937,
        taskbarTextHex = 0xFF34D399,
        windowHeaderHex = 0xFF111827,
        windowHeaderTextHex = 0xFFE5E7EB
    ),
    LINUX_REDHAT(
        id = "fedora",
        family = OsFamily.LINUX,
        displayName = "Fedora / Red Hat",
        releaseYear = 2003,
        codename = "Rawhide",
        tagline = "Freedom, Friends, Features, First",
        defaultWallHex = 0xFF1E3A8A,
        accentHex = 0xFFDC2626,      // Red Hat
        taskbarHex = 0xFF0F172A,
        taskbarTextHex = 0xFFEF4444,
        windowHeaderHex = 0xFF1E293B,
        windowHeaderTextHex = 0xFFFFFFFF
    ),

    // --- macOS Family ---
    MAC_SYSTEM_7(
        id = "mac_sys7",
        family = OsFamily.MACOS,
        displayName = "System 7 (Classic Mac)",
        releaseYear = 1991,
        codename = "Big Mac",
        tagline = "The Macintosh System Software that changed everything",
        defaultWallHex = 0xFF808080, // Classic 50% gray pinstripe
        accentHex = 0xFF000000,
        taskbarHex = 0xFFE0E0E0,     // Top white/silver menu bar
        taskbarTextHex = 0xFF000000,
        windowHeaderHex = 0xFFD8D8D8,
        windowHeaderTextHex = 0xFF000000
    ),
    MAC_OS_9(
        id = "mac_os9",
        family = OsFamily.MACOS,
        displayName = "Mac OS 9",
        releaseYear = 1999,
        codename = "Sonata",
        tagline = "The Best Mac OS Ever (Platinum Appearance)",
        defaultWallHex = 0xFF4A6572, // Platinum slate
        accentHex = 0xFF37474F,
        taskbarHex = 0xFFECEFF1,
        taskbarTextHex = 0xFF263238,
        windowHeaderHex = 0xFFCFD8DC,
        windowHeaderTextHex = 0xFF212121
    ),
    MAC_OS_X_CHEETAH(
        id = "mac_osx_10_0",
        family = OsFamily.MACOS,
        displayName = "Mac OS X Cheetah & Aqua",
        releaseYear = 2001,
        codename = "Cheetah / Puma",
        tagline = "Aqua UI: Delicious, lickable pinstripe glass",
        defaultWallHex = 0xFF1565C0, // Iconic Aqua blue curves
        accentHex = 0xFF29B6F6,
        taskbarHex = 0xEEF5F5F5,
        taskbarTextHex = 0xFF000000,
        windowHeaderHex = 0xEEE0E0E0,
        windowHeaderTextHex = 0xFF212121
    ),
    MAC_OS_X_SNOW_LEOPARD(
        id = "mac_snow_leopard",
        family = OsFamily.MACOS,
        displayName = "Mac OS X Snow Leopard",
        releaseYear = 2009,
        codename = "Snow Leopard",
        tagline = "Refining perfection: 0 new features, ultimate speed",
        defaultWallHex = 0xFF37474F, // Space nebula purple/dark slate
        accentHex = 0xFF78909C,
        taskbarHex = 0xDD37474F,
        taskbarTextHex = 0xFFFFFFFF,
        windowHeaderHex = 0xFFE0E0E0,
        windowHeaderTextHex = 0xFF212121
    ),
    MAC_OS_BIG_SUR(
        id = "mac_big_sur",
        family = OsFamily.MACOS,
        displayName = "macOS Big Sur",
        releaseYear = 2020,
        codename = "Big Sur",
        tagline = "The biggest design upgrade since OS X",
        defaultWallHex = 0xFFE65100, // Vibrant chromatic coastal peaks
        accentHex = 0xFF0288D1,
        taskbarHex = 0xCC1E1E24,
        taskbarTextHex = 0xFFFFFFFF,
        windowHeaderHex = 0xEEFAFAFA,
        windowHeaderTextHex = 0xFF1F2937
    ),
    MAC_OS_SONOMA(
        id = "mac_sonoma",
        family = OsFamily.MACOS,
        displayName = "macOS Sonoma",
        releaseYear = 2023,
        codename = "Sonoma",
        tagline = "Dynamic slow-motion screensavers and desktop widgets",
        defaultWallHex = 0xFF1E3A8A, // Sonoma green/blue hills
        accentHex = 0xFF3B82F6,
        taskbarHex = 0xB3000000,     // Translucent blurred top bar
        taskbarTextHex = 0xFFFFFFFF,
        windowHeaderHex = 0xF0FFFFFF,
        windowHeaderTextHex = 0xFF0F172A
    ),
    MAC_OS_SEQUOIA(
        id = "mac_sequoia",
        family = OsFamily.MACOS,
        displayName = "macOS Sequoia",
        releaseYear = 2024,
        codename = "Sequoia",
        tagline = "iPhone Mirroring & Apple Intelligence Redwood Forest",
        defaultWallHex = 0xFF14532D, // Deep Sequoia Redwood Forest Green
        accentHex = 0xFF16A34A,
        taskbarHex = 0x9918181B,
        taskbarTextHex = 0xFFF4F4F5,
        windowHeaderHex = 0xF5FFFFFF,
        windowHeaderTextHex = 0xFF18181B
    )
}
