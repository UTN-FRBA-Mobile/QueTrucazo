package com.utnmobile.quetrucazo.model.events.implementations;


import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.toPoints
import org.json.JSONObject

class TrucoGameEvent (
    val playerId: UserId,
    val points: Map<UserId,Int>,
    val waitingResponse: Boolean
    ): GameEvent(){
        companion object{
            fun from(json: JSONObject): TrucoGameEvent{
                val playerId = json.getInt("playerId")
                val points: Map<UserId,Int> = json.toPoints()
                val waitingResponse = json.getBoolean("waitingResponse")
                return TrucoGameEvent(playerId,points,waitingResponse)
            }
        }


    }
