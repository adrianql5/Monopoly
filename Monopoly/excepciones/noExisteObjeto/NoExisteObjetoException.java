package excepciones.noExisteObjeto;

import excepciones.Excepcion;

// Subclase: NoExisteExcepcion
public class NoExisteObjetoException extends Excepcion {
    protected NoExisteObjetoException(String mensaje) {
        super("Tipo NoExiste:"+mensaje);
    }
}


