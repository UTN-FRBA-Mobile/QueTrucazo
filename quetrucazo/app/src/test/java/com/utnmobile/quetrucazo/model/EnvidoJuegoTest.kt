package com.utnmobile.quetrucazo.model

import org.junit.Assert.assertEquals
import org.junit.Test

class EnvidoJuegoTest {

    private val envidoJuego = EnvidoJuego()

    @Test
    fun calcularEnvido_retorna33_3_palos_iguales() {

        val carta1 = Carta(CartaPalo.ESPADA, CartaNumero.SIETE)
        val carta2 = Carta(CartaPalo.ESPADA, CartaNumero.SEIS)
        val carta3 = Carta(CartaPalo.ESPADA, CartaNumero.CINCO)
        val puntos = envidoJuego.calcularEnvido(carta1, carta2, carta3)
        assertEquals(33, puntos)

    }

    @Test
    fun calcularEnvido_retorna33_2_palos_iguales_y_uno_distinto() {

        val carta1 = Carta(CartaPalo.ESPADA, CartaNumero.SIETE)
        val carta2 = Carta(CartaPalo.ESPADA, CartaNumero.SEIS)
        val carta3 = Carta(CartaPalo.BASTO, CartaNumero.CINCO)
        val puntos = envidoJuego.calcularEnvido(carta1, carta2, carta3)
        assertEquals(33, puntos)

    }

    @Test
    fun calcularEnvido_retorna32_2_palos_iguales_y_uno_distinto() {

        val carta1 = Carta(CartaPalo.BASTO, CartaNumero.SIETE)
        val carta2 = Carta(CartaPalo.ESPADA, CartaNumero.SEIS)
        val carta3 = Carta(CartaPalo.BASTO, CartaNumero.CINCO)
        val puntos = envidoJuego.calcularEnvido(carta1, carta2, carta3)
        assertEquals(32, puntos)

    }

    @Test
    fun calcularEnvido_retorna22_2_palos_iguales_y_uno_distinto() {

        val carta1 = Carta(CartaPalo.BASTO, CartaNumero.SIETE)
        val carta2 = Carta(CartaPalo.ESPADA, CartaNumero.DOS)
        val carta3 = Carta(CartaPalo.ESPADA, CartaNumero.DIEZ)
        val puntos = envidoJuego.calcularEnvido(carta1, carta2, carta3)
        assertEquals(22, puntos)

    }

    @Test
    fun calcularEnvido_retorna7_3_palos_distintos() {
        val carta1 = Carta(CartaPalo.ESPADA, CartaNumero.SIETE)
        val carta2 = Carta(CartaPalo.ORO, CartaNumero.SEIS)
        val carta3 = Carta(CartaPalo.COPA, CartaNumero.CINCO)
        val puntos = envidoJuego.calcularEnvido(carta1, carta2, carta3)
        assertEquals(7, puntos)
    }

    @Test
    fun calcularEnvido_retorna2_3_palos_distintos() {
        val carta1 = Carta(CartaPalo.ESPADA, CartaNumero.DIEZ)
        val carta2 = Carta(CartaPalo.ORO, CartaNumero.DOS)
        val carta3 = Carta(CartaPalo.COPA, CartaNumero.DOCE)
        val puntos = envidoJuego.calcularEnvido(carta1, carta2, carta3)
        assertEquals(2, puntos)
    }

    @Test
    fun calcularEnvido_retorna0_3_palos_distintos() {
        val carta1 = Carta(CartaPalo.ESPADA, CartaNumero.DIEZ)
        val carta2 = Carta(CartaPalo.ORO, CartaNumero.DIEZ)
        val carta3 = Carta(CartaPalo.COPA, CartaNumero.DOCE)
        val puntos = envidoJuego.calcularEnvido(carta1, carta2, carta3)
        assertEquals(0, puntos)
    }

    @Test
    fun calcularEnvido_retorna20_3_cartas_figuras_mismo_palo() {
        val carta1 = Carta(CartaPalo.ESPADA, CartaNumero.DIEZ)
        val carta2 = Carta(CartaPalo.ESPADA, CartaNumero.ONCE)
        val carta3 = Carta(CartaPalo.ESPADA, CartaNumero.DOCE)
        val puntos = envidoJuego.calcularEnvido(carta1, carta2, carta3)
        assertEquals(20, puntos)
    }

    @Test
    fun calcularPuntos_retorna0_NoSeCantoNingunTipoDeEnvido() {
        val puntos = envidoJuego.calcularPuntos(emptyList(), true, 0, 0)
        assertEquals(0, puntos)
    }

    @Test
    fun calcularPuntos_retorna1_EnvidoNoQuerido() {
        val puntos = envidoJuego.calcularPuntos(listOf(EnvidoJuegoPunto.ENVIDO), false, 0, 0)
        assertEquals(1, puntos)
    }

