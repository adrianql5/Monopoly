package partida.avatares;

import java.util.*;

import monopoly.Juego;
import monopoly.casillas.*;
import partida.*;

public class Sombrero extends Avatar{
    public Sombrero(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados){
        super(jugador, lugar, avCreados);
    }

    /**Implementación del movimiento avanzado del sombrero (no se hace)*/
    @Override
    public void moverEnAvanzado(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        Juego.consola.imprimir("Modo avanzado del sombrero no implementado. Movimiento normal.");
        this.moverEnBasico(casillas, valorTirada);
    }
}
