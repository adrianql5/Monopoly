package excepciones.noExisteObjeto;

public class NoExisteComandoException extends NoExisteObjetoException {
    public NoExisteComandoException(String mensaje) {
        super(mensaje);
    }
}