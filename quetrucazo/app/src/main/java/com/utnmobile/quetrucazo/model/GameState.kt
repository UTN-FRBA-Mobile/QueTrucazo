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
            points = emptyMap()
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
            )
        }
    }
}