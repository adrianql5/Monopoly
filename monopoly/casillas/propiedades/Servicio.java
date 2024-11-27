package monopoly.casillas.propiedades;

import monopoly.Valor;
import partida.Jugador;

public class Servicio extends Propiedad{

    public Servicio(String nombre,int posicion, float valor) {
        super(nombre,posicion, valor);
        this.valor=valor;
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
