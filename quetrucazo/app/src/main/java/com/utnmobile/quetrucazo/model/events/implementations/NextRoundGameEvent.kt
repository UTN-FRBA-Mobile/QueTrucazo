package com.utnmobile.quetrucazo.model.events.implementations

import com.utnmobile.quetrucazo.model.Card
import com.utnmobile.quetrucazo.model.UserId
import com.utnmobile.quetrucazo.model.events.GameEvent
import com.utnmobile.quetrucazo.model.events.toUserMapList
import com.utnmobile.quetrucazo.model.toCards
import org.json.JSONObject

class NextRoundGameEvent(
    val round: Int,
    val cards: Map<UserId, List<Card>>,
    val nextPlayerId: UserId
): GameEvent() {
    companion object {
        fun from(json: JSONObject): NextRoundGameEvent {
            val round = json.getInt("round")
            val cards: Map<UserId, List<Card>> = json.toUserMapList("cards") {
                it.toCards()
            }
            val nextPlayerId = json.getInt("nextPlayerId")
            return NextRoundGameEvent(round, cards, nextPlayerId)
        }
    }
}