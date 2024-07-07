package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
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
    disableActions: Boolean
) {

    var inGameCardsRowBounds by remember { mutableStateOf<Rect?>(null) }
    var opponentCardsRowBounds by remember { mutableStateOf<Rect?>(null) }
    var shouldGlow by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onGloballyPositioned { coordinates ->
                    opponentCardsRowBounds = coordinates.boundsInWindow()
                },
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DisplayOpponentCards(opponentCardsSize)
        }


        Box(

            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .clip(RoundedCornerShape(10.dp))
                .background(color = if (shouldGlow) Color(255/255f, 255/255f, 255/255f, 0.3f) else Color.Transparent)
                .onGloballyPositioned { coordinates ->
                    inGameCardsRowBounds = coordinates.boundsInWindow()
                },
        ) {
            DisplayInGameCards(myThrownCards, opponentThrownCards, opponentCardsRowBounds)
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
                inGameCardsRowBounds,
                disableActions,
                setShouldGlow = { value -> shouldGlow = value }
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
                    .offset(x = currentOffsetX.dp, y = currentOffsetY.dp)
                    .rotate(currentAngle)
            )
        }
    }
}

@Composable
fun DisplayInGameCards(
    misCartasJugadas: List<Card>,
    oponenteCartasJugadas: List<Card>,
    opponentCardsRowBounds: Rect?
) {
    Row(
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(oponenteCartasJugadas.size) { index ->
            val opponentCard = oponenteCartasJugadas.get(index)
            Box(modifier = Modifier.padding(15.dp)) {
                AnimatedOpponentCard(
                    opponentCard,
                    index == oponenteCartasJugadas.size - 1,
                    true,
                    opponentCardsRowBounds
                )
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
fun AnimatedOpponentCard(card: Card, animate: Boolean, top: Boolean, inGameCardsRowBounds: Rect?) {
    var componentPosition by remember { mutableStateOf<Offset?>(null) }

    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
    var hasAnimated by remember { mutableStateOf(false) }

    // Función para calcular las posiciones iniciales y finales de la animación
    fun calculateInitialOffset(): Offset {
        val startX = inGameCardsRowBounds?.center?.let {
            it.x - 208 / 2
        } ?: 0f
        val startY = inGameCardsRowBounds?.center?.let {
            it.y - 320 / 2
        } ?: 0f
        val currentX = componentPosition?.x ?: 0f
        val currentY = componentPosition?.y ?: 0f
        return Offset(startX - currentX, startY - currentY)
    }

    LaunchedEffect(card, animate, componentPosition) {
        if (!hasAnimated && animate && componentPosition != null) {
            val initialOffset = calculateInitialOffset()

            offset.snapTo(initialOffset) // Posicionar en el punto inicial calculado

            offset.animateTo(
                targetValue = Offset(0f, 0f),
                animationSpec = tween(durationMillis = 500)
            )
            hasAnimated = true
        }
    }

    Image(
        painter = painterResource(id = card.resource()),
        contentDescription = "Card Image",
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                if (componentPosition == null) {
                    componentPosition = coordinates.positionInRoot()
                }
            }
            .offset { IntOffset(offset.value.x.toInt(), offset.value.y.toInt()) }
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
    inGameCardsRowBounds: Rect?,
    disabledActions: Boolean,
    setShouldGlow: (Boolean) -> Unit
) {
    myCards.forEach { card ->
        val offset = remember { mutableStateOf(Offset.Zero) }
        val cardCoordinates = remember { mutableStateOf<LayoutCoordinates?>(null) }
        val imageModifier = Modifier
            .offset { IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt()) }
            .pointerInput(myTurn, offset, cardCoordinates, userId, gameId, card, disabledActions) {
                detectDragGestures(
                    onDragStart = {
                        setShouldGlow(true)
                    },
                    onDragEnd = {
                        setShouldGlow(false)
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
                        setShouldGlow(false)
                    },
                    onDrag = { change, dragAmount ->
                        if (disabledActions) return@detectDragGestures
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
        disableActions = false
    )
}