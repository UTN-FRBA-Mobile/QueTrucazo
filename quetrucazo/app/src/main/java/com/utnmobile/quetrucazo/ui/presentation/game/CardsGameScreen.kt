package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.utnmobile.quetrucazo.R
import com.utnmobile.quetrucazo.model.Card
import com.utnmobile.quetrucazo.model.CartaPalo
import com.utnmobile.quetrucazo.services.SocketIOManager

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
                .weight(2f),
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
            DisplayMyCards(myCards, removeMyCard, addMyThrownCard, myTurn, gameId, userId)
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
    userId: Int
) {

    myCards.forEach { card ->
        Image(
            painter = painterResource(id = card.resource()),
            contentDescription = "Card Image",
            modifier = Modifier.clickable {
                if (myTurn) {
                    SocketIOManager.throwCard(userId, gameId, card)
                    removeMyCard(card)
                    addMyThrownCard(card)
                }
            }
        )
    }

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