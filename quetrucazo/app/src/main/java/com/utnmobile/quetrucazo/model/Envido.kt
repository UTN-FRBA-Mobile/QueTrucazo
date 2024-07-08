package com.utnmobile.quetrucazo.model

import com.utnmobile.quetrucazo.model.events.toUserMap
import org.json.JSONArray
import org.json.JSONObject

data class Envido (
    val calls: List<String>,
    val firstCaller: UserId?,
    val lastCaller: UserId?,
    val acceptedBy: UserId?,
    val accepted: Boolean?,
    val waitingResponse: Boolean,
    val winner: UserId?,
    val playersPoints: Map<UserId, Int>?,
    val cardsPoints: Map<UserId, Int>?,
) {
    companion object {
        val default = Envido(
            calls = emptyList(),
            firstCaller = null,
            lastCaller = null,
            acceptedBy = null,
            accepted = null,
            waitingResponse = false,
            winner = null,
            playersPoints = emptyMap(),
            cardsPoints = emptyMap(),
        )

        fun from(json: JSONObject): Envido {
            println(json)
            return Envido(
                calls = json.getJSONArray("calls").toList(),
                firstCaller = if (json.isNull("firstCaller")) null else json.getInt("firstCaller"),
                lastCaller = if (json.isNull("lastCaller")) null else json.getInt("lastCaller"),
                acceptedBy = if (json.isNull("acceptedBy")) null else json.getInt("acceptedBy"),
                accepted = if (json.isNull("accepted")) null else json.getBoolean("accepted"),
                waitingResponse = json.getBoolean("waitingResponse"),
                winner = if (json.isNull("winner")) null else json.getInt("winner"),
                playersPoints = if (json.isNull("playersPoints")) null else json.toPlayersPoints(),
                cardsPoints = if (json.isNull("cardsPoints")) null else json.toCardsPoints(),
            )
        }
    }
}

private fun JSONArray.toList(): List<String> {
    val list = mutableListOf<String>()
    for (i in 0 until length()) {
        list.add(getString(i))
    }
    return list
}

fun JSONObject.toPlayersPoints(): Map<UserId, Int> {
    return toUserMap("playersPoints") { points, key ->
        points.getInt(key)
    }
}

fun JSONObject.toCardsPoints(): Map<UserId, Int> {
    return toUserMap("cardsPoints") { points, key ->
        points.getInt(key)
    }
}
