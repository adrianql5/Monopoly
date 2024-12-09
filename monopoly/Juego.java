package monopoly;

import java.util.*;

import excepciones.accionNoValida.*;
import excepciones.noExisteObjeto.*;
import partida.*;
import partida.avatares.*;

import monopoly.casillas.*;
import monopoly.casillas.propiedades.*;
import monopoly.edificios.*;
import monopoly.interfaces.*;



public class Juego implements Comandos {
    // ATRIBUTOS
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno; //Índice de la posición en el arrayList del jugador (y el avatar) que tienen el turno.
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.

    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente (no tiene deudas).
    private boolean turno_extra_coche; //turno extra coche

    private boolean partidaTerminada; //Booleano para acabar la partida
    private int dadosDoblesSeguidos; //(DEL JUGADOR CON EL TURNO) Hay que diferenciar lanzamientos de este atributo

    /**
     * Atributo que usa analizarComando() para saber qué comandos bloquear.
     * / 0->turno normal.
     * / 1->pelota avanzado cuando tira y tiene movimientos pendientes.
     * / 2->coche avanzado no puede mover este turno.
     * / 3->coche avanzado ya compró una propiedad.
     */
    private int controlComandos;

    public final static Consola consola = new ConsolaNormal();

    // SECCIÓN DE CONSTRUCTOR DE JUEGO----------------------------------------------------------------------------------

    public Juego() {
        this.jugadores = new ArrayList<Jugador>();
        this.avatares = new ArrayList<Avatar>();
        this.turno = -1; //Aún no ha empezado la partida, cuando se crea el primer jugador ya se pone a 0
        this.lanzamientos = 0;
        this.banca = new Jugador();
        this.tablero = new Tablero(this.banca);
        this.dado1 = new Dado();
        this.dado2 = new Dado();
        this.tirado = false;
        this.solvente = true;
        this.turno_extra_coche = false;

        this.partidaTerminada = false;
        this.dadosDoblesSeguidos = 0;

        this.controlComandos = 0;
    }


    //SECCIÓN DE CONTROL DEL FLUJO DE LA PARTIDA------------------------------------------------------------------------

    /**
     * Método principal del Monopoly.
     * [1] Mensaje de bienvenida y explicación del comando para crear jugadores.
     * [2] Pide crear jugadores hasta tener una cantidad válidad para empezar (2-6).
     * [3] Al introducirse el comando 'empezar partida' se llama a bucleTurno.
     * [4] bucleTurno gestiona todo el transcurso de la partida.
     * [5] Cuando se sale de bucleTurno es porque la partida ha terminado.
     * [6] Mensaje de final de partida y cerramos todo lo que toca.
     */
    public void iniciarPartida()   {
        // Creamos un scanner para introducir comandos

        // Antes de empezar la partida hay que crear los jugadores
        setTextoTablero(Texto.BIENVENIDA);
        verTablero();

        // Bucle para crear los jugadores (máximo 6)
        // Dentro del propio bucle se empieza la partida
        while (!partidaTerminada) {

            String comando_entero = consola.leer(Texto.M_COMANDO_INVALIDO_INICIO);
            String[] comando = comando_entero.split(" ");

            // IMPORTANTE comprobar la longitud para no acceder a un índice que no existe
            if (comando.length >= 4 && "crearjugador".equals(comando[0] + comando[1])) {
                // Si no tenemos ya 6 jugadores se deja crear un nuevo jugador
                if (this.jugadores.size() < 6) {
                    // Comprobamos si se ha introducido un tipo de avatar válido
                    if (esTipoAvatar(comando[comando.length - 1])) {
                        // El nombre del jugador puede contener varias palabras
                        String nombre_completo = "";
                        for (int i = 2; i < comando.length - 1; i++) {
                            // Quitando la primera, cada vez que se añada una palabra tiene que haber un espacio
                            if (i > 2) {
                                nombre_completo += " ";
                            }
                            nombre_completo += comando[i];
                        }
                        // Creamos el jugador ahora que ya tenemos el nombre completo
                        crearJugador(nombre_completo, comando[comando.length - 1]);
                    } else {
                        consola.imprimir(Texto.M_TIPO_AVATAR_INVALIDO);
                    }
                } else {
                    consola.imprimir(Texto.M_PROHIBIDO_MAS_DE_6_JUGADORES);
                }
            } else if (comando.length == 2 && comando[0].equals("setfortuna")) {
                float fortuna = Float.parseFloat(comando[1]);
                asignarFortuna(fortuna);
            } else if ("empezar partida".equals(comando_entero)) {

                //Si hay al menos 2 jugadores empezamos
                if (this.avatares.isEmpty()) {
                    consola.imprimir("Amig@ habrá que crear algún jugador antes de empezar no crees?");
                } else if (this.avatares.size() != 1) {
                    this.turno = 0; //El primer jugador creado tiene el turno

                    // Avisamos del inicio de la partida y cambiamos el texto dentro del tablero
                    System.out.printf(Texto.M_EMPIEZA_LA_PARTIDA + "\n", obtenerTurno().getNombre());
                    setTextoTablero(Texto.LISTA_COMANDOS);

                    //Este es el bucle de la partida básicamente: cada iteración es un turno
                    while (!this.partidaTerminada) {
                        bucleTurno();
                    }

                } else {
                    consola.imprimir("Creo que jugar una persona sola no tiene mucho sentido...");
                }

            }

        }
        //Acabouse, liberar memoria estaría duro

        consola.imprimir("La partida ha finalizado, esperamos que disfrutáseis la experiencia.");
        System.exit(0);
    }

    /**
     * Método que realiza las acciones asociadas al comando 'crear jugador'.
     * Solo se usa antes de empezar la partida, una vez empezada no se pueden crear más jugadores.
     *
     * @param nombre     Nombre del jugador
     * @param tipoAvatar Tipo de avatar
     */
    @Override
    public void crearJugador(String nombre, String tipoAvatar) {
        // Lo primero que hacemos es crear el avatar, así si el tipo es incorrecto no hace nada la función
        Avatar avatar;
        Casilla casillaInicio = this.tablero.getCasilla(0);
        switch (tipoAvatar) {
            case "coche":
                avatar = new Coche(null, casillaInicio, this.avatares);
                break;
            case "pelota":
                avatar = new Pelota(null, casillaInicio, this.avatares);
                break;

            case "esfinge":
                avatar = new Esfinge(null, casillaInicio, this.avatares);
                break;

            case "sombrero":
                avatar = new Sombrero(null, casillaInicio, this.avatares);
                break;
            default:
                System.out.print("Tipo de avatar inválido. Los tipos válidos son coche, pelota, esfinge y sombrero");
                return;
        }

        // El tipo de avatar es válido, creamos el jugador
        Jugador nuevoJugador = new Jugador(nombre, avatar);
        // Enlazamos el jugador con el avatar
        avatar.setJugador(nuevoJugador);

        // ESTO NO HACE FALTA @ADRI
        nuevoJugador.setDeudaConJugador(banca);

        // Añadir el jugador a la lista de jugadores y a la casilla de inicio
        this.jugadores.add(nuevoJugador);
        casillaInicio.anhadirAvatar(nuevoJugador.getAvatar());

        // Hacemos que sea el turno del jugador recién creado
        // Nótese que por defecto this.turno = -1 así que cuando se crea el primer jugador el índice es 0.
        this.turno += 1;

        // Imprimir detalles del jugador recién creado y el tablero
        infoJugadorTurno();
        verTablero();
    }

    /**
     * Método que gestiona el desarrollo de un turno
     * [1] Primero comprueba el atributo movimientos_pendientes del Menú por si estamos en un estado especial.
     * [2] Caso especial 1: un avatar tipo coche no puede tirar los dados este turno.
     * [3] Caso especial 2: a un avatar tipo pelota le faltan casillas por moverYEvaluar.
     * [4] Una vez gestionados esos casos llama a analizarComando() con el int que toque.
     */
    private void bucleTurno() {
        while (!partidaTerminada) {
            try {
                // Intentar analizar el comando ingresado
                analizarComando(consola.leer("\nIntroduce un comando: "));
            } catch (NoExisteComandoException e) {
                // Manejar la excepción y mostrar el mensaje al usuario
                consola.imprimir(e.getMessage());
            }
        }
    }

    /**
     * Método para acabar la partida cuando alguien gana o no se quiere seguir.
     */
    @Override
    public void acabarPartida() {
        partidaTerminada = true;
    }


    //SECCIÓN DE MÉTODOS ÚTILES DEL MENÚ--------------------------------------------------------------------------------

    /**
     * Método que devuelve el jugador que tiene el turno.
     */
    private Jugador obtenerTurno() {
        return this.jugadores.get(this.turno);
    }

    /**
     * Método para obtener el ArrayList de los movimientos pendientes del jugador actual directamente
     */
    private ArrayList<Integer> movimientosPendientesActual() {
        return obtenerTurno().getMovimientos_pendientes();
    }

    /**
     * Método que elimina al jugador correspondiente de la lista de jugadores.
     * Si se le pasa un jugador que no está en la lista de jugadores no hace nada.
     */
    private void eliminarJugador(Jugador jugador) {
        Iterator<Jugador> iterator = this.jugadores.iterator();
        while (iterator.hasNext()) {
            Jugador j = iterator.next();
            if (j.equals(jugador)) {
                iterator.remove(); // Elimina de manera segura el jugador actual
                // Si eliminamos al último jugador establecemos el turno del primero
                if (this.turno == this.jugadores.size()) {
                    this.turno = 0;
                }
                // Si sólo queda un jugador se acabó la partida!!!
                if (this.jugadores.size() == 1) {
                    consola.imprimir("Increíble! Os ha dado tiempo a acabar una partida de Monopoly!" +
                            "\n¡¡Y el ganador es " + obtenerTurno().getNombre() + "!!");
                    //this.partidaTerminada=true;
                }
            }
        }
    }

