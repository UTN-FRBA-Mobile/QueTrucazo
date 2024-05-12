package com.utnmobile.quetrucazo.model

class Carta(var palo: CartaPalo, var numero: CartaNumero) {

    private var cartaPalo = palo
    private var cartaNumero = numero

    override fun toString(): String {
        return "$numero de $palo"
    }

    fun valorCarta(): Int {
        var valor = 1
        when(this.cartaNumero){
            CartaNumero.UNO -> valor += when(this.cartaPalo){
                                            CartaPalo.BASTO -> 1
                                            CartaPalo.ORO -> 6
                                            CartaPalo.COPA -> 6
                                            CartaPalo.ESPADA -> 0
                                            }
            CartaNumero.DOS -> valor += 5
            CartaNumero.TRES -> valor += 4
            CartaNumero.CUATRO -> valor += 13
            CartaNumero.CINCO -> valor += 12
            CartaNumero.SEIS -> valor += 11
            CartaNumero.SIETE -> valor += when(this.cartaPalo){
                                            CartaPalo.BASTO -> 10
                                            CartaPalo.ESPADA -> 2
                                            CartaPalo.ORO -> 3
                                            CartaPalo.COPA -> 10
                                            }
            CartaNumero.DIEZ -> valor += 9
            CartaNumero.ONCE -> valor += 8
            CartaNumero.DOCE -> valor += 7
        }
        return valor

    }



}