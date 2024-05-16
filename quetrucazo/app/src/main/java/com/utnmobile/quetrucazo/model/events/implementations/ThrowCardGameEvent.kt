package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.Card
import com.utnmobile.quetrucazo.model.Step
import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import org.json.JSONObject

class ThrowCardGameEvent(
    val playerId: UserId,
    val card: Card,
    val step: Step,
): GameEvent() {
    companion object {
        fun from(json: JSONObject): ThrowCardGameEvent {
            val playerId = json.getInt("playerId")
            val card = Card.from(json.getString("card"))
            val step = Step.from(json.getInt("step"))
            return ThrowCardGameEvent(playerId, card, step)
        }
    }
}