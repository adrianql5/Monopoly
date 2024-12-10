package monopoly.cartas;



import monopoly.Tablero;

import partida.*;
import partida.avatares.*;

public class CartaSuerte extends Carta {

    public CartaSuerte(String texto, int indice) {
        super(texto, indice);
    }

    /**
     * Función que dada una carta ejecuta las acciones que dice la misma.
     * @return TRUE si el jugador es solvente, FALSE en caso contrario
     */
    @Override
    public boolean accion(Tablero tablero, Jugador jugadorActual, int tirada) {

        Avatar avatarActual = jugadorActual.getAvatar();
        int posicion = avatarActual.getLugar().getPosicion();

        switch(this.getID()) {
            case 1: //Ir a Transportes1 (pos=5). Si pasas por la Salida cobrar
                //Siempre se pasa por la salida ya que no hay ninguna casilla Suerte entre la Salida y Trans1
                avatarActual.moverEnBasico(tablero.getPosiciones(), 45-posicion);
                jugadorActual.cobrarSalida();

                // Evaluamos la nueva casilla!!
                return tablero.getCasilla(5).evaluarCasilla(tablero, jugadorActual, tirada);
            case 2: //Ir a Solar15 (pos=26) sin pasar por la Salida (y por tanto sin cobrar)
                avatarActual.moverEnBasico(tablero.getPosiciones(), posicion<26 ? 26-posicion : 66-posicion);

                // Evaluamos la nueva casilla!!
                return tablero.getCasilla(26).evaluarCasilla(tablero, jugadorActual, tirada);
            case 3: //Cobrar 500.000€
                jugadorActual.sumarFortuna(500000);
                return true;
            case 4: //Ir a Solar3 (pos=6). Si pasas por la Salida cobrar
                //Siempre se pasa por la salida ya que no hay ninguna casilla Suerte entre la Salida y Solar3
                avatarActual.moverEnBasico(tablero.getPosiciones(), 46-posicion);
                jugadorActual.cobrarSalida();

                // Evaluamos la nueva casilla!!
                return tablero.getCasilla(6).evaluarCasilla(tablero, jugadorActual, tirada);
            case 5: //Ir a la cárcel (encarcelado) sin pasar por la Salida (y por tanto sin cobrar)
                avatarActual.moverEnBasico(tablero.getPosiciones(), posicion<10 ? 10-posicion : 50-posicion);
                jugadorActual.encarcelar(tablero.getCasilla(20));
                return true;
            case 6: //Cobrar 1.000.000€
                jugadorActual.sumarFortuna(1000000);
                return true;
            default:
                return true;
        }
    }
}
