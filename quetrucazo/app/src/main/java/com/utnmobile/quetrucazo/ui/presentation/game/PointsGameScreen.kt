package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.utnmobile.quetrucazo.ui.presentation.iconDialogColor

@Composable
fun PointsGameScreen(modifier: Modifier = Modifier, myPoints: Int, opponentPoints: Int) {
    Column(
        modifier = modifier.wrapContentHeight().fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        PointCounter(text = "TU", points = myPoints, color = iconDialogColor())
        PointCounter(text = "ÉL", points = opponentPoints, color = iconDialogColor())
    }
}

@Composable
fun PointCounter(text: String, points: Int, color: Color = Color.Black) {
    val fullSets = points / 5
    val remaining = points % 5
    val emptySets = 6 - fullSets - if (remaining > 0) 1 else 0

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = text, color = color, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        repeat(fullSets) {
            DrawStrokes(5, color)
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (remaining > 0) {
            DrawStrokes(remaining, color)
            Spacer(modifier = Modifier.height(8.dp))
        }
        repeat(emptySets) {
            DrawEmptyBox()
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DrawStrokes(count: Int, color: Color) {
    Box(modifier = Modifier.size(32.dp).padding(4.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (i in 0 until count) {
                when (i) {
                    0 -> drawLine(color, Offset(0f, 0f), Offset(0f, size.height), strokeWidth = 6f)
                    1 -> drawLine(color, Offset(0f, 0f), Offset(size.width, 0f), strokeWidth = 6f)
                    2 -> drawLine(color, Offset(size.width, size.height), Offset(size.width, 0f), strokeWidth = 6f)
                    3 -> drawLine(color, Offset(size.width, size.height), Offset(0f, size.height), strokeWidth = 6f)
                    4 -> drawLine(color, Offset(0f, 0f), Offset(size.width, size.height), strokeWidth = 6f)
                }
            }
        }
    }
}

@Composable
fun DrawEmptyBox() {
    Box(modifier = Modifier.size(32.dp).padding(4.dp))
}
