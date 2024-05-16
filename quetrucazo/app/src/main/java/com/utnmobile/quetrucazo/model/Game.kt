package com.utnmobile.quetrucazo.model

import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.toGameEvents
import org.json.JSONArray
import org.json.JSONObject

enum class Step {
    FIRST, SECOND, THIRD;
    companion object {
        fun from(step: Int): Step {
            return when (step) {
                1 -> FIRST
                2 -> SECOND
                3 -> THIRD
                else -> throw IllegalArgumentException("Invalid step")
            }
        }
    }
}

data class Game(
    val id: Int,
    val name: String,
    val players: List<SafeUser>,
    val events: List<GameEvent>,
) {
    companion object {
        val default = Game(
            id = 0,
            name = "Game default",
            players = emptyList(),
            events = emptyList(),
        )
        fun from(json: JSONObject): Game {
            println(json.toString())
            return Game(
                id = json.getInt("id"),
                name = json.getString("name"),
                players = json.getJSONArray("players").toSafeUsers(),
                events = json.getJSONArray("events").toGameEvents(),
            )
        }
    }
}

fun JSONArray.toGames(): List<Game> {
    val gamesList = mutableListOf<Game>()
    for (i in 0 until length()) {
        val game = getJSONObject(i)
        gamesList.add(Game.from(game))
    }
    return gamesList
}