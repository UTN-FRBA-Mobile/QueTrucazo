package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.toCalls
import org.json.JSONObject

class EnvidoGoFirstGameEvent (
    val caller: UserId,
): GameEvent() {
    companion object{
        fun from(json: JSONObject): EnvidoGoFirstGameEvent{
            val caller = json.getInt("caller")
            return EnvidoGoFirstGameEvent(caller)
        }
    }
}