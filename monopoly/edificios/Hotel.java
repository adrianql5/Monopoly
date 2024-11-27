package monopoly.edificios;

import monopoly.casillas.Casilla;

public class Hotel extends Edificio {
    public Hotel(Casilla lugar) {
        super(lugar);
    }

    @Override
    protected float calcularCoste(float valorGrupo) {
        return valorGrupo * 0.60f; 
    }
}
