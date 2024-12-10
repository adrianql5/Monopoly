package excepciones.noExisteObjeto;

public class NoExisteAvatarException extends NoExisteObjetoException {
    public NoExisteAvatarException(String mensaje) {
        super(mensaje);
    }
}