package monopoly.casillas.acciones;

import java.util.ArrayList;

import monopoly.casillas.*;
import monopoly.cartas.*;
import partida.*;
import monopoly.Texto;

public class AccionCajaComunidad extends Accion {
    // ATRIBUTO EXTRA: BARAJA CORRESPONDIENTE
    private ArrayList<CartaCajaComunidad> baraja_caja_comunidad;

    // CONSTRUCTOR
    public AccionCajaComunidad(String nombre, int posicion) {
        super(nombre, posicion);

        // Añadimos las cartas de la baraja caja comunidad
        this.baraja_caja_comunidad = new ArrayList<CartaCajaComunidad>();
        this.baraja_caja_comunidad.add(new CartaCajaComunidad(Texto.CARTA_CAJA_1, 1));
        this.baraja_caja_comunidad.add(new CartaCajaComunidad(Texto.CARTA_CAJA_2, 2));
        this.baraja_caja_comunidad.add(new CartaCajaComunidad(Texto.CARTA_CAJA_3, 3));
        this.baraja_caja_comunidad.add(new CartaCajaComunidad(Texto.CARTA_CAJA_4, 4));
        this.baraja_caja_comunidad.add(new CartaCajaComunidad(Texto.CARTA_CAJA_5, 5));
        this.baraja_caja_comunidad.add(new CartaCajaComunidad(Texto.CARTA_CAJA_6, 6));
    }

    // GETTER DEL TIPO DE BARAJA PARA cogerCarta() en Accion.java
    @Override
    protected ArrayList<CartaCajaComunidad> getBaraja() {
        return this.baraja_caja_comunidad; // Return the specific deck for AccionSuerte
    }
}
