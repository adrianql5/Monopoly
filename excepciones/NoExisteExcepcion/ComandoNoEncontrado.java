package excepciones.NoExisteExcepcion;

public class ComandoNoEncontrado extends NoExisteExcepcion {
    public ComandoNoEncontrado() {
        super("El comando solicitado no fue encontrado.");
    }
}