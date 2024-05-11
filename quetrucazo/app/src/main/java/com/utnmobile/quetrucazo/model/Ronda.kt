package com.utnmobile.quetrucazo.model

class Ronda {

    private var numeroRonda: Int
    private var arrancaJugador1: Boolean
    private var esTurnoJugador1: Boolean
    private var cartasJugadasJugador1: List<Carta>
    private var cartasJugadasJugador2: List<Carta>

    constructor(numeroRonda: Int, arrancaJugador1: Boolean, esTurnoJugador1: Boolean, cartasJugadasJugador1: List<Carta>, cartasJugadasJugador2: List<Carta>) {
        this.numeroRonda = numeroRonda
        this.arrancaJugador1 = arrancaJugador1
        this.esTurnoJugador1 = esTurnoJugador1
        this.cartasJugadasJugador1 = cartasJugadasJugador1
        this.cartasJugadasJugador2 = cartasJugadasJugador2
    }

    fun cartaGanadora(carta1: Carta, carta2: Carta){
        if (carta1.valorCarta() < carta2.valorCarta()){
            println(carta1.toString())
        } else if (carta1.valorCarta() > carta2.valorCarta()){
            println(carta2.toString())
        }   else{
            println("EMPATE")
        }
    }
}