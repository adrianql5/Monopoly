package monopoly.edificios;

import monopoly.casillas.Casilla;

public class Casa extends Edificio {
    public Casa(Casilla lugar) {
        super(lugar);
    }

    @Override
    protected float calcularCoste(float valorGrupo) {
        return valorGrupo * 0.60f;
    }
}
