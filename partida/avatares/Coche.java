package partida.avatares;

import java.util.*;

import monopoly.Juego;
import monopoly.Texto;
import monopoly.casillas.*;
import partida.*;

public class Coche extends Avatar{
    public Coche(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados){
        super(jugador, lugar, avCreados);
    }

    /**Implementación del movimiento avanzado del coche*/
    @Override
    public void moverEnAvanzado(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        /*
        SOLO PUEDE COMPRAR 1 CASILLA EN ESOS 3 LANZAMIENTOS MAXIMOS
        */
        if (valorTirada > 4) {
            // El jugador puede volver a tirar hasta un total de cuatro veces (lo limita lanzarDados())
            this.moverEnBasico(casillas, valorTirada);
            Juego.consola.imprimir("Has sacado más de 4! Vuelves a tirar!");
        }
        else {
            // Si saca menos de un 4 retrocede ese número de casillas
            this.moverEnBasico(casillas, -valorTirada);
            // No puede lanzar los dados ni este turno ni los 2 siguientes
            jugador.dosTurnosSinTirar();
            Juego.consola.imprimir(Texto.M_DOS_TURNOS_SIN_TIRAR);
        }
    }
}
