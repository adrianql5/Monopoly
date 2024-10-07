package partida;

import java.util.Random;

public class Dado {
    //El dado solo tiene un atributo en nuestro caso: su valor.
    private int valor;

    private Random numeroRandom = new Random();
    /**Metodo para simular lanzamiento de un dado: devolverá un valor aleatorio entre 1 y 6.*/
    public int tirarDado() {
        valor= numeroRandom.nextInt(1,7);
        return valor;
    }

    public int getValor(){
        return this.valor;
    }
}
