package monopoly.casillas.acciones;

import java.util.ArrayList;

import monopoly.casillas.*;
import monopoly.cartas.*;
import partida.*;
import monopoly.Texto;
import monopoly.*;

public  abstract class Accion extends Casilla {
    // ATRIBUTO EXTRA: independientemente de la baraja necesitan la carta al revés para cuando se muestran las cartas
    private CartaReverso carta_del_reves;

    // CONSTRUCTOR
    public Accion(String nombre, int posicion) {
        super(nombre, posicion);

        // Añadimos la carta al revés
        this.carta_del_reves = new CartaReverso();
    }


    // MÉTODO ABSTRACTO PARA OBTENER LA BARAJA DEL TIPO CORRESPONDIENTE AL TIPO DE CASILLA DE ACCION
    protected abstract ArrayList<? extends Carta> getBaraja();


    // IMPLEMENTACIÓN DE LOS MÉTODOS ABSTRACTOS DE CASILLA--------------------------------------------------------------

    @Override
    public String infoCasilla() {
        return "No se puede describir esta casilla.";
    }

    @Override
    public boolean evaluarCasilla(Jugador jugadorActual, int tirada) {
        cogerCarta();
        return true;
    }


    // MÉTODOS SOBRE EL PROCESO DE ESCOGER LA CARTA---------------------------------------------------------------------

    /**Método para cuando se cae en una casilla de tipo Suerte o Caja de comunidad
     * [1] Reordena de manera aleatoria el ArrayList de cartas correspondiente
     * [2] Le pide al usuario el número de la carta que quiere escoger (del 1 al 6)
     */
    public void cogerCarta() {
        consola.imprimir("Barajando las cartas...");
        Collections.shuffle(getBaraja()); //Barajamos las cartas
        cartasAlReves(); //Mostramos el reverso de las cartas
        consola.imprimir("Escoge una carta con un número del 1 al 6.");
        int n=leerDadoValido(); //Leemos input hasta que sea un número válido
        mostrarCartaEscogida(getBaraja(), n); //Volvemos a mostrar las cartas con la escogida dada la vuelta
        //if(!evaluarCarta(getBaraja().get(n-1))) bucleBancarrota(); //Ojo con los índices del ArrayList que empiezan en 0!!
    }

    /**
     * Función que dada una carta ejecuta las acciones que dice la misma.
     * @param carta Carta que tenemos que evaluar
     * @return TRUE si el jugador es solvente, FALSE en caso contrario
     */
     public boolean evaluarCarta(Jugador jugadorActual, Carta carta) {

        return true;
     }

    /**Método auxiliar (por eso privado) para imprimir 6 cartas al revés en fila.*/
    private void cartasAlReves() {
        // Vamos imprimiendo línea por línea
        for(int i=0; i<Valor.NLINEAS_CARTA; i++) {
            // Función repeat() muy útil pa este caso
            consola.imprimir(this.carta_del_reves.getTexto().get(i).repeat(6));
        }
    }

    /**
     * Método auxiliar (por eso privado) para imprimir 6 cartas, todas al revés menos la que indica el índice
     * @param baraja Baraja de cartas de entre las cuales se escoge
     * @param n Posición de la carta que se escoge en el ArrayList
     */
    private void mostrarCartaEscogida(int n) {
        // Para cada línea...
        for(int i=0; i<Valor.NLINEAS_CARTA; i++) {
            // ...iteramos para cada carta (en total 6)...
            for(int j=0; j<6; j++) {
                // ...vemos si la carta es la que escogió el jugador o no
                // Nótese que el jugador escoge del 1 al 6 pero los índices empiezan en 0
                if(j==n-1) {
                    System.out.print(getBaraja().get(n-1).getTexto().get(i)); // consola.imprimir mete /n... piedad...
                }
                else {
                    System.out.print(this.carta_del_reves.getTexto().get(i)); // consola.imprimir mete /n... piedad...
                }
            }
            Juego.consola.imprimir(""); //Imprimimos un salto de línea al haber iterado las 6 cartas
        }
    }

}
