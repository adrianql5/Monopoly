package monopoly.cartas;

import java.util.ArrayList;

import monopoly.Texto;
import monopoly.Valor;
import monopoly.Juego;

import partida.*;
import partida.avatares.*;

public class CartaCajaComunidad extends Carta {

    public CartaCajaComunidad(String texto, int indice) {
        super(texto, indice);
    }

    /**
     * Función que dada una carta ejecuta las acciones que dice la misma.
     * @param jugadorActual Jugador al que se le tiene que aplicar las acciones de la carta.
     * @return TRUE si el jugador es solvente, FALSE en caso contrario
     */
    @Override
    public boolean evaluarCarta(Jugador jugadorActual) {
        return true;
        /*
        Especial parking = (Especial)this.tablero.getCasilla(20);
        switch(this.getID()) {
            case 1: //Pagar 500.000€ (a la banca)
                if(500000f>jugadorActual.getFortuna()){
                    jugadorActual.setDeuda(500000f);
                    jugadorActual.setDeudaConJugador(this.banca);
                    return false;
                }
                jugadorActual.sumarFortuna(-500000f);
                jugadorActual.sumarGastos(500000);
                jugadorActual.getEstadisticas().sumarPagoDeAlquileres(500000f);
                parking.incrementarBote(500000f);
                return true;
            case 2: //Ir a la cárcel (encarcelado) sin pasar por la Salida (y por tanto sin cobrar)
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), posicion<10 ? 10-posicion : 50-posicion);
                jugadorActual.encarcelar(this.tablero.getCasilla(20));
                break;
            case 3: //Ir a Salida (pos=0=40) y cobrar
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), 40-posicion);
                cobrarSalida(jugadorActual);
                break;
            case 4: //Cobrar 2.000.000€
                jugadorActual.sumarFortuna(2000000);
                break;
            case 5: //Pagar 1.000.000€ (a la banca)
                if(1000000f>jugadorActual.getFortuna()){
                    jugadorActual.setDeuda(500000f);
                    jugadorActual.setDeudaConJugador(this.banca);
                    return false;
                }
                jugadorActual.sumarFortuna(-1000000f);
                jugadorActual.sumarGastos(1000000f);
                jugadorActual.getEstadisticas().sumarPagoDeAlquileres(1000000f);
                parking.incrementarBote(1000000f);
                return true;
            case 6: //Pagar 200.000€ a cada jugador
                float total_a_pagar = 200000 * (this.jugadores.size()-1);

                if(total_a_pagar>jugadorActual.getFortuna()){
                    jugadorActual.setDeuda(total_a_pagar);
                    jugadorActual.setDeudaConJugador(this.banca);
                    return false;
                }
                jugadorActual.restarFortuna(total_a_pagar);
                parking.incrementarBote(500000f);
                //Recorremos el ArrayList de jugadoresda: si no es el jugador actual sumamos 200.000€
                for(Jugador j : this.jugadores) {
                    if(!j.equals(jugadorActual)) {
                        j.sumarFortuna(200000);
                    }
                }
                return true;
        }*/
    }

    /**
     * Función que dada una carta ejecuta las acciones que dice la misma.
     * //@param jugadorActual Jugador al que se le tiene que aplicar las acciones de la carta.
     */
    public void accion() {

        /**switch(this.ID) {
            case 1: //Pagar 500.000€ (a la banca)
                if(500000f>jugadorActual.getFortuna()){
                    jugadorActual.setDeuda(500000f);
                    jugadorActual.setDeudaConJugador(this.banca);
                    //return false;
                }
                jugadorActual.sumarFortuna(-500000f);
                jugadorActual.sumarGastos(500000);
                jugadorActual.getEstadisticas().sumarPagoDeAlquileres(500000f);
                parking.incrementarBote(500000f);
                //return true;
            case 2: //Ir a la cárcel (encarcelado) sin pasar por la Salida (y por tanto sin cobrar)
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), posicion<10 ? 10-posicion : 50-posicion);
                jugadorActual.encarcelar(this.tablero.getCasilla(20));
                break;
            case 3: //Ir a Salida (pos=0=40) y cobrar
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), 40-posicion);
                cobrarSalida(jugadorActual);
                break;
            case 4: //Cobrar 2.000.000€
                jugadorActual.sumarFortuna(2000000);
                break;
            case 5: //Pagar 1.000.000€ (a la banca)
                if(1000000f>jugadorActual.getFortuna()){
                    jugadorActual.setDeuda(500000f);
                    jugadorActual.setDeudaConJugador(this.banca);
                    //return false;
                }
                jugadorActual.sumarFortuna(-1000000f);
                jugadorActual.sumarGastos(1000000f);
                jugadorActual.getEstadisticas().sumarPagoDeAlquileres(1000000f);
                parking.incrementarBote(1000000f);
                //return true;
            case 6: //Pagar 200.000€ a cada jugador
                float total_a_pagar = 200000 * (this.jugadores.size()-1);

                if(total_a_pagar>jugadorActual.getFortuna()){
                    jugadorActual.setDeuda(total_a_pagar);
                    jugadorActual.setDeudaConJugador(this.banca);
                    //return false;
                }
                jugadorActual.restarFortuna(total_a_pagar);
                parking.incrementarBote(500000f);
                //Recorremos el ArrayList de jugadoresda: si no es el jugador actual sumamos 200.000€
                for(Jugador j : this.jugadores) {
                    if(!j.equals(jugadorActual)) {
                        j.sumarFortuna(200000);
                    }
                }
                //return true;
        }*/
    }
}
