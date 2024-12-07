package monopoly;

import partida.Jugador;

import java.util.ArrayList;
import java.util.Iterator;

import monopoly.casillas.propiedades.Propiedad;
import monopoly.*;

public class Trato {

    // Atributos de la clase
    private static int contadorTratos = 0; // Generador de ID único
    private String id; // Identificador único del trato
    private Jugador jugadorPropone; // Jugador que propone el trato
    private Jugador jugadorRecibe; // Jugador que recibe el trato
    private ArrayList<Propiedad> propiedadesOfrecidas; // Propiedades que ofrece el jugador que propone
    private ArrayList<Propiedad> propiedadesDemandadas; // Propiedades que solicita el jugador que propone
    private float dineroOfrecido; // Dinero que ofrece el jugador que propone
    private float dineroDemandado; // Dinero que solicita el jugador que propone


    // Constructor
    public Trato(Jugador jugadorPropone, Jugador jugadorRecibe,
        ArrayList<Propiedad> propiedadesOfrecidas, ArrayList<Propiedad> propiedadesDemandadas,
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

    public ArrayList<Propiedad> getPropiedadesOfrecidas() {
        return propiedadesOfrecidas;
    }

    public ArrayList<Propiedad> getPropiedadesDemandadas() {
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
            Juego.consola.imprimir("El trato no puede ser aceptado: " + jugadorRecibe.getNombre() + " no dispone de suficiente dinero.\n");
            return false;
        }
        if (dineroOfrecido > 0 && jugadorPropone.getFortuna() < dineroOfrecido) {
            Juego.consola.imprimir("El trato no puede ser aceptado: " + jugadorPropone.getNombre() + " no dispone de suficiente dinero.\n");
            return false;
        }
        for (Propiedad propiedad : propiedadesOfrecidas) {
        

            if (!((Propiedad)propiedad).getDuenho().getNombre().equals( this.jugadorPropone.getNombre()) ) {
                Juego.consola.imprimir(String.format("El trato no puede ser aceptado: %s no pertenece a %s.\n",
                        propiedad.getNombre(), jugadorPropone.getNombre()));
                return false;
            }
        }
        // Verificar propiedades demandadas
        for (Propiedad propiedad : propiedadesDemandadas) {

            if (!((Propiedad)propiedad).getDuenho().getNombre().equals( this.jugadorRecibe.getNombre()) ) {
                Juego.consola.imprimir("El trato no puede ser aceptado: " + propiedad.getNombre() + " no pertenece a " + jugadorRecibe.getNombre() + ".\n");
                return false;
            }
        }
        Juego.consola.imprimir("Detalles del trato:\n");
        Juego.consola.imprimir(jugadorPropone.getNombre() + " DA:\n");
        for (Propiedad propiedad : propiedadesOfrecidas) {
            Juego.consola.imprimir("- Propiedad: " + propiedad.getNombre() + "\n");
        }
        if (dineroOfrecido > 0) {
            Juego.consola.imprimir("- Dinero: " + dineroOfrecido + "€\n");
        }

        Juego.consola.imprimir(jugadorRecibe.getNombre() + " DA:\n");
        for (Propiedad propiedad : propiedadesDemandadas) {
            Juego.consola.imprimir("- Propiedad: " + propiedad.getNombre() + "\n");
        }
        if (dineroDemandado > 0) {
            Juego.consola.imprimir("- Dinero: " + dineroDemandado + "€\n");
        }
        // Transferencia de propiedades y dinero
        transferirPropiedades(propiedadesOfrecidas, jugadorPropone, jugadorRecibe);
        transferirPropiedades(propiedadesDemandadas, jugadorRecibe, jugadorPropone);
        transferirDinero(dineroOfrecido, jugadorPropone, jugadorRecibe);
        transferirDinero(dineroDemandado, jugadorRecibe, jugadorPropone);

        Juego.consola.imprimir("Se ha aceptado el trato con éxito entre " + jugadorPropone.getNombre() + " y " + jugadorRecibe.getNombre() + ".\n");
        return true;
    }

    /**
     * Transfiere propiedades entre dos jugadores.
     */
    private void transferirPropiedades(ArrayList<Propiedad> propiedades, Jugador de, Jugador a) {
        Iterator<Propiedad> iterator = propiedades.iterator();
        while (iterator.hasNext()) {
            Propiedad propiedad = iterator.next();
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