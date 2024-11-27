package monopoly.casillas;


public class Impuesto extends Casilla{
    private float impuesto;
    
    public Impuesto(String nombre, int posicion, float impuesto) {
        super(nombre, posicion);
        this.impuesto=impuesto;
    }
}
