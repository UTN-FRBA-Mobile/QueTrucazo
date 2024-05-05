package com.utnmobile.quetrucazo.model

class Carta(var palo: CartaPalo, var numero: CartaNumero) {


    override fun toString(): String {
        return "$numero de $palo"
    }



}