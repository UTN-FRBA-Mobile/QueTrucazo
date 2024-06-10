package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import org.json.JSONObject

class TrucoDeclineGameEvent (
        val declinedBy: UserId,
): GameEvent(){
    companion object{
        fun from(json: JSONObject): TrucoDeclineGameEvent{
            val declinedBy = json.getInt("declinedBy")
            return TrucoDeclineGameEvent(declinedBy)
        }
    }


}
