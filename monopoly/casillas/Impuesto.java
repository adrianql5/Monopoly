package monopoly.casillas;

import monopoly.*;
import partida.*;


public class Impuesto extends Casilla{
    private float impuesto;
    private Especial parking;
    // ==========================
    // SECCIÓN: CONSTRUCTORES
    // ==========================
    public Impuesto(String nombre, int posicion) {
        super(nombre, posicion);
        this.impuesto=calcularImpuesto();
        this.parking=null;
    }

    public void asignarParking(Especial parking){
        this.parking=parking;
    }

    public float getImpuesto(){
        return this.impuesto;
    }

    public float calcularImpuesto(){
        switch (posicion) {
            case 38:
                return Valor.SUMA_VUELTA;
            
            case 4:
                return Valor.SUMA_VUELTA/2;
        
            default:
                return 0.0f;
        }
    }

    @Override
    public boolean evaluarCasilla(Jugador jugadorActual, int tirada){
        incrementarVecesVisitada();
        Juego.consola.imprimir(String.format("Debes pagar tus impuestos a la banca: %,.0f€\n", impuesto));

        // Si puede pagarlo de alguna manera se cobra
        if(impuesto>jugadorActual.getFortuna()) {
            jugadorActual.setDeudaConJugador(null);
            jugadorActual.setDeuda(impuesto);
            return false;
        }
            
        jugadorActual.sumarFortuna(-impuesto);

        parking.incrementarBote(impuesto);

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
