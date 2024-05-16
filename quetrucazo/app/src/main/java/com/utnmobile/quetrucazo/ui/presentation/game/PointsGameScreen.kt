package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PointsGameScreen(modifier: Modifier = Modifier, myPoints: Int, opponentPoints: Int) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Mis puntos: $myPoints")
        Text(text = "Puntos del oponente: $opponentPoints")
    }
}