package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.toPoints
import org.json.JSONObject

class EnvidoDeclinedGameEvent (
    val declinedBy: UserId,
    val nextPlayerId: UserId,
    val points: Map<UserId, Int>): GameEvent() {
    companion object{
        fun from(json: JSONObject): EnvidoDeclinedGameEvent{
            val declinedBy = json.getInt("declinedBy")
            val nextPlayerId = json.getInt("nextPlayerId")
            val points = json.toPoints()
            return EnvidoDeclinedGameEvent(declinedBy, nextPlayerId, points)
        }
    }
}