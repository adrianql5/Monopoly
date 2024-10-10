package monopoly;


public class Valor {
    //Estos 3 venían dados
    public static final float FORTUNA_BANCA = 372642563;
    public static final float FORTUNA_INICIAL = 9543076.28f;
    public static final float SUMA_VUELTA = 1301328.584f; //Se aproxima a la media de los precios de los solares del tablero.

    //Colores del texto:
    //Curiosidad: \u001B equivale a 1 caracter (ESC)
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    //A PARTIR DE AQUÍ TODO LO AÑADIMOS NOSOTROS
    public static final String BOLD_STRING = "\u001B[1m";
    public static final String SUBRAYADO = "\u001B[4m";

    //VALORES CONSTANTES DURANTE TODA LA PARTIDA
    //Información importante: aunque sea una fórmula sólo se calcula una vez, no cada vez que se accede
    public static final float SALIR_CARCEL = 0.25f * Valor.SUMA_VUELTA;

    //PRECIO DE COMPRA DE LAS CASILLAS DE TIPO TRANSPORTE Y SERVICIO
    public static final float TRANSPORTE = Valor.SUMA_VUELTA;   //Podría quitarse pero sería menos intuitivo
    public static final float SERVICIO = 0.75f * Valor.SUMA_VUELTA;

    //VALORES INICIALES DE LOS GRUPOS DE LOS SOLARES
    public static final float INCREMENTO = 1.3f;
    public static final float GRUPO1 = 1200000f;
    public static final float GRUPO2 = Valor.GRUPO1 * Valor.INCREMENTO;
    public static final float GRUPO3 = Valor.GRUPO2 * Valor.INCREMENTO;
    public static final float GRUPO4 = Valor.GRUPO3 * Valor.INCREMENTO;
    public static final float GRUPO5 = Valor.GRUPO4 * Valor.INCREMENTO;
    public static final float GRUPO6 = Valor.GRUPO5 * Valor.INCREMENTO;
    public static final float GRUPO7 = Valor.GRUPO6 * Valor.INCREMENTO;
    public static final float GRUPO8 = Valor.GRUPO7 * Valor.INCREMENTO;

    //Constantes auxiliares para imprimir la tabla:
    /**Número de casillas por fila (incluyendo ambas esquinas).*/
    public static final int NCASILLAS_POR_FILA = 11;
    /**Número de caracteres de una casilla (SIN CONTAR EL BORDE DERECHO).
     *Se decide por la longitud del nombre más largo (IrCarcel)
     */
    public static final int NCHARS_CASILLA = 8;
    public static final String BARRA = "|";     //No es necesario pero por si queremos cambiarlo en algún momento
    public static final String CASILLA_VACIA = "        ";

    //Algunas cadenas que pueden ser útiles
    public static final String TEXTO_BIENVENIDA =
            "Bienvenido al Monopoly! \uD83E\uDD11\n" +
            "Vamos a crear los jugadores de la partida. Usa el comando\n" +
            "    crear jugador <tuNombre> <tipoJugador>\n" +
            "Los tipos de jugadores son: coche, esfinge, pelota, sombrero.\n" +
            "Cuando hayas creado todos introduce:\n" +
            "    empezar partida\n";

    public static final String LISTA_COMANDOS =
            "LISTA DE COMANDOS:\n" +
            ">jugador\n" +
            ">lanzar dados\n" +
            ">comprar <Casilla>\n" +
            ">acabar turno\n" +
            ">listar jugadores\n" +
            ">listar avatares\n" +
            ">listar enventa\n" +
            ">describir <Casilla>\n" +
            ">describir jugador <Nombre>\n" +
            ">describir avatar <ID>\n" +
            ">ver tablero\n";

    //Petadinha: texto dentro del tablero
    //No sé si es buena práctica pero los valores se pueden modificar desde otra clase
    public static final String[] TEXTO_TABLERO = new String[17];
}
