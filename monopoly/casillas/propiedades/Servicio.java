package monopoly.casillas.propiedades;

import monopoly.Valor;

public class Servicio extends Propiedad{

    public Servicio(String nombre,int posicion) {
        super(nombre,posicion);
    }

    public float calcularValor(){
        return Valor.SERVICIO;
    }

    public float calcularAlquiler(){
        return Valor.VALOR_IMPUESTO_SERVICIO;
    }

    /**Método para evaluar lo que hay que pagar en una casilla de tipo Servicio.
     * Sobrecarga del método para las casillas de este tipo ya que necesitan el valor de la tirada.
     * @param tirada Valor de la tirada
     */
    public float evaluarAlquiler(int tirada) {
        // Precio base del alquiler de cualquier casilla
        float totalAlquiler = alquiler;

        totalAlquiler *= tirada * (this.duenho.contarPropiedadesPorTipo(Servicio.class) == 1 ? 4 : 10);
        
        return totalAlquiler;
    }
}