    @Test
    fun calcularPuntos_retorna2_EnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(listOf(EnvidoJuegoPunto.ENVIDO), true, 0, 0)
        assertEquals(2, puntos)
    }


    @Test
    fun calcularPuntos_retorna1_RealEnvidoNoQuerido() {
        val puntos = envidoJuego.calcularPuntos(listOf(EnvidoJuegoPunto.REAL_ENVIDO), false, 0, 0)
        assertEquals(1, puntos)
    }

    @Test
    fun calcularPuntos_retorna3_RealEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(listOf(EnvidoJuegoPunto.REAL_ENVIDO), true, 0, 0)
        assertEquals(3, puntos)
    }

    @Test
    fun calcularPuntos_retorna2_EnvidoEnvidoNoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(EnvidoJuegoPunto.ENVIDO, EnvidoJuegoPunto.ENVIDO),
            false,
            0,
            0
        )
        assertEquals(2, puntos)
    }

    @Test
    fun calcularPuntos_retorna4_EnvidoEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(EnvidoJuegoPunto.ENVIDO, EnvidoJuegoPunto.ENVIDO),
            true,
            0,
            0
        )
        assertEquals(4, puntos)
    }

    @Test
    fun calcularPuntos_retorna2_EnvidoRealEnvidoNoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.REAL_ENVIDO
            ), false, 0, 0
        )
        assertEquals(2, puntos)
    }

    @Test
    fun calcularPuntos_retorna5_EnvidoRealEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.REAL_ENVIDO
            ), true, 0, 0
        )
        assertEquals(5, puntos)
    }

    @Test
    fun calcularPuntos_retorna4_EnvidoEnvidoRealEnvidoNoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.REAL_ENVIDO
            ), false, 0, 0
        )
        assertEquals(4, puntos)
    }

    @Test
    fun calcularPuntos_retorna4_EnvidoEnvidoRealEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.REAL_ENVIDO
            ), true, 0, 0
        )
        assertEquals(7, puntos)
    }

    @Test
    fun calcularPuntos_retorna1_FaltaEnvidoNoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), false, 0, 0
        )
        assertEquals(1, puntos)
    }

    @Test
    fun calcularPuntos_retorna2_EnvidoFaltaEnvidoNoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), false, 0, 0
        )
        assertEquals(2, puntos)
    }

    @Test
    fun calcularPuntos_retorna3_RealEnvidoFaltaEnvidoNoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.REAL_ENVIDO,
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), false, 0, 0
        )
        assertEquals(3, puntos)
    }

    @Test
    fun calcularPuntos_retorna5_EnvidoRealEnvidoFaltaEnvidoNoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.REAL_ENVIDO,
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), false, 0, 0
        )
        assertEquals(5, puntos)
    }

    @Test
    fun calcularPuntos_retorna7_EnvidoEnvidoRealEnvidoFaltaEnvidoNoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.REAL_ENVIDO,
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), false, 0, 0
        )
        assertEquals(7, puntos)
    }

    @Test
    fun calcularPuntos_retorna30_FaltaEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), true, 14, 14
        )
        assertEquals(30, puntos)
    }

    @Test
    fun calcularPuntos_retorna30_EnvidoFaltaEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), true, 5, 0
        )
        assertEquals(30, puntos)
    }

    @Test
    fun calcularPuntos_retorna30_RealEnvidoFaltaEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.REAL_ENVIDO,
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), true, 5, 0
        )
        assertEquals(30, puntos)
    }

    @Test
    fun calcularPuntos_retorna30_EnvidoRealEnvidoFaltaEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.REAL_ENVIDO,
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), true, 5, 0
        )
        assertEquals(30, puntos)
    }

    @Test
    fun calcularPuntos_retorna30_EnvidoEnvidoRealEnvidoFaltaEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.REAL_ENVIDO,
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), true, 5, 0
        )
        assertEquals(30, puntos)
    }

    @Test
    fun calcularPuntos_retorna10_FaltaEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), true, 20, 10
        )
        assertEquals(10, puntos)
    }

    @Test
    fun calcularPuntos_retorna5_EnvidoFaltaEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), true, 25, 20
        )
        assertEquals(5, puntos)
    }

    @Test
    fun calcularPuntos_retorna1_EnvidoRealEnvidoFaltaEnvidoQuerido() {
        val puntos = envidoJuego.calcularPuntos(
            listOf(
                EnvidoJuegoPunto.ENVIDO,
                EnvidoJuegoPunto.REAL_ENVIDO,
                EnvidoJuegoPunto.FALTA_ENVIDO
            ), true, 15, 15
        )
        assertEquals(15, puntos)
    }



}



