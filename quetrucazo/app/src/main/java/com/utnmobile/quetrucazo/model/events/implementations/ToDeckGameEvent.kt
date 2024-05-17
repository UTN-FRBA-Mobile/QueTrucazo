package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.Card
import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import org.json.JSONObject

class ToDeckGameEvent(
    val playerId: UserId,
): GameEvent() {
    companion object {
        fun from(json: JSONObject): ToDeckGameEvent {
            val playerId = json.getInt("playerId")
            return ToDeckGameEvent(playerId)
        }
    }
}