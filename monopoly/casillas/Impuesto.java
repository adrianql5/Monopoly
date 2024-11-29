package monopoly.casillas;

import static java.lang.Math.*;

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
        switch (posicion) {
            case 38:
                return Valor.SUMA_VUELTA;
            
            case 4:
                return Valor.SUMA_VUELTA/2;
        
            default:
                return 0.0f;
        }
    }

    public String infoCasilla(){
        String info = "{\n";
        info += "\tTipo: Impuestos\n";
        info += String.format("\tA pagar: %,.0f€\n", this.impuesto);
        info += jugadoresEnCasilla();
        info += "}\n";
        return info;
    }
}
