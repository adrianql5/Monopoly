package monopoly.cartas;

import java.util.ArrayList;

import monopoly.Texto;
import monopoly.Valor;
import monopoly.Juego;

import partida.*;
import partida.avatares.*;

public class CartaSuerte extends Carta {

    public CartaSuerte(String texto, int indice) {
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
        Avatar avatarActual = jugadorActual.getAvatar();
        int posicion = avatarActual.getLugar().getPosicion();

        switch(this.getID()) {
            case 1: //Ir a Transportes1 (pos=5). Si pasas por la Salida cobrar
                //Siempre se pasa por la salida ya que no hay ninguna casilla Suerte entre la Salida y Trans1
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), 45-posicion);
                cobrarSalida(jugadorActual);
                if(!evaluarCasilla(this.tablero.getCasilla(5))) bucleBancarrota();
                break;
            case 2: //Ir a Solar15 (pos=26) sin pasar por la Salida (y por tanto sin cobrar)
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), posicion<26 ? 26-posicion : 66-posicion);
                if(!evaluarCasilla(this.tablero.getCasilla(26))) bucleBancarrota();
                break;
            case 3: //Cobrar 500.000€
                jugadorActual.sumarFortuna(500000);
                break;
            case 4: //Ir a Solar3 (pos=6). Si pasas por la Salida cobrar
                //Siempre se pasa por la salida ya que no hay ninguna casilla Suerte entre la Salida y Solar3
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), 46-posicion);
                cobrarSalida(jugadorActual);
                if(!evaluarCasilla(this.tablero.getCasilla(6))) bucleBancarrota();
                break;
            case 5: //Ir a la cárcel (encarcelado) sin pasar por la Salida (y por tanto sin cobrar)
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), posicion<10 ? 10-posicion : 50-posicion);
                jugadorActual.encarcelar(this.tablero.getCasilla(20));
                break;
            case 6: //Cobrar 1.000.000€
                jugadorActual.sumarFortuna(1000000);
                break;
        }*/
    }

    /**
     * Función que dada una carta ejecuta las acciones que dice la misma.
     * @param //jugadorActual Jugador al que se le tiene que aplicar las acciones de la carta.
     * @return TRUE si el jugador es solvente, FALSE en caso contrario
     */
    @Override
    public void accion() {

        /*
        Avatar avatarActual = jugadorActual.getAvatar();
        int posicion = avatarActual.getLugar().getPosicion();

        switch(this.getID()) {
            case 1: //Ir a Transportes1 (pos=5). Si pasas por la Salida cobrar
                //Siempre se pasa por la salida ya que no hay ninguna casilla Suerte entre la Salida y Trans1
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), 45-posicion);
                cobrarSalida(jugadorActual);
                if(!evaluarCasilla(this.tablero.getCasilla(5))) bucleBancarrota();
                break;
            case 2: //Ir a Solar15 (pos=26) sin pasar por la Salida (y por tanto sin cobrar)
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), posicion<26 ? 26-posicion : 66-posicion);
                if(!evaluarCasilla(this.tablero.getCasilla(26))) bucleBancarrota();
                break;
            case 3: //Cobrar 500.000€
                jugadorActual.sumarFortuna(500000);
                break;
            case 4: //Ir a Solar3 (pos=6). Si pasas por la Salida cobrar
                //Siempre se pasa por la salida ya que no hay ninguna casilla Suerte entre la Salida y Solar3
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), 46-posicion);
                cobrarSalida(jugadorActual);
                if(!evaluarCasilla(this.tablero.getCasilla(6))) bucleBancarrota();
                break;
            case 5: //Ir a la cárcel (encarcelado) sin pasar por la Salida (y por tanto sin cobrar)
                avatarActual.moverEnBasico(this.tablero.getPosiciones(), posicion<10 ? 10-posicion : 50-posicion);
                jugadorActual.encarcelar(this.tablero.getCasilla(20));
                break;
            case 6: //Cobrar 1.000.000€
                jugadorActual.sumarFortuna(1000000);
                break;
        }*/
    }
}
