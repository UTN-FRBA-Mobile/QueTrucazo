package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.rotate
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
import androidx.compose.ui.zIndex
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
    userId: Int,
) {

    var inGameCardsRowBounds by remember { mutableStateOf<Rect?>(null) }

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .onGloballyPositioned { coordinates ->
                    inGameCardsRowBounds = coordinates.boundsInWindow()
                },
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
    val angle = 15f
    val offsetY = -10
    val offsetX = 15

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until opponentCardsSize) {
            val currentAngle = if (i == 0 && opponentCardsSize > 1) angle
            else if (i == 1 && opponentCardsSize == 2 || i == 2 && opponentCardsSize == 3) -angle
            else 0f

            val currentOffsetX = if (i == 0 && opponentCardsSize == 3) offsetX
            else if (i == 2 && opponentCardsSize == 3) -offsetX
            else if (i == 0 && opponentCardsSize == 2) (offsetX * 1.5).toInt()
            else if (i == 1 && opponentCardsSize == 2) -(offsetX * 1.5).toInt()
            else 0

            val currentOffsetY = if (i == 0 && opponentCardsSize > 1) offsetY
            else if (i == 2 && opponentCardsSize == 3) offsetY
            else if (i == 1 && opponentCardsSize == 2) offsetY
            else 0

            Image(
                painter = painterResource(id = R.drawable.reverso),
                contentDescription = "Card Image",
                modifier = Modifier
                    .offset(x = currentOffsetX.dp, y = currentOffsetY.toInt().dp)
                    .rotate(currentAngle)
            )
        }
    }
}

@Composable
fun DisplayInGameCards(
    misCartasJugadas: List<Card>,
    oponenteCartasJugadas: List<Card>,
) {
    Row(
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(oponenteCartasJugadas.size) { index ->
            val opponentCard = oponenteCartasJugadas.get(index)
            Box(modifier = Modifier.padding(15.dp)) {
                AnimatedOpponentCard(opponentCard, index == oponenteCartasJugadas.size - 1,  true)
            }
        }
    }

    Row(
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(misCartasJugadas.size) { index ->
            val myCard = misCartasJugadas.get(index)
            Box(modifier = Modifier.padding(15.dp)) {
                DisplayCard(myCard, true, offset = true)
            }
        }
    }
}

@Composable
fun AnimatedOpponentCard(card: Card, animate: Boolean, top: Boolean) {
    val offsetY = remember { Animatable(if (animate) -200f else 0f) }
    var hasAnimated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!hasAnimated && animate) {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 500)
            )
            hasAnimated = true
        }
    }

    Image(
        painter = painterResource(id = card.resource()),
        contentDescription = "Card Image",
        modifier = Modifier
            .offset { IntOffset(0, offsetY.value.toInt()) }
            .zIndex(if (top) 1f else 0f)
    )
}

@Composable
fun DisplayCard(card: Card?, top: Boolean, offset: Boolean = false) {
    card?.let {
        Image(
            painter = painterResource(id = it.resource()),
            contentDescription = "Card Image",
            modifier = (if (offset) Modifier.offset(y = 40.dp) else Modifier).zIndex(if (top) 1f else 0f)
        )
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
    myCards.forEach { card ->
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
        userId = 1,
    )
}