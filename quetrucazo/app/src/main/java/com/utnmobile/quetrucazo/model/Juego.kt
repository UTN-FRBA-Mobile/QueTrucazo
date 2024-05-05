package com.utnmobile.quetrucazo.model

class Juego {

    private var puntosJugador1: Int
    private var puntosJugador2: Int
    private var jugador1: Jugador
    private var jugador2: Jugador
    private var rondas: List<Ronda>

    constructor(puntosJugador1: Int, puntosJugador2: Int, jugador1: Jugador, jugador2: Jugador, rondas: List<Ronda>) {
        this.puntosJugador1 = puntosJugador1
        this.puntosJugador2 = puntosJugador2
        this.jugador1 = jugador1
        this.jugador2 = jugador2
        this.rondas = rondas
    }

}