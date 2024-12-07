package excepciones.ComandoImposibleException;

import excepciones.Excepciones;

// Subclase: ComandoImposibleExcepcion
public class ComandoImposibleExcepcion extends Excepciones {
    public ComandoImposibleExcepcion(String mensaje) {
        super("Tipp Imposible:"+ mensaje);
    }
}
