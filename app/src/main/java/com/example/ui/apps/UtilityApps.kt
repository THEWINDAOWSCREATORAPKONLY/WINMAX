package com.example.ui.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OsFamily
import com.example.model.OsVersionId
import com.example.util.AudioSynth

// --- Notepad / TextEdit App ---
@Composable
fun NotepadApp(
    initialText: String,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(initialText) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(8.dp)
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onContentChange(it)
            },
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 18.sp
            ),
            modifier = Modifier
                .fillMaxSize()
                .testTag("notepad_text_editor")
        )
    }
}

// --- System Specs / About This Mac App ---
@Composable
fun SysInfoApp(
    currentOs: OsVersionId,
    modifier: Modifier = Modifier
) {
    val isMac = currentOs.family == OsFamily.MACOS

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isMac) {
                Text(
                    text = "",
                    fontSize = 38.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 8.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = null,
                    tint = Color(currentOs.accentHex),
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(
                    text = currentOs.displayName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "Release: ${currentOs.releaseYear} • Codename: ${currentOs.codename}",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        Divider()

        SpecRow("Family", currentOs.family.name)
        SpecRow(
            "Kernel Architecture",
            when (currentOs.family) {
                OsFamily.MACOS -> "Darwin Mach-O Kernel 24.1 (64-bit arm64/x86_64)"
                OsFamily.WINDOWS -> "Windows NT / MS-DOS Hybrid Kernel"
                OsFamily.LINUX -> "Linux Monolithic Kernel 6.8.9"
            }
        )
        SpecRow("Processor", if (isMac) "Apple M3 Max (16 cores)" else "Intel Core i7-14700K @ 3.4GHz")
        SpecRow("Unified Memory", "64 GB High-Bandwidth Unified RAM")
        SpecRow("Graphics", if (isMac) "Apple Metal 3 Graphics (40-core GPU)" else "DirectX / Vulkan Accelerated")
        SpecRow("All OS Access", "100% Free & Unlocked")

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                .padding(10.dp)
        ) {
            Text(
                text = "Tagline: \"${currentOs.tagline}\"",
                fontSize = 11.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = Color(0xFF334155)
            )
        }
    }
}

@Composable
fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Color(0xFF475569))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
    }
}

// --- Package / App Manager (Free for all) ---
@Composable
fun PackageManagerApp(
    currentOs: OsVersionId,
    onPackageInstalled: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isMac = currentOs.family == OsFamily.MACOS

    val packages = remember {
        mutableStateMapOf(
            (if (isMac) "Homebrew CLI" else "neofetch-cli") to false,
            (if (isMac) "Xcode Tools" else "doom-shareware") to false,
            (if (isMac) "Safari Preview" else "matrix-cmatrix") to false,
            "htop-monitor" to false,
            "vlc-media-player" to false
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF181825))
            .padding(10.dp)
    ) {
        Text(
            text = if (isMac) "Mac App Store & Homebrew" else "Synaptic & Winget Repo",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Install free simulated packages and tools",
            color = Color(0xFFBAC2DE),
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(packages.keys.toList()) { pkg ->
                val isInstalled = packages[pkg] == true
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E1E2E))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(pkg, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text(if (isInstalled) "Status: Installed" else "Status: Free Download", color = Color.Gray, fontSize = 10.sp)
                    }

                    Button(
                        onClick = {
                            if (!isInstalled) {
                                packages[pkg] = true
                                AudioSynth.playSuccessFanfare()
                                onPackageInstalled()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isInstalled) Color(0xFF313244) else Color(0xFF89B4FA)
                        ),
                        modifier = Modifier.height(32.dp).testTag("btn_install_$pkg")
                    ) {
                        Text(
                            text = if (isInstalled) "Installed" else "Get (Free)",
                            fontSize = 11.sp,
                            color = if (isInstalled) Color.Gray else Color.Black
                        )
                    }
                }
            }
        }
    }
}
