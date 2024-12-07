package excepciones;

public class Excepciones extends Exception {


    public Excepciones(String mensaje) {
        super("Excepcion " +mensaje);
    }
}
