package excepciones.accionNoValida;

import excepciones.Excepcion;

// Subclase: ComandoImposibleExcepcion
public abstract class AccionNoValida extends Excepcion {
    protected AccionNoValida(String mensaje) {
        super("No es posible usar:"+ mensaje);
    }
}
