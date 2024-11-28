package monopoly.casillas;

import static java.lang.Math.*;

import monopoly.Valor;

public class Impuesto extends Casilla{
    private float impuesto;
    
    // ==========================
    // SECCIÓN: CONSTRUCTORES
    // ==========================
    public Impuesto(String nombre, int posicion) {
        super(nombre, posicion);
        this.impuesto=calcularImpuesto();
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
    

}
