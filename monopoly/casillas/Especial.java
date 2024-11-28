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
}

