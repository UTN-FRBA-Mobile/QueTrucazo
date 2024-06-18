package com.utnmobile.quetrucazo.services

import com.utnmobile.quetrucazo.model.Card
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketIOManager {

    private var _socket: Socket? = null

    val socket: Socket?
        get() = _socket

    fun connect(userId: Int, onConnect: () -> Unit, onDisconnect: () -> Unit) {
        try {
            // Configurar y conectar el socket
            _socket = IO.socket(baseUrl)
            _socket?.connect()

            // Escuchar eventos de conexión
            _socket?.on(Socket.EVENT_CONNECT) {
                // Una vez conectado, registrar el usuario
                onConnect()
                val data = JSONObject()
                data.put("userId", userId)
                _socket?.emit("register-connection", data)
            }

            // Manejo de errores
            _socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                println("Connection error: ${args[0]}")
            }

            // on disconnect
            _socket?.on(Socket.EVENT_DISCONNECT) {
                onDisconnect()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createGame(userId: Int) {
        val data = JSONObject()
        data.put("userId", userId)
        _socket?.emit("create-game", data)
    }

    fun joinGame(userId: Int, gameId: Int) {
        val data = JSONObject()
        data.put("userId", userId)
        data.put("gameId", gameId)
        _socket?.emit("join-game", data)
    }

    fun cancelGame(userId: Int, gameId: Int) {
        val data = JSONObject()
        data.put("userId", userId)
        data.put("gameId", gameId)
        _socket?.emit("cancel-game", data)
    }

    fun getGames() {
        _socket?.emit("games-list")
    }

    fun throwCard(userId: Int, gameId: Int, card: Card) {
        val data = JSONObject()
        data.put("userId", userId)
        data.put("gameId", gameId)
        data.put("card", card.shortCutName())
        _socket?.emit("throw-card", data)

    }

    fun disconnectSocket() {
        _socket?.disconnect()
        _socket?.off(Socket.EVENT_CONNECT)
        _socket?.off("games-list")
        _socket?.off("throw-card")
        _socket?.off(Socket.EVENT_CONNECT_ERROR)
    }

    fun playAgain(userId: Int, gameId: Int) {
        val data = JSONObject()
        data.put("userId", userId)
        data.put("gameId", gameId)
        _socket?.emit("play-again", data)
    }

    fun noPlayAgain(userId: Int, gameId: Int) {
        val data = JSONObject()
        data.put("userId", userId)
        data.put("gameId", gameId)
        _socket?.emit("no-play-again", data)
    }

    fun trucoCall(userId: Int, gameId: Int, call: String){
        val data = JSONObject()
        data.put("userId",userId)
        data.put("gameId", gameId)
        data.put("call", call)
        _socket?.emit("truco", data)
    }

}