    //Se puede hacer desde aquí porque no existe encapsulación de sus elementos al ser un String[]
    /**
     * Método para cambiar el texto que se muestra en medio del tablero.
     * Se presupone que el texto viene ya con los saltos de línea y cumple con el máximo de longitud de cada línea.
     * Si se intenta introducir un texto con más líneas de las posibles salta un error y no hace nada.
     */
    private void setTextoTablero(String nuevo_texto) {
        //Dividimos el String en partes en función de los saltos de línea
        String[] nuevo_texto_tablero = nuevo_texto.split("\n");

        if (nuevo_texto_tablero.length < 17) {
            // Se empieza en el índice 1 porque la primera línea del centro del tablero se deja vacía
            // Nótese que para asignar bien el texto el índice de nuevo_texto_tablero es i-1
            for (int i = 1; i < nuevo_texto_tablero.length + 1; i++) {
                Texto.TABLERO[i] = nuevo_texto_tablero[i - 1];
            }
        } else {
            consola.imprimir("Se ha intentado meter más líneas de las que caben en el medio del tablero.");
        }
    }

    
    //SECCIÓN DE COMANDOS DEL MENÚ--------------------------------------------------------------------------------------

    /**
     * Método que interpreta el comando introducido y toma la accion correspondiente.
     * En función del valor de control puede capar unas funciones u otras.
     *
     * @param comando_entero Línea de comando que introduce el jugador
     */
    private void analizarComando(String comando_entero) throws NoExisteComandoException {

        switch (comando_entero) {
            // PRIMER BLOQUE DE COMANDOS: no dependen de una instancia----------------------------------------

            // Variado
            case "terminar partida":
            case "acabar partida":
                acabarPartida();
                break;
            case "bancarrota":
                declararBancarrota(this.banca);
                break; // Se elimina al jugador de la partida!!
            case "ver tablero":
                verTablero();
                break;
            case "jugador":
                infoJugadorTurno();
                break;
            case "estadisticas":
                estadisticasGenerales();
                break;
            case "salir carcel":
                try {
                    salirCarcel();
                } catch (SalirCarcelException e) {
                    // Manejar la excepción y mostrar el mensaje al usuario
                    consola.imprimir(e.getMessage());
                }
                break;

            case "ayuda":
                ayuda();
                break;
            case "edificar pista de deporte":
            try{
                edificar("pista de deporte");
            } catch (NoExisteEdificioException e) {
                consola.imprimir(e.getMessage());
            }
            break;
            // Comandos de listar cosas
            case "listar enventa":
                listarVenta();
                break;
            case "listar jugadores":
                listarJugadores();
                break;
            case "listar avatares":
                listarAvatares();
                break;
            case "listar edificios":
                listarEdificios(null);
                break;
            // Comando para listar tratos
            case "listar tratos":
                listarTratosJugadorActual();
                break;

            // Comandos que no se pueden ejecutar en ciertos casos-----------------------
            case "lanzar dados":
                if (this.controlComandos == 0 ||
                        this.controlComandos == 2 && this.dado1.getValor() + this.dado2.getValor() > 4) {
                    lanzarDados(0, 0);
                } else {
                    consola.imprimir(Texto.M_COMANDO_BLOQUEADO);
                }
                break;

            case "acabar turno":
                // La pelota no puede pasar el turno si tiene movimientos pendientes
                try {
                    acabarTurno();
                } catch (AcabarTurnoException e) {
                    // Manejar la excepción y mostrar el mensaje al usuario
                    consola.imprimir(e.getMessage());
                }
                break;

            case "cambiar modo":
                try {
                    cambiarModo();
                } catch (CambiarModoException e) {
                    // Manejar la excepción y mostrar el mensaje al usuario
                    consola.imprimir(e.getMessage());
                }
                break;

            // Comando que se usa para ir avanzando en el movimiento con paradas de la pelota
            case "siguiente":
                if (this.controlComandos == 1 && this.tirado && !movimientosPendientesActual().isEmpty()) {
                    // Cuando este comando tiene sentido hay movimientos pendientes del mismo turno, pues movemos
                    moverYEvaluar(obtenerTurno(), movimientosPendientesActual().get(0));
                    verTablero();
                    if (movimientosPendientesActual().isEmpty()) {
                        if (this.dado1.getValor() == this.dado2.getValor()) {
                            consola.imprimir(Texto.M_YA_SE_HICIERON_LOS_MOVIMIENTOS_TIRADA);
                        } else {
                            consola.imprimir(Texto.M_YA_SE_HICIERON_LOS_MOVIMIENTOS_TURNO);
                        }
                    }
                } else {
                    consola.imprimir(Texto.M_COMANDO_BLOQUEADO);
                }
                break;


            // CHEATS----------------------------------------------------------
            // DINERO INFINITO (+mil millones)
            case "dinero infinito":
                dineroInfinito();
                break;
            // AVANZAR 40 CASILLAS HASTA LA MISMA CASILLA
            case "dar vuelta":
                obtenerTurno().sumarVuelta();
                break;

            // Algunos mensajes concretos a comandos inválidos
            case "empezar partida":
                consola.imprimir("¡La partida ya está empezada! \uD83D\uDE21");
                break;


            // SEGUNDO BLOQUE DE COMANDOS: dependen de una instancia------------------------------------------
            default:
                //Dividimos el comando en partes
                String[] comando = comando_entero.split(" ");


                // IMPORTANTE: hacer las comprobaciones en función del número de palabras del comando
                // Si no podríamos querer acceder a un índice que no existe
                if (comando.length == 1) {
                    throw new NoExisteComandoException(comando_entero + " no es un comando válido.");
                } else if (comando.length == 2) {

                    // Podría ser uno de los siguientes:
                    switch (comando[0]) {

                        // Relacionados con casillas
                        case "describir":
                            try {
                                descCasilla(comando[1]);
                            } catch (NoExisteCasillaException e) {
                                consola.imprimir(e.getMessage());
                            }

                            break;
                        case "eliminar":
                            try {
                                eliminarTrato(comando[1]);
                            } catch (NoExisteTratoException e) {
                                consola.imprimir(e.getMessage());
                            }

                            break;
                        // Cuando el coche avanzado ya compró una propiedad este turno no puede comprar más
                        case "comprar":
                            if (this.controlComandos != 3) {
                                try{
                                    comprar(comando[1]);
                                } catch (NoExisteCasillaException e) {
                                    consola.imprimir(e.getMessage());
                                }

                            } else {
                                consola.imprimir(Texto.M_UNA_CASILLA_POR_TURNO);
                            }
                            break;
                        //comando de tratos
                        case "aceptar":
                            try{
                                aceptarTrato(comando[1]);
                            } catch (NoExisteTratoException e) {
                                consola.imprimir(e.getMessage());
                            }

                            break;

                        // Comandos de edificar, hipotecar, deshipotecar (para pista de deporte en length==4)
                        case "edificar":
                            try{
                                edificar(comando[1]);
                            } catch (NoExisteEdificioException e) {
                                consola.imprimir(e.getMessage());
                            }

                            break;
                        case "hipotecar":
                            try {
                                hipotecar(comando[1]);
                            } catch (NoExisteCasillaException | HipotecarException e) {
                                consola.imprimir(e.getMessage());
                            }

                            break;
                        case "deshipotecar":
                            try{
                                deshipotecar(comando[1]);
                            } catch (DeshipotecarException| NoExisteCasillaException e) {
                                consola.imprimir(e.getMessage());
                            }

                            break;

                        // Estadísticas de un jugador
                        case "estadisticas":
                            try{
                                estadisticasJugador(comando[1]);
                            } catch (NoExisteJugadorException e) {
                                consola.imprimir(e.getMessage());
                            }

                            break;

                        case "setfortuna":
                            float fortuna = Float.parseFloat(comando[1]);
                            asignarFortuna(fortuna);
                            break;

                        // Comando inválido
                        default:
                            throw new NoExisteComandoException(comando_entero + " no es un comando válido.");
                    }
                }
                // Caso especial: describir jugador tiene un número variable por si el nombre es compuesto
                // Al llegar aquí ya se sabe que comando.length es como mínimo 3
                else if ("describirjugador".equals(comando[0] + comando[1])) {
                    try{
                        descJugador(comando);
                    } catch (NoExisteJugadorException e) {
                        consola.imprimir(e.getMessage());
                    }

                }
                // Mensaje de error por si se intenta crear un jugador con la partida empezada
                else if ("crearjugador".equals(comando[0] + comando[1])) {
                    consola.imprimir("No se pueden crear más jugadores con la partida empezada.");
                } else if (comando.length == 3) {

                    if ("describiravatar".equals(comando[0] + comando[1])) {
                        try{
                            descAvatar(comando[2]);
                        } catch (NoExisteAvatarException e) {
                            consola.imprimir(e.getMessage());
                        }

                    } else if ("listaredificios".equals(comando[0] + comando[1])) {
                        listarEdificios(comando[2]);
                    }
                    // CHEAT PARA SACAR LO QUE QUIERAS CON LOS DADOS
                    // Si alguno de los valores de dados introducidos no es válido
                    // se imprime un mensaje de error (lo hace dadoValido) y no hace nada
                    else if ("dados".equals(comando[0])) {
                        if (this.controlComandos == 0 ||
                                obtenerTurno().esCocheAvanzado() && this.dado1.getValor() + this.dado2.getValor() > 4) {
                            int dado1 = dadoValido(comando[1]);
                            int dado2 = dadoValido(comando[2]);
                            // Si dado1 y dado2 no son números válidos se salta este if
                            if (dado1 != 0 && dado2 != 0) {
                                lanzarDados(dado1, dado2);
                            }
                        } else {
                            consola.imprimir(Texto.M_COMANDO_BLOQUEADO);
                        }

                    } else {
                        throw new NoExisteComandoException(comando_entero + " no es un comando válido.");
                    }
                } else if (comando.length == 4) {
                    if (comando[0].equals("vender")) {
                        // Desde aquí se permiten valores entre 1 y 6
                        int num_edificios = dadoValido(comando[3]);
                        if (num_edificios != 0) {
                            try{
                                venderEdificios(comando[1], comando[2], num_edificios);
                            } catch (NoExisteCasillaException | NoExisteEdificioException e) {
                                consola.imprimir(e.getMessage());
                            }

                        }
                    } else {
                        throw new NoExisteComandoException(comando_entero + " no es un comando válido.");
                    }
                } else if (comando.length > 5 && comando[0].equals("trato")) {
                   try{
                       proponerTrato(comando_entero);
                   } catch (NoExisteJugadorException | NoExisteCasillaException e) {
                       consola.imprimir(e.getMessage());
                   }


                } else {
                    throw new NoExisteComandoException(comando_entero + "no es un comando valido");
                }
        }
    }


