package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.toPoints
import org.json.JSONObject

class RoundResultGameEvent(
    val winner: UserId,
    val points: Map<UserId, Int>
): GameEvent() {
    companion object {
        fun from(json: JSONObject): RoundResultGameEvent {
            val winner = json.getInt("winner")
            val points = json.toPoints()
            return RoundResultGameEvent(winner, points)
        }
    }
}