package com.utnmobile.quetrucazo.services

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketIOManager {

    private var _socket: Socket? = null

    val socket: Socket?
        get() = _socket

    fun connect(userId: Int) {
        try {
            // Configurar y conectar el socket
            _socket = IO.socket(baseUrl)
            _socket?.connect()

            // Escuchar eventos de conexión
            _socket?.on(Socket.EVENT_CONNECT) {
                // Una vez conectado, registrar el usuario
                val data = JSONObject()
                data.put("userId", userId)
                _socket?.emit("register-connection", data)
            }

            // Manejo de errores
            _socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                println("Connection error: ${args[0]}")
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

    fun getGames() {
        _socket?.emit("games-list")
    }

    fun disconnectSocket() {
        _socket?.disconnect()
        _socket?.off(Socket.EVENT_CONNECT)
        _socket?.off("games-list")
        _socket?.off(Socket.EVENT_CONNECT_ERROR)
    }
}