package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun BootScreen(
    targetOs: OsVersionId,
    progress: Float,
    logs: List<String>,
    modifier: Modifier = Modifier
) {
    val isWin = targetOs.family == OsFamily.WINDOWS
    val isMac = targetOs.family == OsFamily.MACOS

    val bootBg = if (isMac && targetOs.releaseYear <= 1999) Color(0xFFC0C0C0) else Color.Black

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bootBg)
            .testTag("boot_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // OS Logo / Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(targetOs.accentHex).copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                if (isMac) {
                    Text(
                        text = if (targetOs.releaseYear <= 1999) "☻" else "",
                        color = if (bootBg == Color.Black) Color.White else Color.Black,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = if (isWin) Icons.Default.Computer else Icons.Default.Terminal,
                        contentDescription = null,
                        tint = Color(targetOs.accentHex),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = targetOs.displayName,
                color = if (bootBg == Color.Black) Color.White else Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = if (targetOs.releaseYear < 2002) FontFamily.Monospace else FontFamily.SansSerif
            )

            Text(
                text = targetOs.tagline,
                color = if (bootBg == Color.Black) Color(0xFF94A3B8) else Color(0xFF475569),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (isMac) Color.White else Color(targetOs.accentHex),
                trackColor = Color(0xFF334155)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Console output logs
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                logs.takeLast(4).forEach { log ->
                    Text(
                        text = log,
                        color = when (targetOs.family) {
                            OsFamily.WINDOWS -> Color(0xFFE2E8F0)
                            OsFamily.MACOS -> Color(0xFF38BDF8)
                            OsFamily.LINUX -> Color(0xFF4ADE80)
                        },
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun BsodCrashScreen(
    currentOs: OsVersionId,
    message: String,
    onReboot: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isMac = currentOs.family == OsFamily.MACOS
    val isWin = currentOs.family == OsFamily.WINDOWS

    val bgColor = if (isMac) Color(0xFF1E1E24) else if (isWin) Color(0xFF0000AA) else Color(0xFF090D16)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .clickable { onReboot() }
            .padding(32.dp)
            .testTag("bsod_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = if (isMac) " Kernel Panic — Tap screen to restart" else ":( Your PC ran into a problem and needs to restart.",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = message,
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Recovery:",
                color = Color(0xFFFFFF80),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Text(
                text = "• Tap anywhere on the screen to reboot ${currentOs.displayName}\n• 100% Free - Switch to any Windows, Mac, or Linux version anytime in OS Hub!",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
