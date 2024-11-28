package monopoly;

import partida.Jugador;

import java.util.ArrayList;
import java.util.Iterator;

import monopoly.casillas.Casilla;
import monopoly.casillas.propiedades.Propiedad;


public class Trato {

    // Atributos de la clase
    private static int contadorTratos = 0; // Generador de ID único
    private String id; // Identificador único del trato
    private Jugador jugadorPropone; // Jugador que propone el trato
    private Jugador jugadorRecibe; // Jugador que recibe el trato
    private ArrayList<Casilla> propiedadesOfrecidas; // Propiedades que ofrece el jugador que propone
    private ArrayList<Casilla> propiedadesDemandadas; // Propiedades que solicita el jugador que propone
    private float dineroOfrecido; // Dinero que ofrece el jugador que propone
    private float dineroDemandado; // Dinero que solicita el jugador que propone


    // Constructor
    public Trato(Jugador jugadorPropone, Jugador jugadorRecibe,
        ArrayList<Casilla> propiedadesOfrecidas, ArrayList<Casilla> propiedadesDemandadas,
        float dineroOfrecido, float dineroDemandado) {

        this.id = "trato" + (++contadorTratos); // Asigna un ID único
        this.jugadorPropone = jugadorPropone;
        this.jugadorRecibe = jugadorRecibe;
        this.propiedadesOfrecidas = propiedadesOfrecidas != null ? propiedadesOfrecidas : new ArrayList<>();
        this.propiedadesDemandadas = propiedadesDemandadas != null ? propiedadesDemandadas : new ArrayList<>();
        this.dineroOfrecido = dineroOfrecido;
        this.dineroDemandado = dineroDemandado;
    }

    // Métodos de la clase
    public String getId() {
        return id;
    }

    public Jugador getJugadorPropone() {
        return jugadorPropone;
    }

    public Jugador getJugadorRecibe() {
        return jugadorRecibe;
    }

    public ArrayList<Casilla> getPropiedadesOfrecidas() {
        return propiedadesOfrecidas;
    }

    public ArrayList<Casilla> getPropiedadesDemandadas() {
        return propiedadesDemandadas;
    }

    public float getDineroOfrecido() {
        return dineroOfrecido;
    }

    public float getDineroDemandado() {
        return dineroDemandado;
    }

    /**
     * Comprueba si un trato es válido para ser propuesto.
     * Un trato no es válido si alguna propiedad ofrecida no pertenece al jugador que propone,
     * o si alguna propiedad demandada no pertenece al jugador que recibe.
     */

    /**
     * Realiza las acciones necesarias para aceptar el trato.
     *
     * @return true si el trato fue aceptado exitosamente, false en caso contrario.
     */
    public boolean aceptar() {
        // Verificar dinero demandado
        if (dineroDemandado > 0 && jugadorRecibe.getFortuna() < dineroDemandado) {
            System.out.printf("El trato no puede ser aceptado: %s no dispone de suficiente dinero.\n",
                    jugadorRecibe.getNombre());
            return false;
        }
        if (dineroOfrecido > 0 && jugadorPropone.getFortuna() < dineroOfrecido) {
            System.out.printf("El trato no puede ser aceptado: %s no dispone de suficiente dinero.\n",
                    jugadorPropone.getNombre());
            return false;
        }
        for (Casilla propiedad : propiedadesOfrecidas) {
            if(propiedad instanceof Propiedad){

                if (!((Propiedad)propiedad).getDuenho().getNombre().equals( this.jugadorPropone.getNombre()) ) {
                    System.out.printf("El trato no puede ser aceptado: %s no pertenece a %s.\n",
                            propiedad.getNombre(), jugadorPropone.getNombre());
                    return false;
                }
            }
        }
        // Verificar propiedades demandadas
        for (Casilla propiedad : propiedadesDemandadas) {
            if(propiedad instanceof Propiedad){
                if (!((Propiedad)propiedad).getDuenho().getNombre().equals( this.jugadorRecibe.getNombre()) ) {
                    System.out.printf("El trato no puede ser aceptado: %s no pertenece a %s.\n",
                            propiedad.getNombre(), jugadorRecibe.getNombre());
                    return false;
                }
            }
        }
        // Transferencia de propiedades y dinero
        transferirPropiedades(propiedadesOfrecidas, jugadorPropone, jugadorRecibe);
        transferirPropiedades(propiedadesDemandadas, jugadorRecibe, jugadorPropone);
        transferirDinero(dineroOfrecido, jugadorPropone, jugadorRecibe);
        transferirDinero(dineroDemandado, jugadorRecibe, jugadorPropone);

        System.out.printf("Se ha aceptado el trato con éxito entre %s y %s.\n",
                jugadorPropone.getNombre(), jugadorRecibe.getNombre());
        return true;
    }

    /**
     * Transfiere propiedades entre dos jugadores.
     */
    private void transferirPropiedades(ArrayList<Casilla> propiedades, Jugador de, Jugador a) {
        Iterator<Casilla> iterator = propiedades.iterator();
        while (iterator.hasNext()) {
            Casilla propiedad = iterator.next();
            de.eliminarPropiedad(propiedad);
            a.anhadirPropiedad(propiedad);
        }
    }

    /**
     * Transfiere dinero entre dos jugadores.
     */
    private void transferirDinero(float cantidad, Jugador de, Jugador a) {
        if (cantidad > 0) {
            de.restarFortuna(cantidad);
            a.sumarFortuna(cantidad);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");
        sb.append(" id: ").append(this.id).append(",\n");
        sb.append(" jugadorPropone: ").append(this.jugadorPropone.getNombre()).append(",\n");
        sb.append(" trato: cambiar (");

        // Añadir propiedades ofrecidas
        if (!this.propiedadesOfrecidas.isEmpty()) {
            for (int i = 0; i < propiedadesOfrecidas.size(); i++) {
                sb.append(propiedadesOfrecidas.get(i).getNombre());
                if (i < propiedadesOfrecidas.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        // Añadir dinero ofrecido
        if (dineroOfrecido > 0) {
            if (!this.propiedadesOfrecidas.isEmpty()) {
                sb.append(", ");
            }
            sb.append(String.format("%,.2f€", dineroOfrecido));
        }

        sb.append(") por (");

        // Añadir propiedades demandadas
        if (!this.propiedadesDemandadas.isEmpty()) {
            for (int i = 0; i < propiedadesDemandadas.size(); i++) {
                sb.append(propiedadesDemandadas.get(i).getNombre());
                if (i < propiedadesDemandadas.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        // Añadir dinero demandado
        if (dineroDemandado > 0) {
            if (!this.propiedadesDemandadas.isEmpty()) {
                sb.append(", ");
            }
            sb.append(String.format("%,.2f€", dineroDemandado));
        }

        sb.append(")\n");
        sb.append("}");

        return sb.toString();
    }
}