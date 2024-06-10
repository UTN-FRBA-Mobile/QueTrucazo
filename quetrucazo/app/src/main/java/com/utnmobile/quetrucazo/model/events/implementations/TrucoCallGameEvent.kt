package com.utnmobile.quetrucazo.model.events.implementations;


import com.utnmobile.quetrucazo.model.TrucoJuego
import com.utnmobile.quetrucazo.model.TrucoJuegoPunto
import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.toPoints
import org.json.JSONObject

class TrucoCallGameEvent (
    val caller: UserId,
    val call: String
    ): GameEvent(){
        companion object{
            fun from(json: JSONObject): TrucoCallGameEvent{
                val caller = json.getInt("playerId")
                val call = json.getString("call")
                return TrucoCallGameEvent(caller,call)
            }
        }


    }
