package com.utnmobile.quetrucazo.model

class TrucoJuego {
    private var tipoDeTruco: TrucoJuegoPunto = TrucoJuegoPunto.NINGUNO
    //La funcion no controla que el flujo de tiposEnvido sea correcto. Esto lo controla la vista o el controlador
    //La funcion solo calcula los puntos. no sabe a quien se los suma.
    fun cantarTruco(tipo: TrucoJuegoPunto,querido: Boolean): Int {
        val puntosConcedidos : Int = if (tipoDeTruco != tipo && querido){
            when(tipoDeTruco){
                TrucoJuegoPunto.TRUCO -> 2
                TrucoJuegoPunto.RETRUCO -> 3
                TrucoJuegoPunto.VALE_CUATRO -> 4
                else -> 0
            }
        } else {
            0
        }
        return puntosConcedidos;
    }
}