package excepciones.noExisteObjeto;

public class NoExisteJugadorException extends NoExisteObjetoException {
    public NoExisteJugadorException(String mensaje) {
        super(mensaje);
    }
}