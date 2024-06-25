package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.toPoints
import org.json.JSONObject

class EnvidoAcceptedGameEvent (
    val acceptedBy: UserId,
    val nextPlayerId: UserId,
    val points: Map<UserId, Int>
): GameEvent() {
    companion object{
        fun from(json: JSONObject): EnvidoAcceptedGameEvent{
            val acceptedBy = json.getInt("acceptedBy")
            val nextPlayerId = json.getInt("nextPlayerId")
            val points = json.toPoints()
            return EnvidoAcceptedGameEvent(acceptedBy, nextPlayerId, points)
        }
    }
}