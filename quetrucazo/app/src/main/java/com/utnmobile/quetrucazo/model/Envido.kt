package com.utnmobile.quetrucazo.model

data class Envido (
    val calls: List<String>,
    val firstCaller: UserId?,
    val lastCaller: UserId?,
    val acceptedBy: UserId?,
    val accepted: Boolean?,
    val waitingResponse: Boolean,
    val winner: UserId?,
    val playersPoints: Map<UserId, Int>,
)