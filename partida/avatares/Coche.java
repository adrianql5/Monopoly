package partida.avatares;

import java.util.*;

import monopoly.casillas.*;
import partida.*;

public class Coche extends Avatar{
    public Coche(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados){
        super(jugador, lugar, avCreados);
    }

    public void infoAvatar() {
        String str = "{\n\tID: " + this.getId() + "\n" +
                "\tTipo: Coche\n" +
                "\tJugador: " + this.getJugador().getNombre() + "\n" +
                "\tCasilla: " + this.getLugar().getNombre() + "\n}";

        // Imprimir directamente el string
        System.out.println(str);
    }
}