    //SECCIÓN DE COMANDOS QUE NO DEPENDEN DE NINGUNA INSTANCIA----------------------------------------------------------

    /**
     * Método que ejecuta todas las acciones relacionadas con el comando 'jugador'.
     * Imprime la información del jugador que tiene el turno.
     */
    @Override
    public void infoJugadorTurno() {
        Jugador jugador = obtenerTurno(); // Obtener el jugador actual

        // Imprimir el nombre y el avatar en el formato requerido
        consola.imprimir("{\n\tnombre: " + jugador.getNombre() + ",");
        consola.imprimir("\tavatar: " + jugador.getAvatar().getId() + "\n}");
    }

    /**
     * Método para comprobar si los jugadores llevan cuatro vueltas al tablero sin comprar.
     * Si es cierto aumenta el precio de los solares un 5% y reinicia las vueltas sin comprar a 0.
     * Una vuelta == pasar por la casilla de salida.
     */
    private void cuatroVueltasSinCompras() {
        boolean todosCumplen = true;
        for (Jugador j : jugadores) {
            if (j.getVueltas_sin_comprar() < 4) { // Si algún jugador no cumple la condición
                todosCumplen = false; // Cambiamos la variable a false
                break; // detenenemos el bucle ya que no es necesario seguir
            }
        }
        if (todosCumplen) {
            this.tablero.aumentarCoste(banca);
            consola.imprimir("Todos los jugadores han dado 4 vueltas sin comprar! El precio de los solares aumenta.");
            reiniciarVueltasSinCompras();
        }
    }

    /**
     * Método que reinicia las vueltas sin comprar a 0.
     */
    private void reiniciarVueltasSinCompras() {
        for (Jugador j : jugadores) {
            j.setVueltas_sin_comprar(0);
        }
    }

    //SECCIÓN DE MÉTODOS QUE DEPENDEN DE INSTANCIAS---------------------------------------------------------------------

    /**
     * Método que se llama cada vez que se pasa por la Salida.
     * [1] Avisa de la cantidad que se cobra y la suma a la fortuna del jugador correspondiente.
     * [2] Suma 1 vuelta y 1 vuelta sin comprar (la última se reinicia cuando se compra algo, claro).
     * [3] Llama a cuatroVueltasSinCompras() para aumentar el precio de los solares si toca.
     */
    private void cobrarSalida(Jugador jugador) {
        jugador.cobrarSalida();
        cuatroVueltasSinCompras();
    }

    /**
     * Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
     * Hace las comprobaciones pertinentes: si realmente se pueden tirar los dados,
     * si el jugador está en la cárcel o no, etc.
     * Llama a la función moverYEvaluar() en caso de que el jugador pueda moverYEvaluar.
     *
     * @param dadoTrucado1 Si se quiere obtener unos dados concretos se llama a la función con esos valores.
     *                     Si se quiere obtener unos dados aleatorios se llama con los valores (0,0)
     */
    @Override
    public void lanzarDados(int dadoTrucado1, int dadoTrucado2) {

        //Obtenemos el avatar que tiene el turno
        Jugador jugador = obtenerTurno();
        Avatar avatar = jugador.getAvatar();

        // Comprobamos si acaba de ser encarcelado
        if (this.tirado && jugador.isEnCarcel() && jugador.getTiradasCarcel() == 0) {
            consola.imprimir(Texto.M_RECIEN_ENCARCELADO);
        }
        // Un avatar coche puede llegar a tirar 4 veces en un mismo turno pero no más
        else if (this.lanzamientos == 4) {
            consola.imprimir("No puedes tirar más de cuatro veces en el mismo turno.");
        }
        // Un avatar coche avanzado no puede tirar si sacó menos que 5
        else if (obtenerTurno().esCocheAvanzado() && this.tirado && this.dado1.getValor() + this.dado2.getValor() < 5) {
            consola.imprimir("Sacaste menos que 5, no puedes volver a tirar.");
        } else {
            // Comprobamos si aún no se ha tirado en el turno o vienes de haber sacado dobles
            // IMPORTANTE EL ORDEN DEL IF: el valor de los dados antes de la primera tirada de la partida es null
            if (!this.tirado || this.dado1.getValor() == this.dado2.getValor() ||
                    (obtenerTurno().esCocheAvanzado() && this.tirado && this.dado1.getValor() + this.dado2.getValor() > 4)) {
                int resultado1, resultado2;
                if (dadoTrucado1 == 0 && dadoTrucado2 == 0) {
                    // Lanzamos 2 dados (aleatorios)
                    resultado1 = this.dado1.tirarDado();
                    resultado2 = this.dado2.tirarDado();
                } else {
                    // Son dados trucados
                    this.dado1.setValor(dadoTrucado1);
                    this.dado2.setValor(dadoTrucado2);
                    resultado1 = this.dado1.getValor();
                    resultado2 = this.dado2.getValor();
                }
                // Imprimimos los dados
                consola.imprimir("[" + resultado1 + "] [" + resultado2 + "]");
                // Actualizamos las stats~
                jugador.getEstadisticas().sumarVecesTirado(1);
                // Se vuelve a asignar true en segundas/terceras tiradas a no ser que hagamos un caso a parte
                this.tirado = true;
                this.lanzamientos += 1;

                // ¿Estás encarcelado?
                if (jugador.isEnCarcel()) {
                    // Si sacas dobles sales!
                    if (resultado1 == resultado2) {
                        consola.imprimir("DOBLES!!\nSales de la cárcel.");
                        desencarcelar(jugador);
                    } else {
                        // No sales a menos que sea la tercera vez que tiras, caso en el que estás obligado
                        // a pagar la fianza. Si no puedes te declaras en bancarrota
                        if (jugador.getTiradasCarcel() == 2) {
                            consola.imprimir("No ha habido suerte con los dados, toca pagar la fianza...");
                            //Por cómo está implementado salirCarcel(), el comando no se puede usar si ya tiraste
                            this.tirado = false;
                            //Pagas la fianza y sales de la cárcel
                            try {
                                salirCarcel();

                            } catch (SalirCarcelException e) {
                                consola.imprimir(e.getMessage());
                            }

                        } else {
                            consola.imprimir("Continúas en la carcel.");
                            jugador.setTiradasCarcel(1 + jugador.getTiradasCarcel());
                        }
                    }
                } else {
                    // No estás encarcelado: TURNO NORMAL
                    int sumaDados = resultado1 + resultado2;

                    // Mensajes sobre dados dobles si toca
                    if (resultado1 == resultado2) {
                        consola.imprimir("DOBLES!!");
                        if (lanzamientos == 4 && !turno_extra_coche) {
                            this.lanzamientos = 3;
                            turno_extra_coche = true;
                        }
                        this.dadosDoblesSeguidos++;
                        if (this.dadosDoblesSeguidos == 3) {
                            jugador.encarcelar(this.tablero.getCasilla(20));
                            consola.imprimir("Tres dobles son muchos, vas a la cárcel sin pasar por la salida.");
                            return;
                        }
                        // Vuelves a tirar a no ser: (1) que caigas en IrCarcel
                        // (2) que seas un avatar coche y sacases 2,2 o 1,1
                        else if (!avatar.getLugar().getNombre().equals("IrCarcel") &&
                                !(avatar instanceof Coche && (sumaDados == 4 || sumaDados == 2))) {
                            consola.imprimir("Vuelves a tirar.");
                        }
                    } else {
                        // Reiniciamos el contador de dados dobles
                        this.dadosDoblesSeguidos = 0;
                    }

                    // Esta función es la cabra
                    moverYEvaluar(jugador, sumaDados);

                    // Si un avatar pelota está en modo avanzado y tiene movimientos pendientes le avisamos
                    if (this.controlComandos == 1 && !movimientosPendientesActual().isEmpty()) {
                        consola.imprimir(Texto.M_MOVIMIENTOS_PENDIENTES);
                    }
                }
                // Si se ha podido tirar los dados imprimimos el tablero
                verTablero();
            } else {
                consola.imprimir(Texto.M_YA_SE_TIRO);
            }
        }
    }

