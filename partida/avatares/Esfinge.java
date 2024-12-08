package partida.avatares;

import java.util.*;

import monopoly.Juego;
import monopoly.casillas.*;
import partida.*;

public class Esfinge extends Avatar{
    public Esfinge(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados){
        super(jugador, lugar, avCreados);
    }

    /**Implementación del movimiento avanzado de la esfinge (no se hace)*/
    @Override
    public void moverEnAvanzado(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        Juego.consola.imprimir("Modo avanzado de la esfinge no implementado. Movimiento normal.");
        this.moverEnBasico(casillas, valorTirada);
    }
}
