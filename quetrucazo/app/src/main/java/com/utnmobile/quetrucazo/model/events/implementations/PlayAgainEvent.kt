package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.events.GameEvent
import org.json.JSONObject

class PlayAgainEvent : GameEvent() {
    companion object {
        fun from(json: JSONObject): PlayAgainEvent {
            return PlayAgainEvent()
        }
    }
}