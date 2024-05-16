package com.utnmobile.quetrucazo.model

import com.utnmobile.quetrucazo.R
import org.json.JSONArray

class Card(private val palo: CartaPalo, private val number: Int) {
    companion object {
        fun from(json: String): Card {
            val palo = when (json[0]) {
                'O' -> CartaPalo.ORO
                'C' -> CartaPalo.COPA
                'B' -> CartaPalo.BASTO
                'E' -> CartaPalo.ESPADA
                else -> throw IllegalArgumentException("Invalid palo")
            }
            val number = json.substring(1).toInt()
            return Card(palo, number)
        }

    }

    override fun toString(): String {
        return "$number de $palo"
    }

    fun resource(): Int {
        val palo = when (palo) {
            CartaPalo.ORO -> "o"
            CartaPalo.COPA -> "c"
            CartaPalo.BASTO -> "b"
            CartaPalo.ESPADA -> "e"
        }
        return R.drawable::class.java.getField("${palo}$number").getInt(null)
    }

    fun value(): Int {
        return when (number) {
            1 -> when (palo) {
                CartaPalo.ORO -> 7
                CartaPalo.COPA -> 7
                CartaPalo.BASTO -> 12
                CartaPalo.ESPADA -> 13
            }

            2 -> 8
            3 -> 9
            4 -> 0
            5 -> 1
            6 -> 2
            7 -> when (palo) {
                CartaPalo.BASTO -> 3
                CartaPalo.COPA -> 3
                CartaPalo.ESPADA -> 11
                CartaPalo.ORO -> 10
            }

            10 -> 4
            11 -> 5
            12 -> 6
            else -> 0
        }
    }
}

fun JSONArray.toCards(): List<Card> {
    val cardsList = mutableListOf<Card>()
    for (i in 0 until length()) {
        val card = getString(i)
        cardsList.add(Card.from(card))
    }
    return cardsList
}