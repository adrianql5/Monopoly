package monopoly.casillas.propiedades;

import monopoly.Valor;

import partida.*;

public class Servicio extends Propiedad{

    public Servicio(String nombre,int posicion, Jugador duenho) {
        super(nombre,posicion,duenho);
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

    public boolean evaluarCasilla(Jugador jugadorActual, int tirada) {
        if (duenho != jugadorActual) {
            if (!duenho.getNombre().equals("banca")) {
                if (!estaHipotecada) {
                    float precio = evaluarAlquiler(tirada);
                    System.out.printf("%s debe pagar el servicio a %s: %,.0f€\n",
                            jugadorActual.getNombre(), duenho.getNombre(), precio);
                    // Si puede pagarlo de alguna manera se cobra
                    if(precio>jugadorActual.getFortuna()) {
                        jugadorActual.setDeudaConJugador(duenho);
                        jugadorActual.setDeuda(precio);
                        return false;
                    }
                            
                    jugadorActual.pagar(duenho, precio);

                    return true;
                } else {
                    System.out.println("La casilla " + this.nombre + " está hipotecada. No hay que pagar.");
                }
            } else {
                System.out.println("La casilla " + this.nombre + " está a la venta.\n");
            }
        } else {
            System.out.println("Esta casilla te pertenece.");
        }
        return true;
    }



    public String infoCasilla(){
        String info = "{\n";
        info += "\tTipo: Servicio\n";
        info += "\tDueño: " + this.duenho.getNombre() + "\n";
        info += String.format("\tPrecio: %,.0f€\n", this.valor);
        info += String.format("\tPago por caer: dados * x * %,.0f€\n", this.alquiler);
        info += "\t\t(x=4 si se posee una casilla de este tipo, x=10 si se poseen 2)\n";
        info += String.format("\tHipoteca: %,.0f€\n", this.hipoteca);
        info += jugadoresEnCasilla();
        info += "}\n";
        return info;
    }

}