    /**
     * Método que mueve el avatar de jugador en el tablero (gestiona si está en modo normal o modo avanzado).
     * Llama a evaluarCasilla() para realizar las acciones que corresponden.
     *
     * @param jugador Jugador cuyo avatar hay que moverYEvaluar
     * @param tirada  Suma del resultado al tirar los dados (SE PRESUPONE UN VALOR ENTRE 2 Y 12)
     */
    private void moverYEvaluar(Jugador jugador, int tirada) {
        // Establecemos el avatar, ya que lo usaremos varias veces directamente
        Avatar avatar = jugador.getAvatar();
        // Guardamos la casilla de salida para luego
        Casilla origen = avatar.getLugar();

        // Comprobamos si el jugador está en movimiento avanzado
        if (avatar.getMovimientoAvanzado()) {
            avatar.moverEnAvanzado(this.tablero.getPosiciones(), tirada);
        } else {
            avatar.moverEnBasico(this.tablero.getPosiciones(), tirada);
        }

        // Guardamos la casilla de destino
        Casilla destino = avatar.getLugar();

        // Gestionamos a la pelota por separado
        if(jugador.esPelotaAvanzado()) {
            // Hacemos cast del avatar a pelota pa comprobar un atributo booleano
            Pelota pelota = (Pelota) avatar;
            if(pelota.isPrimerMovimientoPelota()) {
                if(tirada > 4) {
                    consola.imprimir("El avatar " + avatar.getId() + " avanza 5 casillas desde " +
                            origen.getNombre() + " hasta " + destino.getNombre() + ".");
                }
                else {
                    consola.imprimir("El avatar " + avatar.getId() + " retrocede 1 casilla desde " +
                            origen.getNombre() + " hasta " + destino.getNombre() + ".");
                }
                pelota.setPrimerMovimientoPelota(false);
            }
            else {
                if (tirada > 4) {
                    consola.imprimir("El avatar " + avatar.getId() + " avanza " + tirada +
                            " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");
                }
                else {
                    consola.imprimir("El avatar " + avatar.getId() + " retrocede " + (tirada > 0 ? tirada : -tirada) +
                            " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");
                }
            }

            // Si aún tiene movimientos pendientes ponemos controlComandos a 1
            // Si era el último movimiento pendiente ponemos el controlComandos a 0
            if (movimientosPendientesActual().isEmpty()) {
                this.controlComandos = 0;
            } else {
                this.controlComandos = 1;
            }
        }
        else {
            if (tirada > 4 || !avatar.getMovimientoAvanzado()) {
                consola.imprimir("El avatar " + avatar.getId() + " avanza " + tirada +
                        " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");
            }
            else {
                consola.imprimir("El avatar " + avatar.getId() + " retrocede " + (tirada > 0 ? tirada : -tirada) +
                        " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");
            }
        }

        /*
        Si pasamos por la salida hay que cobrar!
        Nótese que "pasar por la salida" implica que origen.posicion>destino.posicion, pero para no confundirlo con
        retroceder casillas (por ejemplo de la 28 a la 24) le sumamos a destino.posicion el máximo número de casillas
        que se pueden retroceder: 4
         */
        if (origen.getPosicion() > destino.getPosicion() + 4) {
            cobrarSalida(jugador);
        }
        /*
        Si en los movimientos avanzados de pelota y coche avanzados pasamos por la Salida marcha atrás devolvemos
        el dinero que se cobra al pasar por la salida ¡Pero sólo si ha cobrado alguna vez la salida!
        Nótese que "pasar por la salida marcha atrás" implica que destino.posicion>origen.posicion, pero para no
        confundirlo con avanzar casillas de manera normal (por ejemplo de la 24 a la 36) le sumamos a origen.posicion
        el valor máximo que se puede avanzar tirando los dados: 12
         */
        if (destino.getPosicion() > origen.getPosicion() + 12 && jugador.getEstadisticas().getDineroSalidaRecaudado() > 0) {
            // Este if no hace falta pero nunca está mal la programación defensiva
            if (obtenerTurno().esCocheAvanzado() || obtenerTurno().esPelotaAvanzado()) {
                jugador.devolverCobrarSalida();
            }
        }

        // EVALUAR
        Casilla c = avatar.getLugar();
        if (!c.evaluarCasilla(jugador, this.dado1.getValor() + this.dado2.getValor())) bucleBancarrota();
    }

    /**
     * Método para gestionar cuando un jugador no puede pagar un alquiler
     * [1] Comprueba si un jugador puede pagar un alquiler con su dinero (fortuna).
     * [2] Si puede devuelve TRUE.
     * [3] Si no pudiese pero tiene propiedades hipotecables se pide que las hipoteque.
     * [4] A cada propiedad que hipoteca se comprueba si ya puede pagar (y en ese caso devuelve TRUE).
     * [5] Si al acabar este proceso sigue sin poder pagar se le obliga a declararse en bancarrota.
     *
     * @param pagador  Jugador que tiene que pagar la cantidad
     * @param cobrador Jugador al que se tiene que pagar la cantidad
     * @param cantidad
     * @return true si se acaba pudiendo pagar, false si el jugador se tuvo que declarar en bancarrota
     */
    private void bucleBancarrota() {
        Jugador pagador = obtenerTurno();
        Jugador cobrador = pagador.getDeudaConJugador();
        float cantidad = pagador.getDeuda();
        if (cantidad > pagador.getFortuna()) {
            // Mientras tenga propiedades hipotecables puede ganar dinero (también se puede declarar en bancarrota)
            while (pagador.tienePropiedadesHipotecables() && cantidad > pagador.getFortuna()) {
                consola.imprimir(Texto.M_NO_HAY_DINERO_OPCIONES);

                String comando_entero = consola.leer("Introduce los comandos bancarrota, hipotecar o vender:");
                String[] comando = comando_entero.split(" ");

                // Mientras no sea uno de estos tres el comando no es válido
                while (!(comando_entero.equals("bancarrota") || comando[0].equals("hipotecar") ||
                        comando[0].equals("vender"))) {

                    consola.imprimir("Comando inválido.");

                    // Volvemos a pedir comandos hasta que sea uno válido
                    consola.imprimir("");
                    comando_entero = consola.leer("Introduce los comandos bancarrota, hipotecar o vender (has introducido " + comando_entero + "), que es inválido:");
                    comando = comando_entero.split(" ");
                }
                if (comando_entero.equals("bancarrota")) {
                    declararBancarrota(cobrador);
                }
                if (comando.length == 2 && comando[0].equals("hipotecar")) {
                    try{
                        hipotecar(comando[1]);
                    } catch (NoExisteCasillaException| HipotecarException e) {
                        consola.imprimir(e.getMessage());
                    }

                }
                if (comando.length == 4 && comando[0].equals("vender")) {
                    // Desde aquí se permiten valores entre 1 y 6
                    int num_edificios = dadoValido(comando[3]);
                    if (num_edificios != 0) {
                        try{
                            proponerTrato(comando_entero);
                        } catch (NoExisteJugadorException | NoExisteCasillaException e) {
                            consola.imprimir(e.getMessage());
                        }

                    }
                }

                // Si después de la operación (vender edificios/hipotecar) ya puede pagar devolvemos true
                if (cantidad <= pagador.getFortuna()) {
                    consola.imprimir("Ya conseguiste dinero para pagar! Realizas el pago.");
                    pagador.pagar(cobrador, cantidad);
                }

            }
            // No tiene ni dinero ni propiedades hipotecables
            do {
                consola.imprimir(Texto.M_BANCARROTA_OBLIGATORIA);
            } while (!consola.leer("Estas obligado a declararte en bancarrota.").equals("bancarrota"));
            declararBancarrota(cobrador);

        }
    }

    /**
     * Método que resetea los atributos relacionados con la cárcel del jugador que se desencarcela.
     * También resetea el boolean de la tirada y el número de lanzamientos del turno.
     * Nótese que por ese motivo al salir de la cárcel puedes tirar los dados como un turno normal.
     * Lo usa salirCarcel pero tb se llama cuando sales por sacar dobles.
     * Por este motivo de que se use en varios casos NO SE COBRA LA FIANZA AQUÍ.
     *
     * @param jugador Jugador que vamos a desencarcelar
     */
    private void desencarcelar(Jugador jugador) {
        jugador.setEnCarcel(false);
        jugador.setTiradasCarcel(0);
        this.tirado = false;
        this.lanzamientos = 0;
    }

    /**
     * Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
     * Si el jugador está encarcelado y aún no tiró en su turno, paga la fianza y sale de la cárcel.
     * NOTA: cuando sales de la cárcel puedes tirar como en un turno normal.
     * CASO ESPECIAL: si es el tercer turno que tiras los dados para salir de la cárcel y no sacas dobles estás obligado
     * a pagar la fianza. En ese caso el método lanzarDados() llama a este método haciendo primero this.tirado=false.
     */
    @Override
    public void salirCarcel() throws SalirCarcelException {//yo creo que falta comprobacion turno sin tirar
        //Establecemos el jugador actual
        Jugador jugador = this.jugadores.get(this.turno);
        if (jugador.isEnCarcel()) {
            // Si ya ha tirado este turno no puede pagar la fianza!
            if (!this.tirado) {
                if (jugador.getFortuna() >= Valor.SALIR_CARCEL) {
                    desencarcelar(jugador);
                    jugador.restarFortuna(Valor.SALIR_CARCEL);
                    this.banca.sumarFortuna(Valor.SALIR_CARCEL);
                    System.out.printf("%s paga la fianza de %,.0f € y sale de la cárcel. Puedes tirar los dados.\n",
                            jugador.getNombre(), Valor.SALIR_CARCEL);
                } else {
                    throw new SalirCarcelException("¡No tienes dinero suficiente para pagar la fianza!");
                }
            } else {
                throw new SalirCarcelException("Ya has lanzado los dados.");
            }

        } else throw new SalirCarcelException("El jugador " + jugador.getNombre() + " no está en la cárcel.");

    }

