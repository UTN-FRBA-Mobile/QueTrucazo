package com.utnmobile.quetrucazo.model.events

import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.implementations.NextRoundGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.NoPlayAgainEvent
import com.utnmobile.quetrucazo.model.events.implementations.PlayAgainEvent
import com.utnmobile.quetrucazo.model.events.implementations.ResultGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.RoundResultGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.StartGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.ThrowCardGameEvent
import com.utnmobile.quetrucazo.model.events.implementations.ToDeckGameEvent
import org.json.JSONArray
import org.json.JSONObject

open class GameEvent {
    companion object {
        fun from(json: JSONObject): GameEvent {
            return when (json.getString("type")) {
                "START" -> StartGameEvent.from(json)
                "NEXT_ROUND" -> NextRoundGameEvent.from(json)
                "THROW_CARD" -> ThrowCardGameEvent.from(json)
                "ROUND_RESULT" -> RoundResultGameEvent.from(json)
                "RESULT" -> ResultGameEvent.from(json)
                "TO_DECK" -> ToDeckGameEvent.from(json)
                "PLAY_AGAIN" -> PlayAgainEvent.from(json)
                "NO_PLAY_AGAIN" -> NoPlayAgainEvent.from(json)
                else -> throw IllegalArgumentException("Invalid event type ${json.getString("type")}")
            }
        }
    }
}

fun <T> JSONObject.toUserMap(key: String, f: (JSONObject, String) -> T): Map<UserId, T> {
    val map = mutableMapOf<UserId, T>()
    val json = getJSONObject(key)

    json.keys().forEach { k ->
        val value = f(json, k.toString())
        map[k.toString().toInt()] = value
    }

    return map
}

fun JSONObject.toPoints(): Map<UserId, Int> {
    return toUserMap("points") { points, key ->
        points.getInt(key)
    }
}

fun <T> JSONObject.toUserMapList(key: String, f: (JSONArray) -> List<T>): Map<UserId, List<T>> {
    val map = mutableMapOf<UserId, List<T>>()
    val json = getJSONObject(key)

    json.keys().forEach { k ->
        val value = f(json.getJSONArray(k.toString()))
        map[k.toString().toInt()] = value
    }

    return map
}

fun JSONArray.toGameEvents(): List<GameEvent> {
    return (0 until this.length()).map { this.getJSONObject(it) }.map { GameEvent.from(it) }
}