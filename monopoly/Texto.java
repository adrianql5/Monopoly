package monopoly;

public class Texto {

    // Petadinha: texto dentro del tablero
    // No sé si es buena práctica pero los valores se pueden modificar desde otra clase sin setter (de hecho lo hago)
    public static final String[] TABLERO = new String[17];


    // TEXTO QUE SE METE EN EL MEDIO DEL TABLERO
    public static final String BIENVENIDA = "Bienvenido al Monopoly! \uD83E\uDD11\n" +
                    "Vamos a crear los jugadores de la partida. Usa el comando\n" +
                    "    crear jugador <tuNombre> <tipoJugador>\n" +
                    "Los tipos de jugadores son: coche, esfinge, pelota, sombrero.\n" +
                    "Cuando hayas creado todos introduce:\n" +
                    "    empezar partida\n";

    public static final String LISTA_COMANDOS = "LISTA DE COMANDOS:\n" +
                    ">jugador\n" +
                    ">lanzar dados\n" +
                    ">comprar <Casilla>\n" +
                    ">acabar turno\n" +
                    ">listar jugadores / avatares / enventa\n" +
                    ">describir <Casilla>\n" +
                    ">describir jugador <Nombre>\n" +
                    ">describir avatar <ID>\n" +
                    ">edificar <casa, hotel, piscina, pista deportes>\n"+
                    ">listar edificios <color grupo (opcional)>\n"+
                    ">hipotecar / deshipotecar <Casilla>\n"+
                    ">vender <tipo edificio> <Casilla> <Nº edificios>\n"+
                    ">estadisticas <Jugador(opcional)>\n"+
                    ">cambiar modo\n"+ 
                    ">ver tablero\n";


    // VALORES PARA IMPRIMIR LAS CARTAS
    // Mensajes de cada carta (el más largo es CAJA 5 -> 140 caracteres)
    // IMPORTANTE: si en la carta no pone que vas a la casilla no se mueve al jugador
    public static final String CARTA_SUERTE_1 = "Ve a Transportes1 y coge un avión. Si pasas por la casilla de " +
            "Salida cobra la cantidad habitual";
    public static final String CARTA_SUERTE_2 = "Decides hacer un viaje de placer. Avanza hasta Solar15 " +
            "directamente, sin pasar por la casilla de Salida y sin cobrar la cantidad habitual";
    public static final String CARTA_SUERTE_3 = "Vendes tu billete de avión para Solar17 " +
            "en una subasta por Internet. Cobra 500.000€";
    public static final String CARTA_SUERTE_4 = "Ve a Solar3. Si pasas por la casilla de Salida cobra la cantidad habitual";
    public static final String CARTA_SUERTE_5 = "Los acreedores te persiguen por impago. Ve a la Cárcel. Ve " +
            "directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual";
    public static final String CARTA_SUERTE_6 = "¡Has ganado el bote de la lotería! Recibe 1.000.000€";
    public static final String CARTA_CAJA_1 = "Paga 500.000€ por un fin de semana en un balneario de 5 estrellas";
    public static final String CARTA_CAJA_2 = "Te investigan por fraude de identidad. Ve a la Cárcel. " +
            "Ve directamente sin pasar por la casilla de Salida y sin cobrar la cantidad habitual";
    public static final String CARTA_CAJA_3 = "Colócate en la casilla de Salida y cobra la cantidad habitual";
    public static final String CARTA_CAJA_4 = "Tu compañía de Internet obtiene beneficios. Recibe 2.000.000€";
    public static final String CARTA_CAJA_5 = "Paga 1.000.000€ por invitar a todos tus amigos a un viaje a Solar14";
    public static final String CARTA_CAJA_6 = "Alquilas a tus compañeros una villa en Solar7 durante una semana. " +
            "Paga 200.000€ a cada jugador";

    // Caracteres específicos del formato de imprimir las cartas
    public static final String CARTA_BORDESUP = "╔═════════════════╗";
    public static final String CARTA_REVERSO =  "║ ░░░░░░░░░░░░░░░ ║";
    public static final String CARTA_BORDEINF = "╚═════════════════╝";
    public static final String CARTA_LINEAVACIA =  "║                 ║";
    public static final String CARTA_BORDE = "║";

}
