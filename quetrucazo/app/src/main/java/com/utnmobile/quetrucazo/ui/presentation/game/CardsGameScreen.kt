package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.utnmobile.quetrucazo.R
import com.utnmobile.quetrucazo.model.Card
import com.utnmobile.quetrucazo.model.CartaPalo
import com.utnmobile.quetrucazo.services.SocketIOManager
import kotlin.math.roundToInt

@Composable
fun CardsGameScreen(
    modifier: Modifier = Modifier,
    myCards: List<Card>,
    opponentCardsSize: Int,
    myThrownCards: List<Card>,
    opponentThrownCards: List<Card>,
    removeMyCard: (Card) -> Unit,
    addMyThrownCard: (Card) -> Unit,
    myTurn: Boolean,
    gameId: Int,
    userId: Int
) {

    var inGameCardsRowBounds by remember { mutableStateOf<Rect?>(null) }

    LaunchedEffect(inGameCardsRowBounds) {
        println("cambio de bounds $inGameCardsRowBounds")
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DisplayOpponentCards(opponentCardsSize)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .onGloballyPositioned { coordinates ->
                    inGameCardsRowBounds = coordinates.boundsInWindow()
                },
            horizontalArrangement = Arrangement.Absolute.Left,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DisplayInGameCards(myThrownCards, opponentThrownCards)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DisplayMyCards(
                if (inGameCardsRowBounds != null) myCards else emptyList(),
                removeMyCard,
                addMyThrownCard,
                myTurn,
                gameId,
                userId,
                inGameCardsRowBounds
            )
        }

    }
}


@Composable
fun DisplayOpponentCards(opponentCardsSize: Int) {
    repeat(opponentCardsSize) {
        val imageResource = R.drawable.reverso
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Card Image"
        )
    }
}

@Composable
fun DisplayInGameCards(misCartasJugadas: List<Card>, oponenteCartasJugadas: List<Card>) {

    val maxCards = maxOf(misCartasJugadas.size, oponenteCartasJugadas.size)

    repeat(maxCards) {

        val myCard = misCartasJugadas.getOrNull(it)
        val opponentCard = oponenteCartasJugadas.getOrNull(it)

        Box(modifier = Modifier.padding(15.dp)) {
            if (myCard != null && opponentCard != null) {
                DisplayCardsPair(myCard, opponentCard)
            } else {
                DisplayCard(myCard, offset = true)
                DisplayCard(opponentCard)
            }
        }


    }

}

@Composable
fun DisplayCard(card: Card?, offset: Boolean = false) {
    card?.let {
        Image(
            painter = painterResource(id = it.resource()),
            contentDescription = "Card Image",
            modifier = if (offset) Modifier.offset(y = 40.dp) else Modifier
        )
    }
}

@Composable
fun DisplayCardsPair(myCard: Card, opponentCard: Card) {
    val isMyCardWinning = myCard.value() > opponentCard.value()
    if (isMyCardWinning) {
        DisplayCard(opponentCard)
        DisplayCard(myCard, offset = true)
    } else {
        DisplayCard(myCard, offset = true)
        DisplayCard(opponentCard)
    }
}

@Composable
fun DisplayMyCards(
    myCards: List<Card>,
    removeMyCard: (Card) -> Unit,
    addMyThrownCard: (Card) -> Unit,
    myTurn: Boolean,
    gameId: Int,
    userId: Int,
    inGameCardsRowBounds: Rect?
) {
    println("sape $inGameCardsRowBounds")
    myCards.forEach { card ->
        println("haciendo devuelta el foreach con $inGameCardsRowBounds")
        val offset = remember { mutableStateOf(Offset.Zero) }
        val cardCoordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }
        val imageModifier = Modifier
            .offset { IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt()) }
            .pointerInput(myTurn, offset, cardCoordinates, userId, gameId, card) {
                detectDragGestures(
                    onDragStart = {

                    },
                    onDragEnd = {
                        val cardPosition = cardCoordinates.value?.boundsInWindow()?.center
                        println("es mi turno2: $myTurn")
                        println("cardPosition $cardPosition")
                        println("inGameCardsRowBounds $inGameCardsRowBounds")
                        println("esta en posicion: ${isCardInCenter(cardPosition, inGameCardsRowBounds)}")
                        if (myTurn && isCardInCenter(cardPosition, inGameCardsRowBounds)) {
                            SocketIOManager.throwCard(userId, gameId, card)
                            removeMyCard(card)
                            addMyThrownCard(card)
                            offset.value = Offset.Zero
                        } else {
                            offset.value = Offset.Zero
                        }
                    },
                    onDragCancel = {
                    },
                    onDrag = { change, dragAmount ->
                        if (change.pressed) {
                            offset.value += dragAmount
                        }
                        change.consume()
                    }
                )
            }
            .onGloballyPositioned { coordinates ->
                cardCoordinates.value = coordinates
            }

        Image(
            painter = painterResource(id = card.resource()),
            contentDescription = "Card Image",
            modifier = imageModifier
        )
    }
}

fun isCardInCenter(offset: Offset?, rowBounds: Rect?): Boolean {
    return offset?.let { rowBounds?.contains(it) } == true
}

@Preview
@Composable
fun CardsPreview() {
    CardsGameScreen(

        opponentCardsSize = 3,
        myCards = listOf(
            Card(CartaPalo.BASTO, 1),
            Card(CartaPalo.ESPADA, 5),
            Card(CartaPalo.COPA, 4)
        ),

        myThrownCards = listOf(
            Card(CartaPalo.BASTO, 1),
            Card(CartaPalo.ESPADA, 5),
            Card(CartaPalo.COPA, 4)
        ),
        opponentThrownCards = listOf(
            Card(CartaPalo.BASTO, 7),
            Card(CartaPalo.ESPADA, 7),
            Card(CartaPalo.COPA, 4)
        ),
        removeMyCard = {},
        addMyThrownCard = {},
        myTurn = true,
        gameId = 1,
        userId = 1
    )
}