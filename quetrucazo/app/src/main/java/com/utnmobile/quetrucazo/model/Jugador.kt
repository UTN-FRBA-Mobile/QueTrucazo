package com.utnmobile.quetrucazo.model

class Jugador {

    private var nombre: String
    private var cartas: List<Carta>

    constructor(nombre: String, cartas: List<Carta>) {
        this.nombre = nombre
        this.cartas = cartas
    }

}