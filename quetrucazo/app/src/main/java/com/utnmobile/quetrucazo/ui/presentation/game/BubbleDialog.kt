package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyBubbleDialog(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.width(200.dp).height(100.dp)) {
        val zero = 0.dp
        val fifty = 50.dp
        Canvas(modifier = Modifier.matchParentSize().align(Alignment.Center)) {
            val clipPath = Path().apply {
                moveTo(zero.toPx(), fifty.toPx())
                quadraticBezierTo(5.dp.toPx(), 25.dp.toPx(), 40.dp.toPx(), 15.dp.toPx())
                quadraticBezierTo(60.dp.toPx(), 11.dp.toPx(), 80.dp.toPx(), 11.dp.toPx())
                quadraticBezierTo(60.dp.toPx(), 9.dp.toPx(), 140.dp.toPx(), 11.dp.toPx())
                quadraticBezierTo(190.dp.toPx(), 15.dp.toPx(), 200.dp.toPx(), 50.dp.toPx())
                quadraticBezierTo(200.dp.toPx(), 100.dp.toPx(), 100.dp.toPx(), 100.dp.toPx())
                quadraticBezierTo(0.dp.toPx(), 100.dp.toPx(), 0.dp.toPx(), 50.dp.toPx())
                close()
                moveTo(180.dp.toPx(), 85.dp.toPx())
                lineTo(180.dp.toPx(), 90.dp.toPx())
                quadraticBezierTo(180.dp.toPx(), 100.dp.toPx(), 170.dp.toPx(), 125.dp.toPx())
                lineTo(140.dp.toPx(), 90.dp.toPx())
                close()
            }
            drawPath(
                clipPath,
                color = Color.Black,
                style = Stroke(width = 4.dp.toPx())
            )
            drawPath(
                clipPath,
                color = Color.White,
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        ) {
            Text(
                text = text,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun OpponentBubbleDialog(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.width(200.dp).height(100.dp)) {
        val zero = 0.dp
        val fifty = 50.dp
        Canvas(modifier = Modifier.matchParentSize().align(Alignment.Center)) {
            val clipPath = Path().apply {
                moveTo(zero.toPx(), fifty.toPx())
                quadraticBezierTo(5.dp.toPx(), 25.dp.toPx(), 40.dp.toPx(), 15.dp.toPx())
                quadraticBezierTo(60.dp.toPx(), 11.dp.toPx(), 80.dp.toPx(), 11.dp.toPx())
                quadraticBezierTo(60.dp.toPx(), 9.dp.toPx(), 140.dp.toPx(), 11.dp.toPx())
                quadraticBezierTo(190.dp.toPx(), 15.dp.toPx(), 200.dp.toPx(), 50.dp.toPx())
                quadraticBezierTo(200.dp.toPx(), 100.dp.toPx(), 100.dp.toPx(), 100.dp.toPx())
                quadraticBezierTo(0.dp.toPx(), 100.dp.toPx(), 0.dp.toPx(), 50.dp.toPx())
                close()
                moveTo(20.dp.toPx(), 25.dp.toPx())
                lineTo(20.dp.toPx(), 20.dp.toPx())
                quadraticBezierTo(20.dp.toPx(), 10.dp.toPx(), 30.dp.toPx(), -15.dp.toPx())
                lineTo(60.dp.toPx(), 20.dp.toPx())
                close()
            }
            drawPath(
                clipPath,
                color = Color.Black,
                style = Stroke(width = 4.dp.toPx())
            )
            drawPath(
                clipPath,
                color = Color.White,
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        ) {
            Text(
                text = text,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
