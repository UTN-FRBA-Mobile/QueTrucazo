package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.events.GameEvent
import org.json.JSONObject

class NoPlayAgainEvent : GameEvent() {
    companion object {
        fun from(json: JSONObject): NoPlayAgainEvent {
            return NoPlayAgainEvent()
        }
    }
}