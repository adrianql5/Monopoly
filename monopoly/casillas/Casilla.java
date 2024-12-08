package monopoly.casillas;

import java.util.ArrayList;
import monopoly.casillas.propiedades.Solar;
import partida.avatares.Avatar;
import partida.*;
import monopoly.*;

/**
 * Clase abstracta que representa una casilla del tablero en el juego de Monopoly.
 */
public abstract class Casilla {

    // ATRIBUTOS--------------------------------------------------------------------------------------------------------
    protected final String nombre; // Nombre de la casilla
    protected final int posicion; // Posición en el tablero (1-40)
    protected ArrayList<Avatar> avatares; // Avatares situados en la casilla
    protected int vecesVisitada; // Número de veces que se visitó la casilla
    protected float dineroRecaudado; // Dinero recaudado por la casilla


    // CONSTRUCTORES----------------------------------------------------------------------------------------------------
    /**
     * Constructor que inicializa una casilla con su nombre y posición.
     *
     * @param nombre   Nombre de la casilla.
     * @param posicion Posición en el tablero (1-40).
     */
    protected Casilla(String nombre, int posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.avatares = new ArrayList<>();
        this.vecesVisitada = 0;
        this.dineroRecaudado = 0;
    }


    // MÉTODOS PARA MANEJO DE AVATARES----------------------------------------------------------------------------------
    /**
     * Añade un avatar a la lista de avatares de la casilla.
     *
     * @param avatar Avatar a añadir.
     */
    public void anhadirAvatar(Avatar avatar) {
        this.avatares.add(avatar);
    }

    /**
     * Elimina un avatar de la lista de avatares de la casilla.
     *
     * @param avatar Avatar a eliminar.
     */
    public void eliminarAvatar(Avatar avatar) {
        this.avatares.remove(avatar);
    }

    /**
     * Devuelve una cadena con los jugadores situados en la casilla.
     *
     * @return Lista de jugadores o una cadena vacía si no hay avatares.
     */
    public String jugadoresEnCasilla() {
        if (avatares.isEmpty()) {
            return "";
        }

        StringBuilder jugadores = new StringBuilder("\tJugadores: ");
        for (Avatar avatar : avatares) {
            jugadores.append("[").append(avatar.getJugador().getNombre()).append("]  ");
        }
        jugadores.append("\n");
        return jugadores.toString();
    }

    /**Método booleano que comprueba si un avatar está en la casilla*/
    public boolean estaAvatar(Avatar avatar){
        return avatar.getLugar().equals(this);
    }


    // MÉTODOS ABSTRACTOS-----------------------------------------------------------------------------------------------
    /**
     * Método abstracto para obtener información específica de la casilla.
     * Este método debe ser implementado por las subclases.
     */
    public abstract String infoCasilla();

    /**
     * Método abstracto que realiza las acciones de la casilla.
     * Este método debe ser implementado por las subclases.
     */
    public abstract boolean evaluarCasilla(Jugador jugadorActual, int tirada);


    // MÉTODOS AUXILIARES-----------------------------------------------------------------------------------------------
    /**Incrementa en 1 el contador de veces que se visitó la casilla.*/
    public void incrementarVecesVisitada() {
        this.vecesVisitada++;
    }

    /**
     * Suma una cantidad al dinero recaudado por la casilla.
     * Si es una casilla Solar, también suma al grupo asociado.
     *
     * @param cantidad Cantidad a añadir al dinero recaudado.
     */
    public void sumarDineroRecaudado(float cantidad) {
        this.dineroRecaudado += cantidad;

        if (this instanceof Solar) {
            Solar solar = (Solar) this;
            solar.getGrupo().sumarRecaudacionGrupo(cantidad);
        }
    }


    // GETTERS----------------------------------------------------------------------------------------------------------
    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    // Equivale a getVecesVisitada pero la quieren con este nombre
    public int frecuenciaVisita() {
        return vecesVisitada;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public float getDineroRecaudado() {
        return dineroRecaudado;
    }


}
