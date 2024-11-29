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

    public String infoCasilla(){
        String info = "{\n";
        info += "\tTipo: Transporte\n";
        info += "\tDueño: " + this.duenho.getNombre() + "\n";
        info += String.format("\tPrecio: %,.0f€\n", this.valor);
        info += String.format("\tPago por caer: %,.0f€\n",
                this.impuesto * 0.25f * this.duenho.contarPropiedadesPorTipo(Transporte.class));
        info += String.format("\t\t(cada casilla de este tipo que tengas suma 1/4 de %,.0f€ al alquiler)\n",
                this.impuesto);
        info += String.format("\tHipoteca: %,.0f€\n", this.hipoteca);
        info += jugadoresEnCasilla();
        info += "}\n";
        return info;
    }

}
