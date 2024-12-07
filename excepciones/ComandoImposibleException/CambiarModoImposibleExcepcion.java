package excepciones.ComandoImposibleException;

public class CambiarModoImposibleExcepcion extends ComandoImposibleExcepcion {
    public CambiarModoImposibleExcepcion() {
        super("No se puede cambiar de modo una vez ya se ha tirado.");
    }
}