    /**
     * Método que realiza las acciones asociadas al comando 'acabar turno'.
     */
    @Override
    public void acabarTurno() throws AcabarTurnoException {
        // Si aún no tiraste este turno...
        if (this.controlComandos == 1 && !movimientosPendientesActual().isEmpty()) {
            throw new AcabarTurnoException(Texto.M_MOVIMIENTOS_PENDIENTES);
        }

        if (!this.tirado) {
            // ...a no ser que tengas el turno actual bloqueado (no puedes lanzar dados) no puedes acabar el turno
            // IMPORTANTE: comprobar que el arraylist no esté vacío antes de intentar acceder a un elemento
            if (!(!movimientosPendientesActual().isEmpty() && movimientosPendientesActual().get(0) == 0)) {
                throw new AcabarTurnoException("Aún no has lanzado los dados este turno!");
            }
        }
        // Si NO acabas de ser encarcelado...
        else if (!(obtenerTurno().isEnCarcel() && obtenerTurno().getTiradasCarcel() == 0)) {
            // ...vemos si es un coche en modo avanzado
            if (obtenerTurno().esCocheAvanzado()) {
                // Si es un coche en modo avanzado el único caso en el que no puede pasar turno es si saca >4
                // A no ser que ya lleve 4 lanzamientos
                if (this.dado1.getValor() + this.dado2.getValor() > 4 && this.lanzamientos != 4) {
                    throw new AcabarTurnoException("Sacaste más que 4, tienes que volver a tirar.");
                }
            } else {
                // Si no es un coche en modo avanzado NO puede acabar el turno si sacó dobles
                if (this.dado1.getValor() == this.dado2.getValor()) {
                    throw new AcabarTurnoException("¡Sacaste dobles! Tienes que volver a tirar.");
                }
            }
        }

        // CUANDO SE LLEGA AQUÍ EL JUGADOR SÍ PUEDE ACABAR EL TURNO

        // Si un avatar coche acaba un turno en el que no pudo tirar los dados...
        // ...eliminamos el primer elemento ya que es el que acabamos de gestionar este turno
        if (obtenerTurno().esCocheAvanzado()) {
            obtenerTurno().eliminarMovimientoPendiente();
        }

        // Vamos a pasar el turno: actualizamos los atributos correspondientes
        this.tirado = false; // Reiniciar el estado de "tirado"
        this.lanzamientos = 0; // Reiniciar los lanzamientos
        this.dadosDoblesSeguidos = 0; // Reiniciar el contador de dados dobles
        this.turno_extra_coche = false;

        // Incrementar el turno y asegurar que no exceda el tamaño del array
        this.turno += 1;
        if (this.turno >= this.jugadores.size()) {
            this.turno = 0; // Reiniciar el turno si llegamos al final de la lista
        }

        // Imprimir el nombre del nuevo jugador actual
        consola.imprimir("El jugador actual es: " + obtenerTurno().getNombre());

        // Actualizamos controlComandos para el nuevo jugador que tiene el turno
        this.controlComandos = 0;
        if (!movimientosPendientesActual().isEmpty()) {
            // Si hay un 0 en movimientos_pendientes el jugador no va a poder mover este turno
            if (movimientosPendientesActual().get(0) == 0) {
                this.controlComandos = 2;
                consola.imprimir("Este turno no puedes lanzar los dados.");
            }
        }

    }

    /**
     * Método para cambiar el modo de movimiento del avatar que tiene el turno
     */
    @Override
    public void cambiarModo() throws CambiarModoException {
        if (this.tirado) {
            throw new CambiarModoException();
        }// duda grande si hace falta el else creo que no lol
        Avatar avatar = obtenerTurno().getAvatar();
        if (avatar.getMovimientoAvanzado()) {
            if (movimientosPendientesActual().isEmpty()) {
                consola.imprimir("El avatar " + avatar.getId() + " vuelve el movimiento normal.");
                this.controlComandos = 0;
                obtenerTurno().getAvatar().cambiarMovimiento();
            } else {
                consola.imprimir("El avatar " + avatar.getId() + " no puede cambiar de modo ya que esta bloqueado.");
            }
        } else {
            if (avatar instanceof Coche)
                System.out.printf(Texto.M_ACTIVAR_MOVIMIENTO_AVANZADO + "\n", avatar.getId(), "coche");
            if (avatar instanceof Pelota)
                System.out.printf(Texto.M_ACTIVAR_MOVIMIENTO_AVANZADO + "\n", avatar.getId(), "pelota");
            obtenerTurno().getAvatar().cambiarMovimiento();
        }

    }

    /**
     * Método que realiza las acciones asociadas al comando 'ver tablero'
     */
    @Override
    public void verTablero() {
        consola.imprimir(tablero.toString());
    }

    /**
     * Método que realiza las acciones asociadas al comando 'listar jugadores'.
     */
    @Override
    public void listarJugadores() {
        for (Jugador j : jugadores) {
            j.infoJugador();
        }
    }

    /**
     * Método que realiza las acciones asociadas al comando 'listar avatares'.
     */
    @Override
    public void listarAvatares() {
        for (Avatar a : avatares) {
            if (a != null) {
                a.infoAvatar(); // Llama a la función infoAvatar para imprimir los detalles del avatar
            }
        }
    }

    /**
     * Método que realiza las acciones asociadas al comando 'listar enventa'.
     */
    @Override
    public void listarVenta() {
        consola.imprimir("Propiedades en venta:");
        Casilla casilla_aux;
        for (int i = 0; i < 40; i++) {
            casilla_aux = tablero.getCasilla(i);
            if (casilla_aux instanceof Propiedad && ((Propiedad) casilla_aux).getDuenho().getNombre().equals("banca")) {
                System.out.printf("%s - Precio: %,.0f€\n", casilla_aux.getNombre(), ((Propiedad) casilla_aux).getValor());
            }
        }
    }


    //SECCIÓN DE COMANDOS QUE DEPENDEN DE UNA INSTANCIA-----------------------------------------------------------------

    //NOTA PARA LA SEGUNDA ENTREGA: añadiría las comprobaciones que hace comprarCasilla aquí
    //para que la función comprarCasilla se llame solo cuando ya se sabe que se puede comprar
    //pero bueno en general es lioso que se necesiten 2 métodos para hacer 1 cosa de esta manera

    /**
     * Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     *
     * @param nombre Cadena de caracteres con el nombre de la casilla.
     */
    @Override
    public void comprar(String nombre) throws NoExisteCasillaException {
        Propiedad c = tablero.encontrar_propiedad(nombre);
        // Comprobamos si la casilla existe y ya se ha tirado (se hacen otras comprobaciones dentro de comprarCasilla)
        if (c == null) {
            throw new NoExisteCasillaException("No hay ninguna casilla que se llame " + nombre);
        }
        if (this.tirado || lanzamientos > 0) {
            //le paso el jugador que tiene el turno y eljugador 0 (la banca)
            Jugador jugador = obtenerTurno();
            if (c.getDuenho().equals(banca) && jugador.getAvatar().getLugar().equals(c) &&
                    (c.getValor() <= jugador.getFortuna())) {
                reiniciarVueltasSinCompras();
            }
            c.comprarPropiedad(jugador, this.banca);

            // Si un avatar tipo coche en modo avanzado compra ya no va a poder comprar más en el turno
            if (jugador.getAvatar() instanceof Coche && jugador.getAvatar().getMovimientoAvanzado()) {
                this.controlComandos = 3;
            }

        } else {
            consola.imprimir("¡Primero tienes que tirar!");
        }
    }



    /** Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
     * Imprime la información sobre la casilla correspondiente si es descriptible
     *
     * @param nombre_casilla Nombre de la casilla a describir
     */
    
    @Override
    public void descCasilla(String nombre_casilla) throws NoExisteCasillaException {
        Casilla casilla = tablero.encontrar_casilla(nombre_casilla);
        if(casilla==null) {
            throw new NoExisteCasillaException(nombre_casilla + " no es un nombre de casilla válido.");
        }
        System.out.print(casilla.infoCasilla());
    }

    /**Método para encontrar a un jugador a partir de su nombre.
     * Si no lo encuentra devuelve null
     * @param nombre_jugador Nombre del jugador que tenemos que buscar
     */
    private Jugador encontrarJugador(String nombre_jugador) {
        // Si no se encuentra ningún jugador con ese nombre se devolverá null
        Jugador jugadorEncontrado=null;

        // En partes están los nombres de los jugadores, comprueba que el nombre existe y saca su info si existe
        for(Jugador jugador : jugadores) {
            if(jugador.getNombre().equals(nombre_jugador)) {
                jugadorEncontrado = jugador;
                break;
            }
        }

        return jugadorEncontrado;
    }

    /**Método que realiza las acciones asociadas al comando 'describir jugador'.
     * @param partes comando introducido
     */
    @Override
    public void descJugador(String[] partes) throws NoExisteJugadorException {
        String nombre_completo = "";

        // Construímos el nombre_completo a partir de las partes
        for(int i=2; i < partes.length; i++) {
            // Quitando la primera, cada vez que se añada una palabra tiene que haber un espacio
            if(i>2) {
                nombre_completo += " ";
            }
            nombre_completo += partes[i];
        }

        Jugador jugador=encontrarJugador(nombre_completo);

        // Si encontramos al jugador imprimimos su info; si no, avisamos al usuario
        if(jugador==null) {
            throw new NoExisteJugadorException("No se ha encontrado al jugador buscado.");
        }
        jugador.infoJugador();

        
    }
    
