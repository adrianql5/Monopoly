package monopoly.casillas.propiedades;

import monopoly.Valor;
import partida.*;

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

    public boolean evaluarCasilla(Jugador jugadorActual, int tirada) {
        if (duenho != jugadorActual) {
            if (!duenho.getNombre().equals("banca")) {
                if (!estaHipotecada) {
                    float precio = evaluarAlquiler();
                    System.out.printf("%s debe pagar el servicio de transporte a %s: %,.0f€\n",
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
