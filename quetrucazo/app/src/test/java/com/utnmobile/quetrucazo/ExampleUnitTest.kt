package com.utnmobile.quetrucazo

import com.utnmobile.quetrucazo.model.Baraja
import com.utnmobile.quetrucazo.model.Carta
import com.utnmobile.quetrucazo.model.CartaNumero
import com.utnmobile.quetrucazo.model.CartaPalo
import com.utnmobile.quetrucazo.model.Jugador
import com.utnmobile.quetrucazo.model.Ronda
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun baraja(){
        var mazo = Baraja()
        var c1: MutableList<Carta> = ArrayList()
        var c2: MutableList<Carta> = ArrayList()
        var p1 = Jugador("Juan", c1)
        var p2 = Jugador("Pepe", c2)
        mazo.repartirCartas(p1,p2)
        var carta1 = p1.cartas.elementAt(0)
        var carta2 = p1.cartas.elementAt(1)
        var carta3 = p1.cartas.elementAt(2)
        var t1 = Pair(carta1.palo,carta1.numero)
        var t2 = Pair(carta2.palo,carta2.numero)
        var t3 = Pair(carta3.palo,carta3.numero)
        assertTrue(t1 != t2)
        assertTrue(t1 != t3)
        assertTrue(t2 != t3)
    }

    @Test
    fun repetirBaraja(){
        for (i in 1..1000){
            baraja()
        }
    }

    @Test
    fun ganadoraEntreDosCartas(){
        var ronda = Ronda(1,true,true, emptyList(), emptyList())

        ronda.cartaGanadora(Carta(CartaPalo.COPA,CartaNumero.UNO), Carta(CartaPalo.ORO,CartaNumero.UNO))

    }
}