    /**Método que realiza las acciones asociadas al comando 'describir avatar'.
     * @param ID id del avatar a describir
     */
    @Override
    public void descAvatar(String ID) throws NoExisteAvatarException {
        // lista llamada avatares se recorre
        for (Avatar avatar : avatares) { // Busca el avatar en la lista
            if (avatar.getId().equals(ID)) {
                avatar.infoAvatar(); // Salida si encuentra
                return;
            }
        }
        // Si no encuentra el avatar, muestra un mensaje de error
        throw  new NoExisteAvatarException("Avatar con ID " + ID + " no encontrado.");
    }


    //SECCIÓN DE CHEATS DE MENÚ-----------------------------------------------------------------------------------------

    /**Método para conseguir mucho dinero.*/
    private void dineroInfinito() {
        Jugador jugador = obtenerTurno();
        jugador.sumarFortuna(1000000000); //mil millones
    }
    private void asignarFortuna(float fortuna) {
        for(Jugador j: jugadores){
            j.setFortuna(fortuna);
        }
    }
    

    // MÉTODOS SIN GRUPO:

    /**Método que transforma un String a un entero si el String es un número entre 1 y 6.
     * Si no es un número válido imprime un mensaje de error y devuelve 0.
     * Método auxiliar para leerDadoValido() pero también sirve para lanzarDados().
     */
    private int dadoValido(String numero) {
        // Función .matches() para ver si los caracteres de un String son del tipo indicado
        // En nuestro caso "\\d" significa que el String debe contener un único dígito
        if(numero.matches("\\d")) {
            // Convertimos el String a int si es un dígito y vemos que esté entre 1 y 6
            int n = Integer.parseInt(numero);
            if(0<n && n<=6) {
                n = Integer.parseInt(numero);
                return n;
            }
            else {
                consola.imprimir("Número inválido.");
                return 0;
            }
        }
        else {
            consola.imprimir("Número inválido.");
            return 0;
        }
    }

    private boolean esTipoAvatar(String tipo) {
        return "coche".equals(tipo) ||
                "esfinge".equals(tipo) ||
                "sombrero".equals(tipo) ||
                "pelota".equals(tipo);
    }

    /**Método que imprime las estadísticas de un jugador de la partida.*/
    @Override
    public void estadisticasJugador(String nombre_jugador) throws NoExisteJugadorException {
        Jugador jugador=encontrarJugador(nombre_jugador);

        if(jugador==null) {
            throw new NoExisteJugadorException("No se ha encontrado el jugador "+nombre_jugador+"\n");
        }
        jugador.infoEstadisticas();

    }

    /**
     * Método que imprime las estadísticas generales de la partida.
     * Muestra información como las casillas más visitadas, casillas más rentables,
     * grupos más rentables, jugadores con más vueltas, tiradas y fortuna.
     */
    @Override
    public void estadisticasGenerales() {
        consola.imprimir("{");


        //.clear() limpia la lista
        //.add()añade a la lista


        // Casillas más visitadas
        List<String> nombresCasillas = new ArrayList<>();
        int vecesMaxima = 0;

        // Determinar la cantidad máxima de visitas entre todas las casillas
        for (int i = 0; i < 40; i++) {
            Casilla casillaAux = tablero.getCasilla(i);

            // Si la casilla tiene más visitas que el máximo actual, se actualiza el máximo
            if (casillaAux.frecuenciaVisita() > vecesMaxima) {
                vecesMaxima = casillaAux.frecuenciaVisita();
                nombresCasillas.clear(); // Reinicia la lista con la nueva casilla más visitada
                nombresCasillas.add(casillaAux.getNombre());
            }
            // Si tiene el mismo número de visitas que el máximo, se añade a la lista
            else if (casillaAux.frecuenciaVisita() == vecesMaxima) {
                nombresCasillas.add(casillaAux.getNombre());
            }
        }

        // Casillas más rentables
        List<String> nombresRentables = new ArrayList<>();
        float recaudacionMaxima = 0;

        // Determinar la casilla con mayor dinero recaudado
        for (int i = 0; i < 40; i++) {
            Casilla casillaAux = tablero.getCasilla(i);

            // Actualizar si esta casilla tiene una recaudación mayor
            if (casillaAux.getDineroRecaudado() > recaudacionMaxima) {
                recaudacionMaxima = casillaAux.getDineroRecaudado();
                nombresRentables.clear();
                nombresRentables.add(casillaAux.getNombre());
            }
            // Si la recaudación es igual a la máxima, verificar su tipo y añadirla si aplica
            else if (casillaAux.getDineroRecaudado() == recaudacionMaxima) {
                if (casillaAux instanceof Propiedad) {
                    nombresRentables.add(casillaAux.getNombre());
                }
            }
        }

        // Jugadores con más vueltas
        List<String> jugadoresVueltas = new ArrayList<>();
        int maxVueltas = 0;

        // Determinar el jugador con más vueltas dadas
        for (Jugador j : jugadores) {
            if (j.getVueltas() > maxVueltas) {
                maxVueltas = j.getVueltas();
                jugadoresVueltas.clear();
                jugadoresVueltas.add(j.getNombre());
            } else if (j.getVueltas() == maxVueltas) {
                jugadoresVueltas.add(j.getNombre());
            }
        }

        // Jugadores con más tiradas de dados
        List<String> jugadoresDados = new ArrayList<>();
        int maxTiradas = 0;

        // Determinar el jugador con más tiradas
        for (Jugador j : jugadores) {
            int tiradas = j.getEstadisticas().getVecesTirado();

            if (tiradas > maxTiradas) {
                maxTiradas = tiradas;
                jugadoresDados.clear();
                jugadoresDados.add(j.getNombre());
            } else if (tiradas == maxTiradas) {
                jugadoresDados.add(j.getNombre());
            }
        }

        // Jugadores con más fortuna
        List<String> jugadoresRicos = new ArrayList<>();
        float maxFortuna = 0;

        // Determinar el jugador más rico
        for (Jugador j : jugadores) {
            if (j.getFortuna() > maxFortuna) {
                maxFortuna = j.getFortuna();
                ArrayList<Propiedad> propiedades= j.getPropiedades();
                for(Propiedad c: propiedades){
                    maxFortuna+=c.getValor();
                    if(c instanceof Solar){
                        ArrayList<Edificio> casas = ((Solar)c).getCasas();
                        ArrayList<Edificio> hoteles = ((Solar)c).getHoteles();
                        ArrayList<Edificio> pisicinas=((Solar)c).getPiscinas();
                        ArrayList<Edificio> pistas = ((Solar)c).getPistasDeDeporte();
                        
                        for(Edificio ed: casas){
                            maxFortuna+=ed.getCoste();
                        }

                        for(Edificio ed: hoteles){
                            maxFortuna+=ed.getCoste();
                        }

                        for(Edificio ed: pisicinas){
                            maxFortuna+=ed.getCoste();
                        }
                        
                        for(Edificio ed: pistas){
                            maxFortuna+=ed.getCoste();
                        }
                    }
                }
                
                jugadoresRicos.clear();
                jugadoresRicos.add(j.getNombre());
            } else if (j.getFortuna() == maxFortuna) {
                jugadoresRicos.add(j.getNombre());
            }
        }

        // Grupos más rentables
        List<String> gruposRentables = new ArrayList<>();
        float recaudacionGrupoMax = 0;

        // Determinar el grupo con mayor recaudación
        for (int i = 0; i < 40; i++) {
            Casilla casillaAux = tablero.getCasilla(i);

            // Solo considerar casillas de tipo "Solar"
            if (casillaAux instanceof Solar) {
                float recaudacionGrupo = ((Solar)casillaAux).getGrupo().getRecaudacionGrupo();

                // Actualizar si el grupo tiene una mayor recaudación
                if (recaudacionGrupo > recaudacionGrupoMax) {
                    recaudacionGrupoMax = recaudacionGrupo;
                    gruposRentables.clear();
                    gruposRentables.add(((Solar)casillaAux).getGrupo().getColorGrupo());
                }
                // Si la recaudación es igual al máximo, añadir el grupo si no está en la lista
                else if (recaudacionGrupo == recaudacionGrupoMax) {
                    if (!gruposRentables.contains(((Solar)casillaAux).getGrupo().getColorGrupo())) {
                        gruposRentables.add(((Solar)casillaAux).getGrupo().getColorGrupo());
                    }
                }
            }
        }

        // Imprimir los resultados de las estadísticas
        consola.imprimir("casillaMasFrecuentada " + String.join(", ", nombresCasillas));
        consola.imprimir("casillaMasRentable: " + String.join(", ", nombresRentables));
        consola.imprimir("grupoMasRentable: " + String.join(", ", gruposRentables));
        consola.imprimir("jugadorMasVueltas: " + String.join(", ", jugadoresVueltas));
        consola.imprimir("jugadorMasVecesDados: " + String.join(", ", jugadoresDados));
        consola.imprimir("jugadorEnCabeza: " + String.join(", ", jugadoresRicos));

        consola.imprimir("\n}");
    }

    
    @Override
    public void declararBancarrota(Jugador cobrador) {
        Jugador jugador = obtenerTurno();
        ArrayList<Casilla> propiedades = new ArrayList<>(jugador.getPropiedades());
        
        if (cobrador.equals(null)) {
            consola.imprimir("El jugador " + jugador.getNombre() + " se declara en bancarrota. " +
            "Sus propiedades vuelven a estar a la venta al precio original.");
            
            for (Casilla c : propiedades) {
                if (c instanceof Solar) {
                    ((Solar) c).eliminarTodosLosEds();
                }
                if (c instanceof Propiedad) {
                    ((Propiedad) c).setDeshipotecada();
                }
                jugador.eliminarPropiedad((Propiedad)c);
            }
        } else {
            consola.imprimir("El jugador " + jugador.getNombre() + " se declara en bancarrota. " +
            "Sus propiedades pasan a ser de " + cobrador.getNombre());
            
            for (Casilla c : propiedades) {
                if (c instanceof Solar) {
                    ((Solar) c).eliminarTodosLosEds();
                }
                if (c instanceof Propiedad) {
                    ((Propiedad) c).setDeshipotecada();
                }
                cobrador.anhadirPropiedad((Propiedad)c);
                jugador.eliminarPropiedad((Propiedad)c);
            }
        }
    
        eliminarJugador(jugador);
    }
    
