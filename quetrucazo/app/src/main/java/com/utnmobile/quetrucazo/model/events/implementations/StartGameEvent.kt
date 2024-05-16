package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.events.GameEvent
import org.json.JSONObject

class StartGameEvent: GameEvent() {
    companion object {
        fun from(json: JSONObject): StartGameEvent {
            return StartGameEvent()
        }
    }
}