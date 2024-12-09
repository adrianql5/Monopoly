package monopoly.cartas;

import java.util.ArrayList;

import monopoly.Tablero;

import monopoly.casillas.Especial;
import partida.*;
import partida.avatares.*;

public class CartaCajaComunidad extends Carta {

    // Atributo extra: necesitamos los jugadores de la partida para la carta 6
    private ArrayList<Jugador> jugadoresPartida;

    public CartaCajaComunidad(String texto, int indice) {
        super(texto, indice);
        this.jugadoresPartida = new ArrayList<>();
    }

    public void setJugadoresPartida(ArrayList<Jugador> jugadoresPartida) {
        this.jugadoresPartida = jugadoresPartida;
    }

    /**
     * Función que dada una carta ejecuta las acciones que dice la misma.
     * //@param jugadorActual Jugador al que se le tiene que aplicar las acciones de la carta.
     */
    public boolean accion(Tablero tablero, Jugador jugadorActual, int tirada) {

        Avatar avatarActual = jugadorActual.getAvatar();
        int posicion = avatarActual.getLugar().getPosicion();

        // Obtenemos la casilla del parking que nos va a hacer falta en 2 casos
        Especial parking = (Especial) tablero.getCasilla(20);

        switch(this.ID) {
            case 1: //Pagar 500.000€ (a la banca)
                if(500000f>jugadorActual.getFortuna()){
                    jugadorActual.setDeuda(500000f);
                    jugadorActual.setDeudaConJugador(null);
                    return false;
                }
                jugadorActual.sumarFortuna(-500000f);
                jugadorActual.sumarGastos(500000);
                jugadorActual.getEstadisticas().sumarPagoDeAlquileres(500000f);

                // Incrementamos el bote del parking
                parking.incrementarBote(500000f);
                return true;
            case 2: //Ir a la cárcel (encarcelado) sin pasar por la Salida (y por tanto sin cobrar)
                avatarActual.moverEnBasico(tablero.getPosiciones(), posicion<10 ? 10-posicion : 50-posicion);
                jugadorActual.encarcelar(tablero.getCasilla(20));
                return true;
            case 3: //Ir a Salida (pos=0=40) y cobrar
                avatarActual.moverEnBasico(tablero.getPosiciones(), 40-posicion);
                jugadorActual.cobrarSalida();
                return true;
            case 4: //Cobrar 2.000.000€
                jugadorActual.sumarFortuna(2000000);
                return true;
            case 5: //Pagar 1.000.000€ (a la banca)
                if(1000000f > jugadorActual.getFortuna()){
                    jugadorActual.setDeuda(500000f);
                    jugadorActual.setDeudaConJugador(null);
                    return false;
                }
                jugadorActual.sumarFortuna(-1000000f);
                jugadorActual.sumarGastos(1000000f);
                jugadorActual.getEstadisticas().sumarPagoDeAlquileres(1000000f);

                // Incrementamos el bote del parking
                parking.incrementarBote(1000000f);
                return true;
            case 6: //Pagar 200.000€ a cada jugador
                float total_a_pagar = 200000 * (this.jugadoresPartida.size()-1);

                if(total_a_pagar > jugadorActual.getFortuna()){
                    jugadorActual.setDeuda(total_a_pagar);
                    // SI NO SE PUEDE PAGAR LA DEUDA PA LA BANCARROTA ES CON LA BANCA
                    jugadorActual.setDeudaConJugador(null);
                    return false;
                }
                jugadorActual.restarFortuna(total_a_pagar);

                //Recorremos el ArrayList de jugadores: si no es el jugador actual sumamos 200.000€
                for(Jugador j : this.jugadoresPartida) {
                    if(!j.equals(jugadorActual)) {
                        j.sumarFortuna(200000);
                    }
                }
                return true;
            default:
                return true;
        }
    }
}
