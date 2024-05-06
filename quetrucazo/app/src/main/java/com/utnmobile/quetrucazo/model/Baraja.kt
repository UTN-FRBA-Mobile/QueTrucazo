package com.utnmobile.quetrucazo.model

class Baraja {

    fun cartaAlAzar(): Carta {
        val palos = CartaPalo.entries
        val cualquierPalo = palos.random()

        val numeros = CartaNumero.entries
        val cualquierNumero = numeros.random()
        return Carta(cualquierPalo, cualquierNumero)
    }

    fun repartirCartas(p1: Jugador,p2:Jugador){
        var i = 0
        var cartasRonda: ArrayList<Carta> = ArrayList()
        while(i<6){
            var cartaActual = this.cartaAlAzar()
            if (cartasRonda.filter {
                                    cartaAnterior: Carta ->
                                    (cartaAnterior.palo == cartaActual.palo) and
                                    (cartaAnterior.numero == cartaActual.numero)}
                                    .isEmpty()){
                cartasRonda.add(cartaActual)
                i++
            }
        }
        p1.cartas = cartasRonda.subList(0,3)
        p2.cartas = cartasRonda.subList(3,6)
    }


    }


