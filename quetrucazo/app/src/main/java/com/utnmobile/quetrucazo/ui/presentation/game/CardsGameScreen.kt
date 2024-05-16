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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.utnmobile.quetrucazo.R
import com.utnmobile.quetrucazo.model.Card
import com.utnmobile.quetrucazo.model.Carta
import com.utnmobile.quetrucazo.model.CartaNumero
import com.utnmobile.quetrucazo.model.CartaPalo
import com.utnmobile.quetrucazo.model.Game

@Composable
fun CardsGameScreen(modifier: Modifier = Modifier, myCards: List<Card>, removeMyCard: (Card) -> Unit) {

    var cardsPlayed by remember {
        mutableStateOf(
            listOf(
                Pair(
                    Card(CartaPalo.BASTO, 2),
                    Card(CartaPalo.ORO, 7)
                )
            )
        )
    }
    var cardsPlayer2 by remember {
        mutableStateOf(
            listOf(
                Card(CartaPalo.BASTO, 2),
                Card(CartaPalo.ORO, 1),
            )
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            cardsPlayer2.forEachIndexed { index, card ->
                val imageResource = R.drawable.reverso
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = "Card Image",
                    modifier = Modifier.clickable {
                        cardsPlayed = cardsPlayed + Pair(card, card)
                        cardsPlayer2 = cardsPlayer2.filterIndexed { i, _ -> i != index }
                    }
                )

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            cardsPlayed.takeLast(3).forEach { pair ->
                Box(modifier = Modifier.padding(end = 30.dp)) { // Agrega espacio a la derecha de cada Box
                    val imageResource1 = pair.first.resource()
                    val imageResource2 = pair.second.resource()
                    Image(
                        painter = painterResource(id = imageResource1),
                        contentDescription = "Card Image"
                    )
                    Image(
                        painter = painterResource(id = imageResource2),
                        contentDescription = "Card Image",
                        modifier = Modifier.offset(
                            x = 30.dp,
                            y = 30.dp
                        ) // Desplaza la segunda carta
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            myCards.forEach { card ->
                Image(
                    painter = painterResource(id = card.resource()),
                    contentDescription = "Card Image",
                    modifier = Modifier.clickable {
                        cardsPlayed = cardsPlayed + Pair(card, card)
                        removeMyCard(card)
                    }
                )
            }
        }

    }
}

@Preview
@Composable
fun CardsPreview() {
    GameScreen(game = Game.default, isPreview = true)
}