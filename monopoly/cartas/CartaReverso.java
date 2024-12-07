package monopoly.cartas;

import java.util.ArrayList;

import monopoly.Texto;
import monopoly.Valor;

// No es subclase de carta porque habría que crear otra clase abstracta pa diferenciar las cartas que hacen algo de esta

public class CartaReverso {

    // ATRIBUTOS
    protected final ArrayList<String> texto;
    protected final int ID;

    // CONSTRUCTOR
    public CartaReverso() {
        this.texto = new ArrayList<String>();

        this.texto.add(Texto.CARTA_BORDESUP);

        // Parte del medio (en total 9 líneas)
        for(int i=0; i<9; i++) {
            this.texto.add(Texto.CARTA_REVERSO);
        }

        this.texto.add(Texto.CARTA_BORDEINF);

        //Inicializamos el resto de atributos a unos valores por defecto
        this.ID = 0;
    }

}
