package com.utnmobile.quetrucazo.model

import org.json.JSONArray
import org.json.JSONObject

typealias UserId = Int

data class SafeUser(
    val id: UserId,
    val username: String,
) {
    companion object {
        fun from(json: JSONObject): SafeUser {
            return SafeUser(
                id = json.getInt("id"),
                username = json.getString("username"),
            )
        }
    }
}

fun JSONArray.toSafeUsers(): List<SafeUser> {
    val usersList = mutableListOf<SafeUser>()
    for (i in 0 until length()) {
        val user = getJSONObject(i)
        usersList.add(SafeUser.from(user))
    }
    return usersList
}