    @Override
    public void hipotecar(String nombre) throws NoExisteCasillaException, HipotecarException {
        Casilla casilla = tablero.encontrar_casilla(nombre);
        Jugador jugador = obtenerTurno();
        
        if (casilla == null) {
            throw new NoExisteCasillaException("No existe esa casilla. No la puedes hipotecar.");

        }
        
        if (!(casilla instanceof Propiedad)) {
            throw new HipotecarException("No puedes hipotecar " + casilla.getNombre() + ", no es una propiedad.");

        }
        
        Propiedad propiedad = (Propiedad) casilla;
        
        if (!propiedad.getDuenho().equals(jugador)) {
            throw new HipotecarException("El jugador " + jugador.getNombre() + " no puede hipotecar " +
            propiedad.getNombre() + ". No es una propiedad que le pertenezca.");

        }
        
        if (propiedad.esHipotecable()) {
            if(propiedad instanceof Solar){
                consola.imprimir("El jugador " + jugador.getNombre() + " recibe " + propiedad.getHipoteca() +                                " por la hipoteca de " + casilla.getNombre() +
                            ". No puede recibir alquileres ni edificar en el grupo " + ((Solar)propiedad).getGrupo().getColorGrupo());
                            jugador.sumarFortuna(propiedad.getHipoteca());
                        }
                        else{
                            consola.imprimir("El jugador " + jugador.getNombre() + " recibe " + propiedad.getHipoteca() +                                " por la hipoteca de " + casilla.getNombre() +
                            ". No puede recibir alquileres." );
                            jugador.sumarFortuna(propiedad.getHipoteca());
                        }
                        
                    } else {
                        throw new HipotecarException("No puedes hipotecar " + propiedad.getNombre() + " en este momento.");
                    }
                }

    public void deshipotecar(String nombre) throws NoExisteCasillaException, DeshipotecarException {
        Casilla casilla = tablero.encontrar_casilla(nombre);
        Jugador jugador = obtenerTurno();
    
        if (casilla == null) {
            throw new NoExisteCasillaException("No puedes deshipotecar algo que no existe en el tablero.");

        }
        
        if (!(casilla instanceof Propiedad)) {
            throw new DeshipotecarException("No puedes deshipotecar " + casilla.getNombre() + ", no es una propiedad.");

        }

        Propiedad propiedad = (Propiedad) casilla;

        if (!propiedad.getDuenho().equals(jugador)) {
            throw new DeshipotecarException("El jugador " + jugador.getNombre() + " no puede deshipotecar " +
            propiedad.getNombre() + ". No es una propiedad que le pertenezca.");

        }
        
        float costoDeshipotecar = propiedad.getDeshipoteca();
        
        if (costoDeshipotecar > jugador.getFortuna()) {
            throw new DeshipotecarException("No tienes suficiente dinero para deshipotecar " + propiedad.getNombre() + ".");

        }
        
        if (propiedad.esDesHipotecable()) {
            System.out.printf("El jugador %s paga %.2f para deshipotecar %s.%n",
            jugador.getNombre(), costoDeshipotecar, propiedad.getNombre());
            jugador.sumarFortuna(-costoDeshipotecar);
            jugador.sumarGastos(costoDeshipotecar);
        } else {
            throw new DeshipotecarException("No puedes deshipotecar " + propiedad.getNombre() + " en este momento.");
        }
    }
    
    
    private static boolean esEdificioValido(String tipo) {
        return tipo.equals("casa") ||
                tipo.equals("hotel") ||
                tipo.equals("pista de deporte") ||
                tipo.equals("piscina");
    }

    //se puede hacer más modular, como todo obiamente os lo dejo a vosotros
    @Override
    public void edificar(String tipo) throws NoExisteEdificioException {
        if (esEdificioValido(tipo)){
            if (obtenerTurno().getAvatar().getLugar() instanceof Solar) {
                Jugador jugador = obtenerTurno(); // Obtener el jugador cuyo turno es actualmente
                Casilla casilla = jugador.getAvatar().getLugar(); // Obtener la casilla en la que se encuentra el jugador
                Solar solar =tablero.encontrar_solar(casilla.getNombre());
                // En un solar se puede construir una casa si dicho solar pertenece al jugador cuyo avatar se encuentra
                // en la casilla y si (1) el avatar ha caído más de dos veces en esa misma casilla o (2) el jugador posee el
                // grupo de casillas al que pertenece dicha casilla.
                if ((solar.getVecesVisitadaPorDuenho() > 2 || solar.getGrupo().esDuenhoGrupo(jugador))) {
                    if (solar.getDuenho().equals(jugador)) {
                        switch (tipo) {
                            case "casa":
                                solar.edificarCasa(jugador);
                                break;

                                case "hotel":
                                    solar.edificarHotel(jugador);
                                    break;
                                    
                                case "piscina":
                                    solar.edificarPiscina(jugador);
                                    break;

                                case "pista de deporte":
                                    solar.edificarPista(jugador);
                                    break;

                            default:
                                break;
                        }
                    } else {
                        consola.imprimir("No puedes edificar en esta casilla porque no te pertenece.");
                    }
                } else {
                    consola.imprimir("Para edificar debes ser dueño del grupo o haber caído más de dos veces en la casilla siendo el propietario.");
                }
            } else {
                consola.imprimir("No puedes edificar en una casilla que no es de tipo solar.");
            }
        } else {
            consola.imprimir("El tipo de edificio " + tipo + " no es válido para edificar.");
        }
    }


    @Override
    public void venderEdificios(String tipo, String nombre, int n) throws NoExisteEdificioException, NoExisteCasillaException {
        Jugador jugador = obtenerTurno(); // Obtener el jugador cuyo turno es
        Casilla casilla =tablero.encontrar_casilla(nombre);
        Solar solar =tablero.encontrar_solar(casilla.getNombre());
        if(solar == null){
           throw new NoExisteCasillaException("La casilla no existe o no es un solar \n");
        }
        else{
            if (solar.getDuenho().equals(jugador)){
                switch (tipo) {
                    case "casa":
                        solar.venderCasas(jugador, n);
                        break;
                    case "hotel":
                        solar.venderHoteles(jugador, n);
                        break;
                    case "piscina":
                        solar.venderPiscinas(jugador, n);
                        break;
                    case "pista de deporte":
                        solar.venderPistas(jugador, n);
                        break;
                    default:

                        throw new NoExisteEdificioException("El tipo de edificio introducido es incorrecto.");

                }
            } else {
                consola.imprimir("No puedes vender las edificaciones de esta propiedad porque no te pertenece.");
            }
        }
    }

    @Override
    public void listarEdificios(String color) {
        // Si no se proporciona un color, lista todos los edificios
        if (color == null) {
            for (Jugador j : jugadores) {
                for (Solar c : j.getSolares()) {
                    // Lista las edificaciones de la casilla
                    consola.imprimir(c.listarEdificaciones());
                }
            }
        } else {
            // Si se proporciona un color, lista solo los edificios del grupo de ese color
            for (Jugador j : jugadores) {
                for (Solar c : j.getSolares()) {
                    // Si el color del grupo coincide con el proporcionado, listar las edificaciones
                    if (c.getGrupo().getColorGrupo().equalsIgnoreCase(color)) {
                        consola.imprimir(c.listarEdificaciones());
                    }
                }
            }
        }
    }
    
