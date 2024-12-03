package partida.avatares;

import monopoly.casillas.Casilla;
import partida.Jugador;
import java.util.ArrayList;

public class Sombrero extends Avatar{
    public Sombrero(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados){
        super(jugador, lugar, avCreados);
    }

    public void infoAvatar() {
        String str = "{\n\tID: " + this.getId() + "\n" +
                "\tTipo: Sombrero\n" +
                "\tJugador: " + this.getJugador().getNombre() + "\n" +
                "\tCasilla: " + this.getLugar().getNombre() + "\n}";

        // Imprimir directamente el string
        System.out.println(str);
    }
}
