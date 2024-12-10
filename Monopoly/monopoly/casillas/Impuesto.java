package monopoly.casillas;

import monopoly.*;
import partida.*;


public class Impuesto extends Casilla{
    private float impuesto;

    // ==========================
    // SECCIÓN: CONSTRUCTORES
    // ==========================
    public Impuesto(String nombre, int posicion) {
        super(nombre, posicion);
        this.impuesto=calcularImpuesto();
    }

    public float getImpuesto(){
        return this.impuesto;
    }

    public float calcularImpuesto(){
        return this.posicion == 4 ? Valor.SUMA_VUELTA/2 : Valor.SUMA_VUELTA;
    }

    @Override
    public boolean evaluarCasilla(Tablero tablero, Jugador jugadorActual, int tirada){
        incrementarVecesVisitada();
        Juego.consola.imprimir(String.format("Debes pagar tus impuestos a la banca: %,.0f€\n", impuesto));

        // Si puede pagarlo de alguna manera se cobra
        if(this.impuesto>jugadorActual.getFortuna()) {
            jugadorActual.setDeudaConJugador(null);
            jugadorActual.setDeuda(this.impuesto);
            return false;
        }
            
        jugadorActual.sumarFortuna(-this.impuesto);

        // Incrementamos el bote del parking
        Especial parking = (Especial) tablero.getCasilla(20);
        parking.incrementarBote(this.impuesto);

        return true;
    }

    @Override
    public String infoCasilla(){
        String info = "{\n";
        info += "\tTipo: Impuestos\n";
        info += String.format("\tA pagar: %,.0f€\n", this.impuesto);
        info += jugadoresEnCasilla();
        info += "}\n";
        return info;
    }
}
