package com.utnmobile.quetrucazo.model

import org.json.JSONObject

data class Truco (
    val lastCall: String?,
    val firstCaller: UserId?,
    val lastCaller: UserId?,
    val playerTurnAfterResponse: UserId?,
    val acceptedBy: UserId?,
    val accepted: Boolean?,
    val waitingResponse: Boolean,
    val points: Int,
) {
    companion object {
        val default = Truco(
            lastCall = null,
            firstCaller = null,
            lastCaller = null,
            playerTurnAfterResponse = null,
            acceptedBy = null,
            accepted = null,
            waitingResponse = false,
            points = 0,
        )

        fun from(json: JSONObject): Truco {
            return Truco(
                lastCall = if (json.isNull("lastCall")) null else json.getString("lastCall"),
                firstCaller = if (json.isNull("firstCaller")) null else json.getInt("firstCaller"),
                lastCaller = if (json.isNull("lastCaller")) null else json.getInt("lastCaller"),
                playerTurnAfterResponse = if (json.isNull("playerTurnAfterResponse")) null else json.getInt("playerTurnAfterResponse"),
                acceptedBy = if (json.isNull("acceptedBy")) null else json.getInt("acceptedBy"),
                accepted = if (json.isNull("accepted")) null else json.getBoolean("accepted"),
                waitingResponse = json.getBoolean("waitingResponse"),
                points = json.getInt("points"),
            )
        }
    }
}