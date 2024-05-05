package com.utnmobile.quetrucazo.model

class EnvidoJuego {

    fun calcularEnvido(carta1: Carta, carta2: Carta, carta3: Carta): Int {

        val cartas = listOf(carta1, carta2, carta3)
        val grupos = cartas.groupBy { it.palo }

        var maxEnvido = 0
        for ((_, grupo) in grupos) {
            if (grupo.size >= 2) {
                val puntos = grupo.map { it.numero.getPuntosEnvido() }
                    .sortedDescending()
                    .take(2)
                    .sum() + 20
                maxEnvido = maxOf(maxEnvido, puntos)
            }
        }

        if (maxEnvido == 0) {
            maxEnvido = cartas.maxOfOrNull { it.numero.getPuntosEnvido() } ?: 0
        }

        return maxEnvido

    }

    fun calcularPuntos(
        tiposEnvido: List<EnvidoJuegoPunto>,
        querido: Boolean,
        puntosJugador1: Int,
        puntosJugador2: Int
    ): Int {

        //La funcion no controla que el flujo de tiposEnvido sea correcto. Esto lo controla la vista o el controlador
        //La funcion solo calcula los puntos. no sabe a quien se los suma.
        //La lista de tiposEnvido tiene que respetar el orden en que fueron cantados los tipos de envido

        //No se canto ningun envido
        if (tiposEnvido.isEmpty()) return 0

        //No se quiso el primer canto
        if(!querido && tiposEnvido.size == 1) return 1

        //Se quiso la falta envido
        if (querido && tiposEnvido.last() == EnvidoJuegoPunto.FALTA_ENVIDO) {
            if (puntosJugador1 < 15 && puntosJugador2 < 15) {
                return 30
            }
            return 30 - maxOf(puntosJugador1, puntosJugador2)
        }

        //1 canto querido
        //2 cantos o mas queridos o no queridos sin falta envido querida
        val tiposEnvidoModificados = if (!querido) tiposEnvido.dropLast(1) else tiposEnvido
        var puntosSumados = 0
        for (tipoEnvido in tiposEnvidoModificados) {
            puntosSumados += when (tipoEnvido) {
                EnvidoJuegoPunto.ENVIDO -> 2
                EnvidoJuegoPunto.REAL_ENVIDO -> 3
                else -> throw Exception("Tipo de envido no valido")
            }
        }
        return puntosSumados

    }

}