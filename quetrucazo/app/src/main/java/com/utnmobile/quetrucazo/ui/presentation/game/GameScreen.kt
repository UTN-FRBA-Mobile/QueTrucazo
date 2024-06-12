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
import com.utnmobile.quetrucazo.model.events.implementations.NextRoundGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.ResultGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.RoundResultGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.StartGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.ThrowCardGameEvent
import com.utnmobile.quetrucazo.model.events.toGameEvents
import com.utnmobile.quetrucazo.services.SocketIOManager
import com.utnmobile.quetrucazo.ui.presentation.NavigateTo
import com.utnmobile.quetrucazo.ui.presentation.YouWinDialog
import com.utnmobile.quetrucazo.ui.viewmodel.auth.AuthViewModel
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel
import kotlinx.coroutines.delay
import org.json.JSONArray

@Composable
fun GameScreen(navigateTo: NavigateTo, game: Game, isPreview: Boolean = false) {

    var userId = 1

    var analyzingEvents = false

    if (!isPreview) {
        viewModel<MusicViewModel>().playMusic()
        userId = viewModel<AuthViewModel>().user!!.id
    }

    var events by remember { mutableStateOf(listOf(*game.events.toTypedArray())) }
    var eventIndex by remember { mutableIntStateOf(game.events.size) }

    var myCards by remember { mutableStateOf(game.state.userCards(userId)) }
    var opponentCardsSize by remember { mutableIntStateOf(game.state.opponentCardsSize(userId)) }

    var myPoints by remember { mutableIntStateOf(game.state.myPoints(userId)) }
    var opponentPoints by remember { mutableIntStateOf(game.state.opponentPoints(userId)) }

    var myThrownCards by remember { mutableStateOf(game.state.myThrownCards(userId)) }
    var opponentThrownCards by remember { mutableStateOf(game.state.opponentThrownCards(userId)) }

    var winner by remember { mutableStateOf(game.state.winner) }

    var myTurn by remember { mutableStateOf(game.state.playerTurn == userId) }

    var showWinDialog by remember { mutableStateOf(false) }

    suspend fun analyzeEvents() {
        analyzingEvents = true
        println("Analyzing events: $eventIndex - ${events.size}")
        if (eventIndex >= events.size) {
            analyzingEvents = false
            return
        }

        println("Analyzing event $eventIndex: ${events[eventIndex]}")

        when (val event = events[eventIndex]) {
            is NextRoundGameEvent -> {
                delay(1500)
                myThrownCards = emptyList()
                opponentThrownCards = emptyList()
                myCards = event.cards[userId] ?: emptyList()
                opponentCardsSize = event.cards.entries.first { it.key != userId }.value.size
                myTurn = event.nextPlayerId == userId
            }

            is ResultGameEvent -> {
                myPoints = event.points[userId] ?: 0
                opponentPoints = event.points.entries.first { it.key != userId }.value
                winner = event.winner
                showWinDialog = winner == userId
            }

            is RoundResultGameEvent -> {
                myPoints = event.points[userId] ?: 0
                opponentPoints = event.points.entries.first { it.key != userId }.value
            }

            is StartGameEvent -> {}
            is ThrowCardGameEvent -> {

                if (event.playerId != userId) {
                    opponentThrownCards += event.card
                    opponentCardsSize--
                }

                myTurn = event.nextPlayerId == userId
            }
        }
        eventIndex++
        analyzeEvents()
    }

    LaunchedEffect(events) {
        if (!analyzingEvents) {
            analyzeEvents()
        }
    }

    LaunchedEffect(Unit) {
        SocketIOManager.socket?.on("new-events") { args ->
            val newEvents = (args[0] as JSONArray).toGameEvents()
            println(newEvents)
            events += newEvents
        }
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
                    addMyThrownCard = { card ->
                        myThrownCards = myThrownCards + card
                    },
                    opponentCardsSize = opponentCardsSize,
                    myThrownCards = myThrownCards,
                    opponentThrownCards = opponentThrownCards,
                    myTurn = myTurn,
                    gameId = game.id,
                    userId = userId,
                )

                Spacer(modifier = Modifier.weight(1f))

                PlayGameScreen(
                    modifier = Modifier.fillMaxWidth()
                )

                if (showWinDialog) {
                    YouWinDialog(
                        onDismissRequest = { showWinDialog = false },
                        myPoints = myPoints,
                        opponentPoints = opponentPoints,
                        navigateTo = navigateTo
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreen(game = Game.default, isPreview = true, navigateTo = { _, _ -> })
}
