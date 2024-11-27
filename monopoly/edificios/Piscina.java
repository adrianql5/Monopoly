package monopoly.edificios;

import monopoly.casillas.Casilla;

public class Piscina extends Edificio {
    public Piscina(Casilla lugar) {
        super(lugar);
    }

    @Override
    protected float calcularCoste(float valorGrupo) {
        return valorGrupo * 0.40f;
    }
}
