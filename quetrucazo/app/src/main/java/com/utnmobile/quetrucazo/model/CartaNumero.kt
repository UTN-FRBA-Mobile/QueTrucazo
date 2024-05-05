package com.utnmobile.quetrucazo.model

enum class CartaNumero(val value: Int) {

    UNO(1),
    DOS(2),
    TRES(3),
    CUATRO(4),
    CINCO(5),
    SEIS(6),
    SIETE(7),
    DIEZ(10),
    ONCE(11),
    DOCE(12);

    fun getPuntosEnvido(): Int {
        return when (this) {
            UNO -> 1
            DOS -> 2
            TRES -> 3
            CUATRO -> 4
            CINCO -> 5
            SEIS -> 6
            SIETE -> 7
            DIEZ -> 0
            ONCE -> 0
            DOCE -> 0
        }
    }

}