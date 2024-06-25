package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import org.json.JSONObject

class EnvidoCallGameEvent (
    val call: String,
    val caller: UserId
): GameEvent() {
    companion object{
        fun from(json: JSONObject): EnvidoCallGameEvent{
            val caller = json.getInt("caller")
            val call = json.getString("call")
            return EnvidoCallGameEvent(call, caller)
        }
    }
}