package com.utnmobile.quetrucazo.model

import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.toGameEvents
import org.json.JSONArray
import org.json.JSONObject

data class Game(
    val id: Int,
    val name: String,
    val players: List<SafeUser>,
    val state: GameState,
    val events: List<GameEvent>,
    val finished: Boolean,
    val playAgainResponse: List<UserId>,
    val closed: Boolean,
) {
    companion object {
        val default = Game(
            id = 0,
            name = "Game default",
            players = emptyList(),
            state = GameState.default,
            events = emptyList(),
            finished = false,
            playAgainResponse = emptyList(),
            closed = false,
        )
        fun from(json: JSONObject): Game {
            println(json.toString())
            return Game(
                id = json.getInt("id"),
                name = json.getString("name"),
                state = GameState.from(json.getJSONObject("state")),
                players = json.getJSONArray("players").toSafeUsers(),
                events = json.getJSONArray("events").toGameEvents(),
                finished = json.getBoolean("finished"),
                playAgainResponse = json.getJSONArray("playAgainResponse").toUserIds(),
                closed = json.getBoolean("closed"),
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