package monopoly.casillas.acciones;

import java.util.ArrayList;

import monopoly.casillas.*;
import monopoly.cartas.*;
import partida.*;
import monopoly.Texto;

public class AccionSuerte extends Accion {
    // ATRIBUTO EXTRA: BARAJA CORRESPONDIENTE
    private ArrayList<CartaSuerte> baraja_suerte;

    // CONSTRUCTOR
    public AccionSuerte(String nombre, int posicion) {
        super(nombre, posicion);

        // Añadimos las cartas de la baraja suerte
        this.baraja_suerte = new ArrayList<CartaSuerte>();
        this.baraja_suerte.add(new CartaSuerte(Texto.CARTA_SUERTE_1, 1));
        this.baraja_suerte.add(new CartaSuerte(Texto.CARTA_SUERTE_2, 2));
        this.baraja_suerte.add(new CartaSuerte(Texto.CARTA_SUERTE_3, 3));
        this.baraja_suerte.add(new CartaSuerte(Texto.CARTA_SUERTE_4, 4));
        this.baraja_suerte.add(new CartaSuerte(Texto.CARTA_SUERTE_5, 5));
        this.baraja_suerte.add(new CartaSuerte(Texto.CARTA_SUERTE_6, 6));
    }

    // GETTER DEL TIPO DE BARAJA PARA cogerCarta() en Accion.java
    @Override
    protected ArrayList<CartaSuerte> getBaraja() {
        return this.baraja_suerte; // Return the specific deck for AccionSuerte
    }
}
