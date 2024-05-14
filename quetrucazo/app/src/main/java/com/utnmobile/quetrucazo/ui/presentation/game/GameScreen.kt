package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.model.Game
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel

@Composable
fun GameScreen(game: Game, isPreview: Boolean = false) {

    if (!isPreview) {
        viewModel<MusicViewModel>().playMusic()
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                PointsGameScreen(modifier = Modifier.weight(1f))
                CardsGameScreen(modifier = Modifier.weight(7f))
                PlayGameScreen(modifier = Modifier.weight(2f))
            }
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreen(Game(1, "Preview game"), isPreview = true)
}