    @Override
    public void ayuda() {
        consola.imprimir("Lista de comandos disponibles:");
        consola.imprimir("- terminar partida / acabar partida: Termina la partida.");
        consola.imprimir("- bancarrota: Declararse en bancarrota.");
        consola.imprimir("- ver tablero: Muestra el estado actual del tablero.");
        consola.imprimir("- jugador: Muestra la información del jugador actual.");
        consola.imprimir("- estadisticas: Muestra estadísticas generales.");
        consola.imprimir("- salir carcel: Salir de la cárcel.");
        consola.imprimir("- listar enventa: Lista las casillas en venta.");
        consola.imprimir("- listar jugadores: Lista todos los jugadores.");
        consola.imprimir("- listar avatares: Lista todos los avatares.");
        consola.imprimir("- listar edificios: Lista todos los edificios.");
        consola.imprimir("- lanzar dados: Lanza los dados (si está permitido).");
        consola.imprimir("- acabar turno: Finaliza el turno del jugador actual.");
        consola.imprimir("- cambiar modo: Cambia el modo del avatar.");
        consola.imprimir("- siguiente: Realiza el siguiente movimiento.");
        consola.imprimir("- dinero infinito: Activa el modo dinero infinito.");
        consola.imprimir("- dar vuelta: Avanza 40 casillas.");
        consola.imprimir("- describir [nombre_casilla]: Describe la casilla indicada.");
        consola.imprimir("- comprar [nombre_casilla]: Compra la casilla indicada.");
        consola.imprimir("- edificar [nombre_casilla]: Construye un edificio.");
        consola.imprimir("- hipotecar [nombre_casilla]: Hipoteca una propiedad.");
        consola.imprimir("- deshipotecar [nombre_casilla]: Deshipoteca una propiedad.");
        consola.imprimir("- estadisticas [nombre_jugador]: Muestra estadísticas de un jugador.");
        consola.imprimir("- describir jugador [nombre_jugador]: Describe un jugador.");
        consola.imprimir("- crear jugador [nombre_avatar]: Crea un jugador (solo antes de empezar la partida).");
        consola.imprimir("- describir avatar [Letra avatar]: Describe un avatar.");
        consola.imprimir("- listar edificios [gruoi]: Lista edificios de un tipo.");
        consola.imprimir("- dados [valor1] [valor2]: Lanza dados con valores específicos.");
        consola.imprimir("- vender [nombre_propiedad] [tipo_edificio] [cantidad]: Vende edificios.");
        consola.imprimir("- edificar/deshipotecar/hipotecar [Tipo edificio]e: Gestiona pistas de deporte.");
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    METODOS DE LOS TRATOS

    ACEPTAR TRATO
    PROPONER TRATO
    LISTAR TRATOS

     */
    @Override
    public void aceptarTrato(String idTrato) throws NoExisteTratoException {
        // Obtener el jugador actual
        Jugador jugador = obtenerTurno();

        // Buscar el trato por ID en los tratos pendientes del jugador
        Trato trato = jugador.buscarTratoPorId(idTrato);
        if (trato == null) {
            throw new NoExisteTratoException("Trato inexistente o no estás involucrado.");
        }

        Jugador jugador2 = trato.getJugadorPropone();

        if (jugador.getNombre().equals(jugador2.getNombre())) {
            throw new NoExisteTratoException("No puedes aceptar un trato que tú mismo propusiste. ¡Tramposo!");
        }

        // Intentar aceptar el trato
        if (trato.aceptar()) {
            // Eliminar el trato tras ser aceptado
            jugador.eliminarTrato(trato);

            // Construir el mensaje detallado
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Se ha aceptado el siguiente trato con ")
                    .append(jugador2.getNombre()).append(": ");

            if (trato.getPropiedadOfrecida() != null) {
                mensaje.append("le doy ").append(trato.getPropiedadOfrecida().getNombre());
            }

            if (trato.getDineroOfrecido() > 0) {
                if (trato.getPropiedadOfrecida() != null) {
                    mensaje.append(" y ");
                }
                mensaje.append(trato.getDineroOfrecido()).append("€");
            }

            mensaje.append(" y ").append(jugador2.getNombre()).append(" me da ");

            if (trato.getPropiedadDemandada() != null) {
                mensaje.append(trato.getPropiedadDemandada().getNombre());
            }

            if (trato.getDineroDemandado() > 0) {
                if (trato.getPropiedadDemandada() != null) {
                    mensaje.append(" y ");
                }
                mensaje.append(trato.getDineroDemandado()).append("€");
            }

            mensaje.append(".");
            consola.imprimir(mensaje.toString());
        } else {
            consola.imprimir("No se pudo aceptar el trato.");
        }
    }



    @Override
    public void listarTratosJugadorActual() {
        // Obtener el jugador actual
        Jugador jugador = obtenerTurno();

        // Obtener la lista de tratos pendientes
        ArrayList<Trato> tratos = jugador.getTratosPendientes(); // Cambiado de ArrayList a Set

        // Imprimir los tratos pendientes
        if (tratos.isEmpty()) {
            consola.imprimir("No tienes tratos pendientes.");
        } else {
            consola.imprimir("Tus tratos pendientes:");
            for (Trato trato : tratos) {
                consola.imprimir(trato.toString());
            }
        }
    }

    @Override
    public void proponerTrato(String detalleTrato) throws NoExisteJugadorException, NoExisteCasillaException {
        // Dividir el comando para extraer el jugador y el detalle del trato
        String[] partes = detalleTrato.split(": cambiar ");
        if (partes.length < 2) {
            consola.imprimir("Formato inválido para proponer trato." +
                    " Ejemplo: 'trato Maria: cambiar (Solar1 y 300000) por (Solar4 y 300000)");
            return;
        }

        // Extraer el nombre del jugador receptor y los detalles del trato
        String nombreJugador = partes[0].replace("trato", "").trim();
        String detalle = partes[1].trim();

        // Buscar al jugador receptor
        Jugador receptor = encontrarJugador(nombreJugador);
        if (receptor == null) {
            throw new NoExisteJugadorException("El jugador " + nombreJugador + " no existe.\n");
        }

        // Dividir el detalle en dos partes separadas por "por"
        String[] bloques = detalle.split("\\)\\s+por\\s+\\(");
        if (bloques.length != 2) {
            consola.imprimir("Formato inválido para el trato. Asegúrate de incluir 'por' entre los paréntesis.");
            return;
        }

        // Procesar el lado ofrecido
        String ladoOfrecido = bloques[0].replace("(", "").trim();
        String[] elementosOfrecidos = ladoOfrecido.split(" y ");
        Propiedad propiedadOfrecida = null;
        float dineroOfrecido = 0;

        for (String elemento : elementosOfrecidos) {
            if (esDinero(elemento)) {
                dineroOfrecido += Float.parseFloat(elemento.trim());
            } else {
                propiedadOfrecida = this.tablero.encontrar_propiedad(elemento.trim());
                if (propiedadOfrecida == null || !propiedadOfrecida.getDuenho().getNombre().equals(obtenerTurno().getNombre())) {
                    throw new NoExisteCasillaException("La propiedad ofrecida " + elemento + " no pertenece al jugador actual o no existe.");
                }
            }
        }

        // Procesar el lado reclamado
        String ladoReclamado = bloques[1].replace(")", "").trim();
        String[] elementosReclamados = ladoReclamado.split(" y ");
        Propiedad propiedadReclamada = null;
        float dineroReclamado = 0;

        for (String elemento : elementosReclamados) {
            if (esDinero(elemento)) {
                dineroReclamado += Float.parseFloat(elemento.trim());
            } else {
                propiedadReclamada = this.tablero.encontrar_propiedad(elemento.trim());
                if (propiedadReclamada == null || !propiedadReclamada.getDuenho().getNombre().equals(receptor.getNombre())) {
                    throw new NoExisteCasillaException("La propiedad reclamada " + elemento + " no pertenece al jugador receptor o no existe.");
                }
            }
        }
        if(dineroReclamado > 0 && dineroOfrecido > 0){
            consola.imprimir("No tiene sentido enviar cantidades de dinero por ambos lados" +
                    " haz la resta si quieres enviarlo");
            return;
        }
        // Validar que al menos una propiedad o cantidad de dinero esté presente en cada lado
        if (propiedadOfrecida == null && dineroOfrecido == 0) {
            consola.imprimir("El lado ofrecido del trato debe incluir al menos una propiedad o una cantidad de dinero.");
            return;
        }
        if (propiedadReclamada == null && dineroReclamado == 0) {
            consola.imprimir("El lado reclamado del trato debe incluir al menos una propiedad o una cantidad de dinero.");
            return;
        }

        // Crear el objeto Trato
        Jugador jugadorOfreciendo = obtenerTurno(); // Jugador actual
        Trato trato = new Trato(jugadorOfreciendo, receptor, propiedadOfrecida, propiedadReclamada, dineroOfrecido, dineroReclamado);

        // Agregar el trato a los pendientes del receptor y del jugador actual
        receptor.agregarTrato(trato);
        jugadorOfreciendo.agregarTrato(trato);

        // Construir el mensaje final del trato
        StringBuilder mensaje = new StringBuilder();
        mensaje.append(receptor.getNombre()).append(", ¿te doy ");

        if (propiedadOfrecida != null) {
            mensaje.append(propiedadOfrecida.getNombre());
        }
        if (dineroOfrecido > 0) {
            if (propiedadOfrecida != null) mensaje.append(" y ");
            mensaje.append(dineroOfrecido).append("€");
        }

        mensaje.append(" y tú me das ");

        if (propiedadReclamada != null) {
            mensaje.append(propiedadReclamada.getNombre());
        }
        if (dineroReclamado > 0) {
            if (propiedadReclamada != null) mensaje.append(" y ");
            mensaje.append(dineroReclamado).append("€");
        }

        mensaje.append("?");

        consola.imprimir(mensaje.toString());
        consola.imprimir("Se ha propuesto el trato con ID: " + trato.getId() + " a " + receptor.getNombre());
    }

    // Método auxiliar para determinar si un elemento es dinero
    private boolean esDinero(String elemento) {
        try {
            Float.parseFloat(elemento.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void eliminarTrato(String idTrato) throws NoExisteTratoException {
        // Obtener el jugador actual
        Jugador jugadorActual = obtenerTurno();

        // Buscar el trato por ID en los tratos pendientes del jugador actual
        Trato trato = jugadorActual.buscarTratoPorId(idTrato);
        if (trato != null) {
            // Si no lo encuentra en el jugador actual, buscar en el otro jugador involucrado
            consola.imprimir("El trato entre "+ trato.getJugadorRecibe().getNombre()+
            " y "+  trato.getJugadorPropone().getNombre() + " se elimino \n");
            trato.getJugadorPropone().eliminarTrato(trato);
            trato.getJugadorRecibe().eliminarTrato(trato);

            return;
        }
        throw new NoExisteTratoException("No se encontró un trato con el ID proporcionado en ningún jugador.");

    }


}
