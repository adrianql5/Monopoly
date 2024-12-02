package monopoly.casillas.acciones;

import partida.*;

public class AccionCajaComunidad extends Accion {
    public AccionCajaComunidad(String nombre, int posicion) {
        super(nombre,posicion);
    }

    public boolean evaluarCasilla(Jugador jugadorActual, int tirada){
        return true;
    }

    public String infoCasilla(){
        String info = "{\n";
        info += "\tTipo: Accion\n";
        info += String.format("\tDeberás escoger una entre 6 cartas\n");
        info += jugadoresEnCasilla();
        info += "}\n";
        return info;
    }
}
