package monopoly.casillas.propiedades;

import monopoly.Valor;

public class Transporte extends Propiedad{

    private float impuesto;


    public Transporte(String nombre,int posicion){
        super(nombre,posicion);
    }

    public float calcularValor(){
        return Valor.TRANSPORTE;
    }

    public float calcularAlquiler(){
        return Valor.VALOR_IMPUESTO_TRANSPORTE;
    }

    /**Método para evaluar lo que hay que pagar en una casilla de tipo Transporte o Solar.*/
    public float evaluarAlquiler() {
        // Precio base del alquiler de cualquier casilla
        float totalAlquiler = this.impuesto;

        totalAlquiler *= 0.25f * this.duenho.contarPropiedadesPorTipo(Transporte.class);;

        return totalAlquiler;
    }

}
