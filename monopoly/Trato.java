package monopoly;

import partida.Jugador;


import monopoly.casillas.propiedades.Propiedad;

public class Trato {

    // Atributos de la clase
    private static int contadorTratos = 0; // Generador de ID único
    private String id; // Identificador único del trato
    private Jugador jugadorPropone; // Jugador que propone el trato
    private Jugador jugadorRecibe; // Jugador que recibe el trato
    private Propiedad propiedadOfrecida; // Propiedad que ofrece el jugador que propone
    private Propiedad propiedadDemandada; // Propiedad que solicita el jugador que propone
    private float dineroOfrecido; // Dinero que ofrece el jugador que propone
    private float dineroDemandado; // Dinero que solicita el jugador que propone

    // Constructor
    public Trato(Jugador jugadorPropone, Jugador jugadorRecibe,
                 Propiedad propiedadOfrecida, Propiedad propiedadDemandada,
                 float dineroOfrecido, float dineroDemandado) {

        this.id = "trato" + (++contadorTratos); // Asigna un ID único
        this.jugadorPropone = jugadorPropone;
        this.jugadorRecibe = jugadorRecibe;
        this.propiedadOfrecida = propiedadOfrecida;
        this.propiedadDemandada = propiedadDemandada;
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

    public Propiedad getPropiedadOfrecida() {
        return propiedadOfrecida;
    }

    public Propiedad getPropiedadDemandada() {
        return propiedadDemandada;
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
    public boolean esValido() {
        boolean propiedadPorPropiedad = propiedadOfrecida != null && propiedadDemandada != null
                && dineroOfrecido == 0 && dineroDemandado == 0;
        boolean propiedadPorDinero = propiedadOfrecida != null && propiedadDemandada == null
                && dineroDemandado > 0 && dineroOfrecido == 0;
        boolean dineroPorPropiedad = propiedadOfrecida == null && propiedadDemandada != null
                && dineroOfrecido > 0 && dineroDemandado == 0;
        boolean propiedadPorPropiedadYDinero = propiedadOfrecida != null && propiedadDemandada != null
                && dineroDemandado > 0 && dineroOfrecido == 0;
        boolean propiedadYDineroPorPropiedad = propiedadOfrecida != null && propiedadDemandada != null
                && dineroOfrecido > 0 && dineroDemandado == 0;

        return propiedadPorPropiedad || propiedadPorDinero || dineroPorPropiedad
                || propiedadPorPropiedadYDinero || propiedadYDineroPorPropiedad;
    }


    private boolean confirmarPropiedadHipotecada(Propiedad propiedad) {
        Juego.consola.imprimir("La propiedad " + propiedad.getNombre() + " está hipotecada. ¿Quieres aceptar el trato de todas formas? (Si/No)\n");
        String respuesta = Juego.consola.leer("¿Seguro que aceptas el trato?").trim();
        return respuesta.equalsIgnoreCase("Si");
    }

    public boolean aceptar() {
        if (!esValido()) {
            Juego.consola.imprimir("El trato no es válido y no puede ser aceptado.\n");
            return false;
        }

        // Verificar dinero
        if (dineroDemandado > 0 && jugadorRecibe.getFortuna() < dineroDemandado) {
            Juego.consola.imprimir("El trato no puede ser aceptado: " + jugadorRecibe.getNombre() + " no dispone de suficiente dinero.\n");
            return false;
        }
        if (dineroOfrecido > 0 && jugadorPropone.getFortuna() < dineroOfrecido) {
            Juego.consola.imprimir("El trato no puede ser aceptado: " + jugadorPropone.getNombre() + " no dispone de suficiente dinero.\n");
            return false;
        }

        // Verificar propiedades
        if (propiedadOfrecida != null && !propiedadOfrecida.getDuenho().equals(jugadorPropone)) {
            Juego.consola.imprimir("El trato no puede ser aceptado: " + propiedadOfrecida.getNombre() + " no pertenece a " + jugadorPropone.getNombre() + ".\n");
            return false;
        }
        if (propiedadDemandada != null && !propiedadDemandada.getDuenho().equals(jugadorRecibe)) {
            Juego.consola.imprimir("El trato no puede ser aceptado: " + propiedadDemandada.getNombre() + " no pertenece a " + jugadorRecibe.getNombre() + ".\n");
            return false;
        }

        if (propiedadOfrecida != null && propiedadOfrecida.estaHipotecada()) {
            if (!confirmarPropiedadHipotecada(propiedadOfrecida)) return false;
        }
        if (propiedadDemandada != null && propiedadDemandada.estaHipotecada()) {
            if (!confirmarPropiedadHipotecada(propiedadDemandada)) return false;
        }
        // Realizar el intercambio
        if (propiedadOfrecida != null) {
            transferirPropiedad(propiedadOfrecida, jugadorPropone, jugadorRecibe);
        }
        if (propiedadDemandada != null) {
            transferirPropiedad(propiedadDemandada, jugadorRecibe, jugadorPropone);
        }
        transferirDinero(dineroOfrecido, jugadorPropone, jugadorRecibe);
        transferirDinero(dineroDemandado, jugadorRecibe, jugadorPropone);


        return true;
    }

    private void transferirPropiedad(Propiedad propiedad, Jugador de, Jugador a) {
        de.eliminarPropiedad(propiedad);
        a.anhadirPropiedad(propiedad);
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
        sb.append(" trato: cambiar ");

        if (propiedadOfrecida != null) {
            sb.append(propiedadOfrecida.getNombre());
        }
        if (dineroOfrecido > 0) {
            if (propiedadOfrecida != null) {
                sb.append(" y ");
            }
            sb.append(String.format("%,.2f€", dineroOfrecido));
        }

        sb.append(" por ");

        if (propiedadDemandada != null) {
            sb.append(propiedadDemandada.getNombre());
        }
        if (dineroDemandado > 0) {
            if (propiedadDemandada != null) {
                sb.append(" y ");
            }
            sb.append(String.format("%,.2f€", dineroDemandado));
        }

        sb.append("\n}");
        return sb.toString();
    }
}


