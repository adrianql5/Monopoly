package monopoly.edificios;

import monopoly.casillas.Casilla;

public class PistaDeporte extends Edificio {
    public PistaDeporte(Casilla lugar) {
        super(lugar);
    }

    @Override
    protected float calcularCoste(float valorGrupo) {
        return valorGrupo * 1.25f;
    }
}
