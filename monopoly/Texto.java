package monopoly;

public class Texto {

    // Petadinha: texto dentro del tablero
    // No sé si es buena práctica pero los valores se pueden modificar desde otra clase sin setter (de hecho lo hago)
    public static final String[] TABLERO = new String[17];

    // MENSAJES (EMPIEZAN POR "M_")-------------------------------------------------------------------------------------
    // Algunos mensajes de texto muy largos los pongo aquí pa hacer más legible el código (principalmente Menu.java)
    // [1] Mensajes de antes de empezar la partida
    public static final String M_PROHIBIDO_MAS_DE_6_JUGADORES = "¡No se pueden crear más de 6 jugadores! " +
            "Empieza la partida con el comando " + Valor.BOLD_STRING + "empezar partida" + Valor.RESET + ".";
    public static final String M_COMANDO_INVALIDO_INICIO = "Usa " + Valor.BOLD_STRING + "crear jugador <tuNombre> " +
            "<tipoJugador>" + Valor.RESET + " o introduce " + Valor.BOLD_STRING + "empezar partida" + Valor.RESET +
            " si ya no quieres crear más.";
    public static final String M_TIPO_AVATAR_INVALIDO = "Tipo de avatar inválido. Los tipos de avatares son: " +
            Valor.BOLD_STRING + "coche, pelota, sombrero y esfinge" + Valor.RESET;
    public static final String M_EMPIEZA_LA_PARTIDA = "¡Que comienze la partida!\nEs el turno de %s. " +
            "Puedes tirar los dados con el comando " + Valor.BOLD_STRING + "lanzar dados" + Valor.RESET + ".";
    // [2] Mensajes sobre dados y movimiento
    public static final String M_RECIEN_ENCARCELADO = "Acabas de ir a la cárcel, no puedes volver a tirar " +
            "hasta el siguiente turno.\nSi no tienes nada más que hacer usa el comando " +
            Valor.BOLD_STRING + "acabar turno" + Valor.RESET;
    public static final String M_YA_SE_TIRO = "¡Ya has tirado! Si no tienes nada más que hacer usa el comando " +
            Valor.BOLD_STRING + "acabar turno" + Valor.RESET;
    public static final String M_YA_SE_HICIERON_TODOS_LOS_MOVIMIENTOS_TIRADA = "Ya has realizado todos los movimientos" +
            " de la tirada. Pero como sacaste dobles vuelves a tirar.";
    public static final String M_YA_SE_HICIERON_TODOS_LOS_MOVIMIENTOS_TURNO = "Ya has realizado todos los movimientos" +
            " de este turno. Si no tienes nada más que hacer usa el comando " + Valor.BOLD_STRING + "acabar turno" +
            Valor.RESET;
    public static final String M_ACTIVAR_MOVIMIENTO_AVANZADO = "El avatar %s activa al movimiento avanzado (tipo %s).";
    public static final String M_COMANDO_BLOQUEADO = "No se puede usar ese comando en este momento.";
    public static final String M_MOVIMIENTOS_PENDIENTES = "Aún tienes movimientos pendientes! Introduce " +
            Valor.BOLD_STRING + "siguiente" + Valor.RESET + " para desplazarte hasta la siguiente casilla :)";
    public static final String M_DOS_TURNOS_SIN_TIRAR = "Has sacado menos que 5... " +
            "No puedes volver a tirar ni en este turno ni en los próximos dos ⛓\uFE0F\uD83E\uDD40⛓\uFE0F";
    // [3] Otros]
    public static final String M_UNA_CASILLA_POR_TURNO = "Sólo puedes comprar una casilla por turno en el modo " +
            "avanzado del coche.";
    public static final String M_NO_HAY_DINERO_SUFICIENTE = "No tienes dinero suficiente para pagar el alquiler. " +
            "Tienes que hipotecar tus propiedades hasta tener dinero.\nPara ello usa el comando " + Valor.BOLD_STRING +
            "hipotecar <nombre_propiedad>" + Valor.RESET + ".";


    // TEXTO QUE SE METE EN EL MEDIO DEL TABLERO------------------------------------------------------------------------
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


    // VALORES PARA IMPRIMIR LAS CARTAS---------------------------------------------------------------------------------
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


