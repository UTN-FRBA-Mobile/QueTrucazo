package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import org.json.JSONObject

class TrucoAcceptGameEvent (
        val acceptedBy: UserId,
    val nextPlayerId: UserId,
): GameEvent(){
    companion object{
        fun from(json: JSONObject): TrucoAcceptGameEvent{
            val acceptedBy = json.getInt("acceptedBy")
            val nextPlayerId = json.getInt("nextPlayerId")
            return TrucoAcceptGameEvent(acceptedBy, nextPlayerId)
        }
    }


}
