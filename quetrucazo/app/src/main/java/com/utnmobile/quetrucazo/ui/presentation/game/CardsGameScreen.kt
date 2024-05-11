package com.utnmobile.quetrucazo.ui.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.utnmobile.quetrucazo.model.Carta
import com.utnmobile.quetrucazo.model.CartaNumero
import com.utnmobile.quetrucazo.model.CartaPalo

@Composable
fun CardsGameScreen(modifier: Modifier = Modifier) {

    val cardsPlayer1: List<Carta> = listOf(
        Carta(CartaPalo.ORO, CartaNumero.UNO),
        Carta(CartaPalo.ORO, CartaNumero.SIETE)
    )

    val cardsPlayer2: List<Carta> = listOf(
        Carta(CartaPalo.BASTO, CartaNumero.DOS),
        Carta(CartaPalo.BASTO, CartaNumero.DOS),
        Carta(CartaPalo.ORO, CartaNumero.UNO),
    )

    val cardsPlayed: List<Carta> = listOf(
        Carta(CartaPalo.BASTO, CartaNumero.DOS)
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            cardsPlayer2.forEach { card ->
                val imageResource = cardImages["${card.palo.name}_${card.numero.name}"]
                if (imageResource != null) {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = "Card Image"
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            cardsPlayed.forEach { card ->
                val imageResource = cardImages["${card.palo.name}_${card.numero.name}"]
                if (imageResource != null) {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = "Card Image"
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            cardsPlayer1.forEach { card ->
                val imageResource = cardImages["${card.palo.name}_${card.numero.name}"]
                if (imageResource != null) {
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = "Card Image"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CardsPreview() {
    GameScreen(isPreview = true)
}
