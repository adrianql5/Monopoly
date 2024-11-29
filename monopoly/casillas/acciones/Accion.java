package monopoly.casillas.acciones;

import monopoly.casillas.*;;

public class Accion extends Casilla {
    public Accion(String nombre, int posicion) {
        super(nombre,posicion);
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
