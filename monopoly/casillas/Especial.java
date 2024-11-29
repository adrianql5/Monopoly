package monopoly.casillas;

import monopoly.*;
import partida.*;

public class Especial extends Casilla{

    public float bote;
    // ==========================
    // SECCIÓN: CONSTRUCTORES
    // ==========================
    public Especial(String nombre, int posicion){
        super(nombre,posicion);
        this.bote=0;
    }

    public void incrementarBote(float bote) {
        this.bote += bote;
    }

    public float getBote(){
        return this.bote;
    }

    public void resetBote(){
        this.bote=0;
    }

    public String infoCasilla(){
        String info = "{\n";
        info += "\tTipo: Especial\n";
        info += String.format("\tPago por vuelta: %,.0f€\n", Valor.SUMA_VUELTA);
        info += jugadoresEnCasilla();
        info += "}\n";
        return info;
    }

}

