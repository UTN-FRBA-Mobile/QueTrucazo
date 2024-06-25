package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import org.json.JSONObject

class EnvidoDeclinedGameEvent (
    val declinedBy: UserId,
    val points: Int
): GameEvent() {
    companion object{
        fun from(json: JSONObject): EnvidoDeclinedGameEvent{
            val declinedBy = json.getInt("declinedBy")
            val points = json.getInt("points")
            return EnvidoDeclinedGameEvent(declinedBy, points)
        }
    }
}