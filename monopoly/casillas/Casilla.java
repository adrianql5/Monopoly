package monopoly.casillas;

import java.util.ArrayList;
import monopoly.casillas.propiedades.Solar;
import partida.avatares.Avatar;

public abstract class Casilla {
    // ATRIBUTOS
    protected String nombre; // Nombre de la casilla
    protected int posicion; // Posición en el tablero (1-40)
    protected ArrayList<Avatar> avatares; // Avatares situados en la casilla
    protected int vecesVisitada; // Número de veces que se visitó la casilla
    protected float dineroRecaudado; // Dinero recaudado por la casilla

    // CONSTRUCTOR
    public Casilla(String nombre, int posicion) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.avatares = new ArrayList<>();
        this.vecesVisitada = 0;
        this.dineroRecaudado = 0;
    }

    // MÉTODOS PARA MANEJO DE AVATARES
    public void anhadirAvatar(Avatar avatar) {
        this.avatares.add(avatar);
    }

    public void eliminarAvatar(Avatar avatar) {
        this.avatares.remove(avatar);
    }

    // MÉTODOS ABSTRACTOS Y GENÉRICOS
    /**
     * Método abstracto para obtener información específica de la casilla.
     * Implementado por cada subclase.
     */

    //public abstract String infoCasilla();

    /**
     * Método auxiliar que devuelve la lista de jugadores en una casilla.
     * Si no hay avatares, devuelve una cadena vacía.
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

    // GETTERS Y SETTERS
    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int nuevaPosicion) {
        if (nuevaPosicion >= 1 && nuevaPosicion <= 40) {
            this.posicion = nuevaPosicion;
        } else {
            System.out.println(nuevaPosicion + " no es una posición válida.\n");
        }
    }

    public int getVecesVisitada() {
        return vecesVisitada;
    }

    public void incrementarVecesVisitada() {
        this.vecesVisitada++;
    }

    public ArrayList<Avatar> getAvatares() {
        return avatares;
    }

    public float getDineroRecaudado() {
        return dineroRecaudado;
    }


    
    public void sumarDineroRecaudado(float cantidad) {
        this.dineroRecaudado += cantidad;

        // Si es una casilla Solar, también suma al grupo
        if (this instanceof Solar) {
            Solar solar = (Solar) this;
            solar.getGrupo().sumarRecaudacionGrupo(cantidad);
        }
    }

    public int contarTipoPropiedadesGrupos(String tipo) {
        int index = getTipoIndex(tipo); // Obtener el índice del tipo de edificación
        if (index == -1) {
            return 0; // Retornar 0 si el tipo es inválido
        }
        int contador = 0;
        // Contar todas las edificaciones del tipo especificado en cada casilla del grupo
        for (Propiedad c : grupo.getMiembrosGrupo()) {
            contador += c.contarTipoPropiedadesCasilla(tipo); // Usar la función para contar en cada casilla
        }
        return contador;
    }
    
}
