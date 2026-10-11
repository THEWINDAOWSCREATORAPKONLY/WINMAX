package com.example.ui.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OsFamily
import com.example.model.OsVersionId

@Composable
fun TerminalApp(
    currentOs: OsVersionId,
    history: List<String>,
    onRunCommand: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isWin = currentOs.family == OsFamily.WINDOWS
    val isKali = currentOs == OsVersionId.LINUX_KALI

    val bgColor = if (isKali) Color(0xFF020902) else if (isWin) Color(0xFF0C0C0C) else Color(0xFF181825)
    val textBaseColor = if (isKali) Color(0xFF22C55E) else if (isWin) Color(0xFFE5E7EB) else Color(0xFFA6E3A1)
    val promptPrefix = if (isWin) "C:\\>" else if (isKali) "root@kali:~# " else "guest@winux:~$ "

    var inputCmd by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(history.size) {
        if (history.isNotEmpty()) {
            listState.animateScrollToItem(history.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(8.dp)
            .testTag("terminal_container")
    ) {
        // Output history
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(history) { line ->
                val color = when {
                    line.startsWith("C:\\>") || line.startsWith("guest@") || line.startsWith("root@") -> Color(0xFF38BDF8)
                    line.contains("ERROR") || line.contains("not recognized") -> Color(0xFFEF4444)
                    line.contains("SUCCESS") || line.contains("GRANTED") -> Color(0xFF4ADE80)
                    else -> textBaseColor
                }
                Text(
                    text = line,
                    color = color,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 16.sp
                )
            }
        }

        // Active command input line
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = promptPrefix,
                color = Color(0xFF38BDF8),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(4.dp))
            BasicTextField(
                value = inputCmd,
                onValueChange = { inputCmd = it },
                textStyle = TextStyle(
                    color = textBaseColor,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                ),
                cursorBrush = SolidColor(textBaseColor),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (inputCmd.isNotBlank()) {
                        onRunCommand(inputCmd)
                        inputCmd = ""
                    }
                }),
                modifier = Modifier
                    .weight(1f)
                    .testTag("terminal_input_field")
            )

            IconButton(
                onClick = {
                    if (inputCmd.isNotBlank()) {
                        onRunCommand(inputCmd)
                        inputCmd = ""
                    }
                },
                modifier = Modifier.testTag("terminal_send_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = textBaseColor
                )
            }
        }
    }
}
