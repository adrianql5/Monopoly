package partida.avatares;

import java.util.ArrayList;
import java.util.Random;

import monopoly.*;
import partida.Jugador;
import monopoly.casillas.*;

public abstract class Avatar {
    
    //ATRIBUTOS
    protected String id; //Identificador: una letra generada aleatoriamente.
    protected Jugador jugador; //Un jugador al que pertenece ese avatar.
    protected Casilla lugar; //Los avatares se sitúan en casillas del tablero.
    protected boolean movimientoAvanzado; //TRUE si el avatar está en modo de movimiento avanzado.
    protected ArrayList<Avatar> avCreados;


    //SECCIÓN DE CONSTRUCTORES DE AVATAR--------------------------------------------------------------------------------
    
    /**
     * Constructor principal.
     *
     * @param jugador   Jugador al que pertenece
     * @param lugar     Lugar en el que está ubicado
     * @param avCreados Arraylist con los avatares creados (usado para crear un ID distinto a los demás)
     */
    protected Avatar(Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.jugador = jugador;
        this.lugar = lugar;
        this.movimientoAvanzado = false;
        this.avCreados = avCreados;
        generarId(avCreados);
        this.avCreados.add(this);
    }

    /**
     * Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
     * El ID generado será una letra mayúscula entre A y Z.
     *
     * @param avCreados Arraylist de los avatares ya creados (objetivo: evitar que se generen dos ID iguales)
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        Random num = new Random();
        String ID;
        boolean repetido = true;

        while (repetido) {
            repetido = false;
            ID = String.valueOf((char) (num.nextInt(26) + 'A'));

            for (Avatar a : avCreados) {
                if (a != null && a.getId().equals(ID)) {
                    repetido = true;
                    break;  //Si uno es igual no hace falta comprobar el resto
                }
            }
            if (!repetido) {
                this.id = ID;
            }
        }
    }

    
    //SECCIÓN DE MÉTODOS ÚTILES DE AVATAR-------------------------------------------------------------------------------

    /**Método que imprime la información sobre el avatar.*/
    public void infoAvatar() {

        String info = "{\n\tID: " + this.getId() + "\n" +
                "\tTipo: " + this.getClass().getSimpleName() + "\n" +
                "\tJugador: " + this.getJugador().getNombre() + "\n" +
                "\tCasilla: " + this.getLugar().getNombre() + "\n}";

        // Imprimir directamente el string
        Juego.consola.imprimir(info);
    }

    /**Método para cambiar el tipo de movimiento del avatar.*/
    public void cambiarMovimiento() {
        this.movimientoAvanzado = !this.movimientoAvanzado;
    }


    // MÉTODOS DE MOVIMIENTO DEL AVATAR---------------------------------------------------------------------------------

    /**Método que "mueve" la ficha de una casilla a otra.*/
    public void colocar(ArrayList<ArrayList<Casilla>> casillas, int nuevaPosicion) {
        // Eliminar el avatar de la casilla actual
        this.lugar.eliminarAvatar(this);

        // Buscar la nueva casilla en el tablero
        for (ArrayList<Casilla> lado : casillas) {
            for (Casilla casilla : lado) {
                if (casilla.getPosicion() == nuevaPosicion) {
                    this.lugar = casilla; // Actualizar la ubicación del avatar a la nueva casilla
                    break;
                }
            }
        }

        // Añadir el avatar a la nueva casilla
        this.lugar.anhadirAvatar(this);
    }

    /**
     * Método que permite mover a un avatar a una casilla concreta en modo básico.
     * Este método no comprueba si se pasa por la Salida ni hace el ingreso correspondiente.
     * Versión 2: ya se admite que valorTirada sea un número negativo.
     * @param casillas    Array con las casillas del tablero. Es un arrayList de arrayList de casillas (uno por lado)
     * @param valorTirada Número de casillas a moverse (se llama valorTirada pero no depende de los dados siempre)
     */
    public void moverEnBasico(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        // PASO 1: calcular la nueva posición
        int posicionActual = this.lugar.getPosicion();

        // Calcular la nueva posición en el tablero después de la tirada
        int nuevaPosicion;
        // Si queremos avanzar un número positivo de casillas (o un número negativo sin pasar por la Salida hacia atrás)
        if(valorTirada>=0 || posicionActual>=Math.abs(valorTirada)) {
            nuevaPosicion = (posicionActual + valorTirada) % 40;
        }
        else {
            //Nos queremos mover un número de casillas negativo (y pasamos hacia atrás por la casilla de Salida)
            nuevaPosicion = 40 + ( (posicionActual + valorTirada) % 40 );
        }

        // PASO 2: colocar el avatar en la nueva posicion
        this.colocar(casillas, nuevaPosicion);
    }

    /**Método abstracto que implementara cada tipo de avatar con su tipo de movimiento avanzado propio.*/
    public abstract void moverEnAvanzado(ArrayList<ArrayList<Casilla>> casillas, int valorTirada);


    // GETTERS, SETTERS Y MÉTODOS BOOLEANOS DE AVATAR-------------------------------------------------------------------

    public String getId() {
        return id;
    }
    //El id no necesita setter porque se le asigna un valor al crear el Avatar y no hay que modificarlo nunca
    public Jugador getJugador() {
        return jugador;
    }
    
    public void setJugador(Jugador jugador_avatar) {
        this.jugador = jugador_avatar;
    }

    public Casilla getLugar() {
        return lugar;
    }

    public void setLugar(Casilla casilla_avatar) {
        this.lugar = casilla_avatar;
    }

    public boolean getMovimientoAvanzado() {
        return movimientoAvanzado;
    }

}
