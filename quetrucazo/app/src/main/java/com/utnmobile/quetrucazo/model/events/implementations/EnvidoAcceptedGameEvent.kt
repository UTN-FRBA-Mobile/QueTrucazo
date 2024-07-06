package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.toCardsPoints
import com.utnmobile.quetrucazo.model.events.toPoints
import org.json.JSONObject

class EnvidoAcceptedGameEvent (
    val acceptedBy: UserId,
    val nextPlayerId: UserId,
    val points: Map<UserId, Int>,
    val cardsPoints: Map<UserId, Int>,
    val handUserId: UserId,
): GameEvent() {
    companion object{
        fun from(json: JSONObject): EnvidoAcceptedGameEvent{
            val acceptedBy = json.getInt("acceptedBy")
            val nextPlayerId = json.getInt("nextPlayerId")
            val points = json.toPoints()
            val cardsPoints = json.toCardsPoints()
            val handUserId = json.getInt("handUserId")
            return EnvidoAcceptedGameEvent(acceptedBy, nextPlayerId, points, cardsPoints, handUserId)
        }
    }
}