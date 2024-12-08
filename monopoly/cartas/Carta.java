package monopoly.cartas;

import java.util.ArrayList;

import monopoly.Texto;
import monopoly.Valor;
import monopoly.Juego;

import partida.*;
import partida.avatares.*;

public abstract class Carta {
    // ATRIBUTOS
    protected final ArrayList<String> texto;
    protected final int ID;


    // CONSTRUCTOR
    /**
     * Constructor de la clase Carta que crea la carta en formato Ascii
     * a partir del mensaje que tiene que mostrar dentro.
     * Separa el texto introducido en palabras y las va añadiendo a cada línea de la carta final (si cogen).
     * Nótese que la carta se guarda como un ArrayList<String> (porque luego se imprimirán varias cartas en fila).
     * @param texto Mensaje que contiene la carta
     * @param indice Número para diferenciar las cartas dentro de una baraja, en la función evaluarCasilla que es la
     *               que ejecuta las acciones de la carta hay un switch en función de este índice
     */
    protected Carta(String texto, int indice) {
        this.texto = new ArrayList<String>();
        this.ID = indice;

        //Borde superior
        this.texto.add(Texto.CARTA_BORDESUP);

        // CREAR LAS LÍNEAS DEL MEDIO: ESTO ES LO COMPLICADO
        // Función addAll para añadir todos los elementos que contiene el ArrayList q devuelve ajustarTextoEnCarta()
        this.texto.addAll(ajustarTextoEnCarta(texto, Valor.NCHARS_CARTA));

        //Borde inferior
        this.texto.add(Texto.CARTA_BORDEINF);
    }


    //GETTERS Y SETTERS-------------------------------------------------------------------------------------------------
    public ArrayList<String> getTexto() {
        return this.texto;
    }

    public int getID() {
        return this.ID;
    }


    // MÉTODOS ABSTRACTOS-----------------------------------------------------------------------------------------------
    /**
     * Función que dada una carta ejecuta las acciones que dice la misma.
     * @param carta Carta que tenemos que evaluar
     * @return TRUE si el jugador es solvente, FALSE en caso contrario
     */
    public abstract boolean evaluarCarta(Jugador jugadorActual);

    //public abstract void accion();


    //MÉTODOS ÚTILES DE CARTA-------------------------------------------------------------------------------------------

    // HAY QUE MOVER ESTA FUNCIÓN A LA CLASE MÁS GENÉRICA DEL MONOPOLY
    /**Método para crear cadenas de espacios.
     * @param n Número de espacios en blanco que se quieren
     */
    public String conEspacios(int n) {
        String cadena = ""; //Creamos una cadena vacía
        if(n>=0) {
            for(int m=0; m<n; m++) {
                cadena += " ";
            }
        }
        else {
            System.out.println("\nError en la función conEspacios: número negativo de espacios.\n");
            //Nótese que igualmente devuelve una cadena vacía si se introduce un número negativo
        }
        return(cadena);
    }

    // Podría estar en la clase Texto pero bueno queda por aquí que es el único sitio en el que se usa de momento
    /**Método auxiliar que coloca un texto en líneas con un máximo de caracteres por línea.
     * [1] Vamos sumando las longitudes de las palabras por orden (teniendo en cuenta los espacios entre ellas).
     * [2] Si la siguiente palabra que queremos meter hace que la cadena exceda el ancho tenemos la línea acabada.
     * [3] Repetimos el proceso hasta haber asignado línea a todas las palabras del texto.
     * @param texto Mensaje que hay que colocar por líneas
     * @param ancho Máximo de caracteres que puede tener una línea
     */
    private ArrayList<String> ajustarTextoEnCarta(String texto, int ancho) {

        // Variables que necesitamos
        ArrayList<String> texto_carta = new ArrayList<String>();
        int i=0; // Es el índice de la palabra en palabras_texto
        int lineas_totales=0;
        String linea_aux="";
        boolean primeraPalabra;

        // Separamos el texto original x palabras
        String[] palabras_texto = texto.split(" ");

        // Miramos cuántas palabras cogen por cada línea (-2 porque no se cuentan los bordes superior/inferior)
        while(lineas_totales<Valor.NLINEAS_CARTA-2) {

            // Variables auxiliares: las reiniciamos en cada vuelta
            primeraPalabra = true; // Boolean para comprobar si es la primera palabra de la línea
            linea_aux = ""; //Reiniciamos la línea auxiliar!!

            // Recorremos el texto desde la primera palabra que aún no ha sido añadida
            // Se van concatenando palabras mientras no nos pasemos del ancho...
            while (i < palabras_texto.length) {

                // La primera palabra no lleva un espacio antes de ella, el resto sí :)
                if (primeraPalabra) {
                    if (palabras_texto[i].length() > ancho) {
                        System.out.println("Error en ajustarTextoEnCarta: ancho de la palabra superior al permitido.");
                        return texto_carta;
                    }
                    else {
                        linea_aux += palabras_texto[i];
                        primeraPalabra = false;
                        i++; //Ponemos el índice para que apunte a la siguiente palabra
                    }
                } else {
                    // Si la suma de las palabras que ya están en la línea con la nueva (y 1 espacio) supera el ancho...
                    if (linea_aux.length() + 1 + palabras_texto[i].length() > ancho) {
                        //...significa que ya tenemos la línea acabada
                        // Rellenamos hasta el ancho con espacios y añadimos los bordes
                        linea_aux = Texto.CARTA_BORDE + linea_aux + conEspacios(ancho - linea_aux.length()) + Texto.CARTA_BORDE;
                        texto_carta.add(linea_aux);
                        lineas_totales++;
                        break;
                    }
                    else {
                        linea_aux += " " + palabras_texto[i];
                        i++; //Ponemos el índice para que apunte a la siguiente palabra
                    }
                }
            }

            // Si es la última palabra ya no tenemos nada más que añadir
            if(i==palabras_texto.length) {
                linea_aux = Texto.CARTA_BORDE + linea_aux + conEspacios(ancho - linea_aux.length()) + Texto.CARTA_BORDE;
                texto_carta.add(linea_aux);
                break;
            }
        }

        // Tenemos que centrar la carta si el texto no ocupa toda su altura (-2 por los bordes superior/inferior)
        int lineas_por_rellenar = Valor.NLINEAS_CARTA-2-texto_carta.size();

        // División de enteros a un entero: si el número es impar se desprecia la parte decimal
        int num_lpr_arriba = lineas_por_rellenar/2;
        int num_lpr_abajo = lineas_por_rellenar - num_lpr_arriba;

        // Añadimos las líneas por arriba (si no hubiese ninguna se salta)
        for(int j=0; j<num_lpr_arriba; j++) {
            texto_carta.add(0,Texto.CARTA_LINEAVACIA);
        }
        // Añadimos las líneas por abajo (si no hubiese ninguna se salta)
        for(int j=0; j<num_lpr_abajo; j++) {
            texto_carta.add(Texto.CARTA_LINEAVACIA);
        }

        return texto_carta;
    }
}
