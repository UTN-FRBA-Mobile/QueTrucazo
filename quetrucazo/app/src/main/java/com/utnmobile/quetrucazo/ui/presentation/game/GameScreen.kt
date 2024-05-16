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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.model.Card
import com.utnmobile.quetrucazo.model.Game
import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.implementations.NextRoundGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.ResultGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.RoundResultGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.StartGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.ThrowCardGameEvent
import com.utnmobile.quetrucazo.model.events.toGameEvents
import com.utnmobile.quetrucazo.services.SocketIOManager
import com.utnmobile.quetrucazo.ui.viewmodel.auth.AuthViewModel
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel
import org.json.JSONArray
import org.json.JSONObject

@Composable
fun GameScreen(game: Game, isPreview: Boolean = false) {
    var events by remember { mutableStateOf(listOf(*game.events.toTypedArray())) }
    var eventIndex by remember { mutableIntStateOf(0) }

    var myCards by remember { mutableStateOf(emptyList<Card>()) }
    var opponentCardsSize by remember { mutableStateOf(0) }

    var myPoints by remember { mutableIntStateOf(0) }
    var opponentPoints by remember { mutableIntStateOf(0) }

    var myThrownCards by remember { mutableStateOf(emptyList<Card>()) }
    var opponentThrownCards by remember { mutableStateOf(emptyList<Card>()) }

    var winner by remember { mutableStateOf<UserId?>(null) }

    val userId = viewModel<AuthViewModel>().user!!.id

    if (!isPreview) {
        viewModel<MusicViewModel>().playMusic()
    }

    fun analyzeEvents() {
        println("Analyzing events: $eventIndex - ${events.size}")
        if (eventIndex >= events.size) {
            return
        }

        println("Analyzing event $eventIndex: ${events[eventIndex]}")

        when (val event = events[eventIndex]) {
            is NextRoundGameEvent -> {
                myThrownCards = emptyList()
                opponentThrownCards = emptyList()
                myCards = event.cards[userId] ?: emptyList()
                opponentCardsSize = event.cards.entries.first { it.key != userId }.value.size
            }
            is ResultGameEvent -> {
                myPoints = event.points[userId] ?: 0
                opponentPoints = event.points.entries.first { it.key != userId }.value
                winner = event.winner
            }
            is RoundResultGameEvent -> {
                myPoints = event.points[userId] ?: 0
                opponentPoints = event.points.entries.first { it.key != userId }.value
            }
            is StartGameEvent -> {}
            is ThrowCardGameEvent -> {
                if (event.playerId == userId) {
                    myThrownCards += event.card
                    myCards -= event.card
                } else {
                    opponentThrownCards += event.card
                    opponentCardsSize--
                }
            }
        }
        eventIndex++
        analyzeEvents()
    }

    SocketIOManager.socket?.on("new-events") { args ->
        val newEvents = (args[0] as JSONArray).toGameEvents()
        events += newEvents
        analyzeEvents()
    }

    LaunchedEffect(Unit) {
        analyzeEvents()
    }

    DisposableEffect(Unit) {
        onDispose {
            SocketIOManager.socket?.off("new-events")
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
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
                        .padding(vertical = 16.dp),
                    myPoints = myPoints,
                    opponentPoints = opponentPoints
                )

                CardsGameScreen(
                    modifier = Modifier
                        .weight(7f)
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    myCards = myCards,
                    removeMyCard = { card ->
                        myCards = myCards.filter { it != card }
                    },
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
    GameScreen(Game.default, isPreview = true)
}
