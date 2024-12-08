package partida.avatares;

import java.util.*;

import monopoly.Juego;
import monopoly.casillas.*;
import partida.*;

public class Pelota extends Avatar {
    // Atributo extra pa imprimir el mensaje del primer movimiento (5) cuando saca >4 en la tirada
    private boolean primerMovimientoPelota;

    public Pelota(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados){
        super(jugador, lugar, avCreados);
        this.primerMovimientoPelota = false;
    }

    public boolean isPrimerMovimientoPelota() {
        return primerMovimientoPelota;
    }

    public void setPrimerMovimientoPelota(boolean primerMovimientoPelota) {
        this.primerMovimientoPelota = primerMovimientoPelota;
    }

    /**Implementación del movimiento avanzado de la pelota*/
    @Override
    public void moverEnAvanzado(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        if (this.jugador.getMovimientos_pendientes().isEmpty()) {
            // Si no tiene movimientos pendientes tenemos que generarlos en función de los dados
            jugador.calcularMovimientosPendientes(valorTirada);
            this.primerMovimientoPelota = true;
            this.moverEnBasico(casillas, this.jugador.getMovimientos_pendientes().get(0));
        } else {
            // Si se llama a moverYEvaluar() para seguir moviendo se hace el movimiento que toca directamente
            this.moverEnBasico(casillas, valorTirada);
        }

        // Eliminamos el movimiento que acabamos de hacer
        this.jugador.eliminarMovimientoPendiente();
    }
}
