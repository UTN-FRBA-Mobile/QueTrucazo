package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
                .padding( PaddingValues(
                    start = 0.dp,
                    top = paddingValues.calculateTopPadding(),
                    end = 0.dp,
                    bottom = paddingValues.calculateBottomPadding()
                )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                PointsGameScreen(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )

                CardsGameScreen(
                    modifier = Modifier
                        .weight(7f)
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                PlayGameScreen(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreen(Game(1, "Preview game"), isPreview = true)
}
