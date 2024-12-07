package excepciones.NoExisteExcepcion;

import excepciones.Excepciones;

// Subclase: NoExisteExcepcion
public class NoExisteExcepcion extends Excepciones {
    public NoExisteExcepcion(String mensaje) {
        super("Tipo NoExiste:"+mensaje);
    }
}


