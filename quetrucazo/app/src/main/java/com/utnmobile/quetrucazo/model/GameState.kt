package com.utnmobile.quetrucazo.model

import com.utnmobile.quetrucazo.model.events.toPoints
import com.utnmobile.quetrucazo.model.events.toUserMapList
import org.json.JSONObject

data class GameState(
    val started: Boolean,
    val firstPlayer: UserId,
    val playerTurn: UserId,
    val winner: UserId?,
    val round: Int,
    val cards: Map<UserId, List<Card>>,
    val thrownCards: Map<UserId, List<Card>>,
    val points: Map<UserId, Int>,
    val envido: Envido,
    val truco: Truco,
) {
    companion object {
        val default: GameState = GameState(
            started = false,
            firstPlayer = 0,
            playerTurn = 0,
            winner = null,
            round = 0,
            cards = emptyMap(),
            thrownCards = emptyMap(),
            points = emptyMap(),
            envido = Envido.default,
            truco = Truco.default,
        )

        fun from(json: JSONObject): GameState {
            return GameState(
                started = json.getBoolean("started"),
                firstPlayer = json.getInt("firstPlayer"),
                playerTurn = json.getInt("playerTurn"),
                winner = if (json.isNull("winner")) null else json.getInt("winner"),
                round = json.getInt("round"),
                cards = json.toUserMapList("cards") {
                    it.toCards()
                },
                thrownCards = json.toUserMapList("thrownCards") {
                    it.toCards()
                },
                points = json.toPoints(),
                envido = Envido.from(json.getJSONObject("envido")),
                truco = Truco.from(json.getJSONObject("truco")),
            )
        }
    }

    fun userCards(userId: UserId): List<Card> {
        return cards[userId] ?: emptyList()
    }

    fun opponentCardsSize(myUserId: UserId): Int {
        return cards.entries.firstOrNull { it.key != myUserId }?.value?.size ?: 0
    }

    fun myPoints(userId: UserId): Int {
        return points[userId] ?: 0
    }

    fun opponentPoints(myUserId: UserId): Int {
        return points.entries.firstOrNull { it.key != myUserId }?.value ?: 0
    }

    fun myThrownCards(userId: UserId): List<Card> {
        return thrownCards[userId] ?: emptyList()
    }

    fun opponentThrownCards(myUserId: UserId): List<Card> {
        return thrownCards.entries.firstOrNull { it.key != myUserId }?.value ?: emptyList()
    }
}