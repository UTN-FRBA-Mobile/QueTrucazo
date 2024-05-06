package com.utnmobile.quetrucazo.model

class Carta(var palo: CartaPalo, var numero: CartaNumero) {

    private var cartaPalo = palo
    private var cartaNumero = numero

    override fun toString(): String {
        return "$numero de $palo"
    }



}