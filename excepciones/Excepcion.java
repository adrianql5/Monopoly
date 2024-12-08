package excepciones;

public abstract class Excepcion extends Exception {
    protected Excepcion(String mensaje) {
        super("Excepcion " +mensaje);
    }
}
