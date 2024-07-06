package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utnmobile.quetrucazo.model.Game
import com.utnmobile.quetrucazo.model.events.implementations.*
import com.utnmobile.quetrucazo.model.events.toGameEvents
import com.utnmobile.quetrucazo.services.SocketIOManager
import com.utnmobile.quetrucazo.ui.presentation.*
import com.utnmobile.quetrucazo.ui.viewmodel.auth.AuthViewModel
import com.utnmobile.quetrucazo.ui.viewmodel.music.MusicViewModel
import kotlinx.coroutines.delay
import org.json.JSONArray
import kotlin.math.min

@Composable
fun GameScreen(navigateTo: NavigateTo, game: Game, isPreview: Boolean = false) {

    var userId = 1

    var analyzingEvents by remember { mutableStateOf(false) }

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

    var playAgainDialog by remember { mutableStateOf(false) } // setear

    var lastTrucoCall by remember { mutableStateOf<String?>(null)} // setear
    var trucoDialogCall by remember { mutableStateOf<String?>(null)} // setear

    var myDialogText by remember { mutableStateOf<String?>(null) } // setear
    var opponentDialogText by remember { mutableStateOf<String?>(null) } // setear

    var showEnvidoAnswerOptions by remember { mutableStateOf(false) } // setear

    var wasEnvidoCalled by remember { mutableStateOf(false) } // setear
    var envidoCalls by remember { mutableStateOf<List<String>>(emptyList()) } // setear

    fun isFirstStep(): Boolean {
        return min(myThrownCards.size, opponentThrownCards.size) == 0
    }

    suspend fun analyzeEvents() {
        analyzingEvents = true
        println("Analyzing events: $eventIndex - ${events.size}")
        if (eventIndex >= events.size) {
            analyzingEvents = false
            println("Fin analizando")
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
                lastTrucoCall = null
                trucoDialogCall = null
                wasEnvidoCalled = false
                envidoCalls = emptyList()
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

            is StartGameEvent -> {
                playAgainDialog = false
                myPoints = 0
                opponentPoints = 0
            }

            is ThrowCardGameEvent -> {
                if (event.playerId != userId) {
                    opponentThrownCards += event.card
                    opponentCardsSize--
                }

                myTurn = event.nextPlayerId == userId
            }

            is PlayAgainEvent -> {
            }

            is NoPlayAgainEvent -> {
                navigateTo(Screen.Main, emptyMap())
            }

            is TrucoCallGameEvent -> {
                myTurn = event.caller != userId
                lastTrucoCall = event.call
                if (userId != event.caller){
                    opponentDialogText = when (event.call) {
                        "TRUCO" -> "Truco"
                        "RETRUCO" -> "Quiero retruco"
                        "VALE_CUATRO" -> "Quiero vale cuatro"
                        else -> "Truco"
                    }
                    delay(1500)
                    opponentDialogText = null
                    trucoDialogCall = event.call
                } else {
                    delay(1200)
                    myDialogText = null
                }
            }

            is TrucoAcceptGameEvent -> {
                myTurn = event.nextPlayerId == userId
                if (event.acceptedBy != userId) {
                    opponentDialogText = "Quiero"
                    delay(1000)
                    opponentDialogText = null
                } else {
                    delay(700)
                    myDialogText = null
                }
            }

            is TrucoDeclineGameEvent -> {
                if (event.declinedBy != userId) {
                    opponentDialogText = "No quiero"
                    delay(1000)
                    opponentDialogText = null
                } else {
                    delay(700)
                    myDialogText = null
                }
            }


            is ToDeckGameEvent -> {
                if (event.playerId != userId) {
                    opponentDialogText = "Me voy al mazo"
                    delay(1000)
                    opponentDialogText = null
                } else {
                    delay(700)
                    myDialogText = null
                }
            }

            is EnvidoCallGameEvent -> {
                envidoCalls = event.calls
                myTurn = event.caller != userId
                wasEnvidoCalled = true
                if (event.caller != userId){
                    myTurn = true
                    println("El oponente me canto envido")
                    showEnvidoAnswerOptions = true
                    opponentDialogText = when (event.call) {
                        "ENVIDO" -> "Envido"
                        "REAL_ENVIDO" -> "Real envido"
                        "FALTA_ENVIDO" -> "Falta envido"
                        else -> "Envido"
                    }
                    delay(1500)
                    opponentDialogText = null
                } else {
                    delay(1200)
                    myDialogText = null
                }
            }

            is EnvidoAcceptedGameEvent -> {
                myTurn = event.nextPlayerId == userId
                showEnvidoAnswerOptions = false

                // acepto
                if (event.acceptedBy != userId) {
                    opponentDialogText = "Quiero"
                    delay(1000)
                    opponentDialogText = null
                } else {
                    delay(700)
                    myDialogText = null
                }

                val myCardPoints = event.cardsPoints[userId] ?: 0
                val opponentCardPoints = event.cardsPoints.entries.first { it.key != userId }.value

                if (event.handUserId == userId) {
                    myDialogText = "$myCardPoints"
                    delay(1500)
                    myDialogText = null
                    if (myCardPoints >= opponentCardPoints) {
                        opponentDialogText = "Son buenas"
                        delay(1500)
                        opponentDialogText = null
                    } else {
                        opponentDialogText = "$opponentCardPoints son mejores"
                        delay(1500)
                        opponentDialogText = null
                    }
                } else {
                    opponentDialogText = "$opponentCardPoints"
                    delay(1500)
                    opponentDialogText = null
                    if (opponentCardPoints >= myCardPoints) {
                        myDialogText = "Son buenas"
                        delay(1500)
                        myDialogText = null
                    } else {
                        myDialogText = "$myCardPoints son mejores"
                        delay(1500)
                        myDialogText = null
                    }
                }

                myPoints = event.points[userId] ?: 0
                opponentPoints = event.points.entries.first { it.key != userId }.value
            }

            is EnvidoDeclinedGameEvent -> {
                myTurn = event.nextPlayerId == userId
                showEnvidoAnswerOptions = false
                myPoints = event.points[userId] ?: 0
                opponentPoints = event.points.entries.first { it.key != userId }.value
                if (event.declinedBy != userId) {
                    opponentDialogText = "No quiero"
                    delay(1000)
                    opponentDialogText = null
                } else {
                    delay(700)
                    myDialogText = null
                }
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
                    disableActions = analyzingEvents
                )

                Spacer(modifier = Modifier.weight(1.5f))

                if (winner != null) {
                    EndGameDialog(
                        onDismissRequest = {
                            winner = null
                            playAgainDialog = true
                        },
                        myPoints = myPoints,
                        opponentPoints = opponentPoints,
                        isWinner = winner == userId,
                        gameId = game.id,
                        userId = userId
                    )
                } else if (playAgainDialog) {
                    PlayAgainDialog(
                        onDismissRequest = { playAgainDialog = false },
                        gameId = game.id,
                        userId = userId,
                    )
                }

                trucoDialogCall?.let {
                    TrucoDialog(
                        onDismissRequest = { trucoDialogCall = null },
                        gameId = game.id,
                        userId = userId,
                        call = it,
                        onMyDialogText = { t -> myDialogText = t }
                    )
                }
            }

            PlayGameScreen(
                modifier = Modifier.fillMaxWidth()
                    .absoluteOffset(x = 0.dp, y = 0.dp)
                    .align(Alignment.BottomEnd),
                userId = userId,
                gameId = game.id,
                myTurn = myTurn,
                onMyDialogText = { myDialogText = it },
                showEnvidoAnswerOptions = showEnvidoAnswerOptions,
                trucoCall = lastTrucoCall,
                isFirstStep = isFirstStep(),
                wasEnvidoCalled =  wasEnvidoCalled,
                envidoCalls = envidoCalls,
            )

            opponentDialogText?.let {
                OpponentBubbleDialog(
                    text = it,
                    modifier = Modifier
                        .absoluteOffset(x = 16.dp, y = 32.dp)
                )
            }
            myDialogText?.let {
                MyBubbleDialog(
                    text = it,
                    modifier = Modifier
                        .absoluteOffset(x = (-20).dp, y = (-60).dp)
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    GameScreen(game = Game.default, isPreview = true, navigateTo = { _, _ -> })
}