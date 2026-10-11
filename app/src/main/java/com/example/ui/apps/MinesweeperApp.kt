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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.AudioSynth
import kotlin.random.Random

data class MineCell(
    val row: Int,
    val col: Int,
    val isMine: Boolean,
    var isRevealed: Boolean = false,
    var isFlagged: Boolean = false,
    var neighborMines: Int = 0
)

@Composable
fun MinesweeperApp(
    onGameSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val rows = 8
    val cols = 7
    val mineCount = 8

    var isGameOver by remember { mutableStateOf(false) }
    var isGameWon by remember { mutableStateOf(false) }
    var flagsLeft by remember { mutableIntStateOf(mineCount) }
    var revealedSafeCount by remember { mutableIntStateOf(0) }

    val grid = remember {
        mutableStateListOf<MineCell>().apply {
            addAll(generateGrid(rows, cols, mineCount))
        }
    }

    fun resetGame() {
        grid.clear()
        grid.addAll(generateGrid(rows, cols, mineCount))
        isGameOver = false
        isGameWon = false
        flagsLeft = mineCount
        revealedSafeCount = 0
    }

    fun revealCell(cell: MineCell) {
        if (isGameOver || isGameWon || cell.isRevealed || cell.isFlagged) return

        if (cell.isMine) {
            AudioSynth.playErrorChord()
            isGameOver = true
            grid.forEach { if (it.isMine) it.isRevealed = true }
        } else {
            AudioSynth.playClickSound()
            cell.isRevealed = true
            revealedSafeCount++

            if (cell.neighborMines == 0) {
                val neighbors = getNeighbors(cell.row, cell.col, rows, cols, grid)
                neighbors.filter { !it.isRevealed && !it.isMine }.forEach { revealCell(it) }
            }

            val totalSafe = (rows * cols) - mineCount
            val revealedNow = grid.count { it.isRevealed && !it.isMine }
            if (revealedNow >= totalSafe) {
                isGameWon = true
                AudioSynth.playSuccessFanfare()
                onGameSuccess()
            }
        }
    }

    fun toggleFlag(cell: MineCell) {
        if (isGameOver || isGameWon || cell.isRevealed) return
        AudioSynth.playClickSound()
        cell.isFlagged = !cell.isFlagged
        flagsLeft += if (cell.isFlagged) -1 else 1
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFC0C0C0))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top control panel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB0B0B0), RoundedCornerShape(4.dp))
                .border(2.dp, Color(0xFF808080), RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Black, RoundedCornerShape(2.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = String.format("%03d", flagsLeft.coerceAtLeast(0)),
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFFD4D0C8), RoundedCornerShape(2.dp))
                    .border(2.dp, Color(0xFFFFFFFF), RoundedCornerShape(2.dp))
                    .clickable { resetGame() }
                    .testTag("minesweeper_reset_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when {
                        isGameWon -> "😎"
                        isGameOver -> "😵"
                        else -> "🙂"
                    },
                    fontSize = 18.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(Color.Black, RoundedCornerShape(2.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isGameWon) "WIN" else if (isGameOver) "DIE" else "RUN",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid board
        Column(
            modifier = Modifier
                .background(Color(0xFF808080), RoundedCornerShape(4.dp))
                .border(2.dp, Color(0xFF404040), RoundedCornerShape(4.dp))
                .padding(4.dp)
        ) {
            for (r in 0 until rows) {
                Row {
                    for (c in 0 until cols) {
                        val cell = grid[r * cols + c]
                        MinesweeperCell(
                            cell = cell,
                            onTap = { revealCell(cell) },
                            onLongTap = { toggleFlag(cell) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Tap: Reveal | Long tap: Flag 🚩",
            color = Color(0xFF333333),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun MinesweeperCell(
    cell: MineCell,
    onTap: () -> Unit,
    onLongTap: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .padding(1.dp)
            .background(
                if (cell.isRevealed) {
                    if (cell.isMine) Color(0xFFEF4444) else Color(0xFFE0E0E0)
                } else Color(0xFFD4D0C8),
                RoundedCornerShape(2.dp)
            )
            .border(
                width = 1.dp,
                color = if (cell.isRevealed) Color(0xFF9E9E9E) else Color(0xFFFFFFFF),
                shape = RoundedCornerShape(2.dp)
            )
            .clickable { onTap() }
            .testTag("mine_cell_${cell.row}_${cell.col}"),
        contentAlignment = Alignment.Center
    ) {
        when {
            cell.isRevealed && cell.isMine -> {
                Text("💣", fontSize = 14.sp)
            }
            cell.isRevealed && cell.neighborMines > 0 -> {
                val numberColor = when (cell.neighborMines) {
                    1 -> Color(0xFF1D4ED8)
                    2 -> Color(0xFF15803D)
                    3 -> Color(0xFFB91C1C)
                    4 -> Color(0xFF6B21A8)
                    else -> Color(0xFF7F1D1D)
                }
                Text(
                    text = "${cell.neighborMines}",
                    color = numberColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
            cell.isFlagged -> {
                Text("🚩", fontSize = 14.sp)
            }
        }
    }
}

fun generateGrid(rows: Int, cols: Int, mines: Int): List<MineCell> {
    val totalCells = rows * cols
    val mineIndices = mutableSetOf<Int>()
    while (mineIndices.size < mines) {
        mineIndices.add(Random.nextInt(totalCells))
    }

    val list = mutableListOf<MineCell>()
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            val idx = r * cols + c
            list.add(MineCell(r, c, isMine = mineIndices.contains(idx)))
        }
    }

    for (cell in list) {
        if (!cell.isMine) {
            val neighbors = getNeighbors(cell.row, cell.col, rows, cols, list)
            cell.neighborMines = neighbors.count { it.isMine }
        }
    }

    return list
}

fun getNeighbors(r: Int, c: Int, rows: Int, cols: Int, list: List<MineCell>): List<MineCell> {
    val result = mutableListOf<MineCell>()
    for (dr in -1..1) {
        for (dc in -1..1) {
            if (dr == 0 && dc == 0) continue
            val nr = r + dr
            val nc = c + dc
            if (nr in 0 until rows && nc in 0 until cols) {
                result.add(list[nr * cols + nc])
            }
        }
    }
    return result
}
