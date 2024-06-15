package com.utnmobile.quetrucazo.model.events.implementations;


import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import org.json.JSONObject

class TrucoCallGameEvent (
    val caller: UserId,
    val call: String
    ): GameEvent(){
        companion object{
            fun from(json: JSONObject): TrucoCallGameEvent{
                val caller = json.getInt("caller")
                val call = json.getString("call")
                return TrucoCallGameEvent(caller,call)
            }
        }


    }
