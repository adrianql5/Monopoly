package excepciones.noExisteObjeto;

public class NoExisteComandoException extends NoExisteObjetoException {
    public NoExisteComandoException() {
        super("El comando solicitado no fue encontrado.");
    }
}