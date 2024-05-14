package com.utnmobile.quetrucazo.model

import org.json.JSONArray
import org.json.JSONObject

data class Game(
    val id: Int,
    val name: String,
) {
    companion object {
        fun from(json: JSONObject): Game {
            println(json.toString())
            return Game(
                id = json.getInt("id"),
                name = json.getString("name"),
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