package monopoly;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;
import java.util.List;

import partida.Avatar;
import partida.Dado;
import partida.Jugador;

public class Menu {

    //Atributos
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

    private ArrayList<Carta> cartas_suerte;
    private ArrayList<Carta> cartas_caja;
    private Carta carta_del_reves;
    private boolean partidaTerminada; //Booleano para acabar la partida
    private int dadosDoblesSeguidos; //(DEL JUGADOR CON EL TURNO) Hay que diferenciar lanzamientos de este atributo

    /**Atributo que usa analizarComando() para saber qué comandos bloquear.
     * / 0->turno normal.
     * / 1->pelota avanzado cuando tira y tiene movimientos pendientes.
     * / 2->coche avanzado no puede mover este turno.
     * / 3->coche avanzado ya compró una propiedad.
     */
    private int controlComandos;



    //SECCIÓN DE CONSTRUIR EL MENÚ--------------------------------------------------------------------------------------

    public Menu(){
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

        anhadirBarajas();
        this.partidaTerminada = false;
        this.dadosDoblesSeguidos = 0;

        this.controlComandos = 0;
    }

    /**Método para generar las cartas de tipo Suerte y de tipo Caja de comunidad (más la carta dada la vuelta).*/
    private void anhadirBarajas() {
        this.cartas_suerte = new ArrayList<Carta>();
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_1, "Suerte", 1));
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_2, "Suerte", 2));
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_3, "Suerte", 3));
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_4, "Suerte", 4));
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_5, "Suerte", 5));
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_6, "Suerte", 6));
        this.cartas_caja = new ArrayList<Carta>();
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_1, "Caja", 1));
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_2, "Caja", 2));
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_3, "Caja", 3));
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_4, "Caja", 4));
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_5, "Caja", 5));
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_6, "Caja", 6));
        this.carta_del_reves = new Carta();
    }



    //SECCIÓN DE CONTROL DEL FLUJO DE LA PARTIDA------------------------------------------------------------------------

    /**Método principal del Monopoly.
     * [1] Mensaje de bienvenida y explicación del comando para crear jugadores.
     * [2] Pide crear jugadores hasta tener una cantidad válidad para empezar (2-6).
     * [3] Al introducirse el comando 'empezar partida' se llama a bucleTurno.
     * [4] bucleTurno gestiona todo el transcurso de la partida.
     * [5] Cuando se sale de bucleTurno es porque la partida ha terminado.
     * [6] Mensaje de final de partida y cerramos todo lo que toca.
     */
    public void iniciarPartida() {
        // Creamos un scanner para introducir comandos
        Scanner scanIniciarPartida = new Scanner(System.in);

        // Antes de empezar la partida hay que crear los jugadores
        setTextoTablero(Texto.BIENVENIDA);
        verTablero();

        // Bucle para crear los jugadores (máximo 6)
        // Dentro del propio bucle se empieza la partida
        while(!partidaTerminada) {

            String comando_entero = scanIniciarPartida.nextLine();
            String[] comando = comando_entero.split(" ");

            // IMPORTANTE comprobar la longitud para no acceder a un índice que no existe
            if (comando.length >= 4 && "crearjugador".equals(comando[0] + comando[1])) {
                // Si no tenemos ya 6 jugadores se deja crear un nuevo jugador
                if(this.jugadores.size()<7) {
                    // Comprobamos si se ha introducido un tipo de avatar válido
                    if(esTipoAvatar(comando[comando.length-1])) {
                        // El nombre del jugador puede contener varias palabras
                        String nombre_completo = "";
                        for(int i=2; i < comando.length-1; i++) {
                            // Quitando la primera, cada vez que se añada una palabra tiene que haber un espacio
                            if(i>2) {
                                nombre_completo += " ";
                            }
                            nombre_completo += comando[i];
                        }
                        // Creamos el jugador ahora que ya tenemos el nombre completo
                        crearJugador(nombre_completo, comando[comando.length-1]);
                    }
                    else {
                        System.out.println(Texto.M_TIPO_AVATAR_INVALIDO);
                    }
                }
                else {
                    System.out.println(Texto.M_PROHIBIDO_MAS_DE_6_JUGADORES);
                }
            }
            else if ("empezar partida".equals(comando_entero)) {

                //Si hay al menos 2 jugadores empezamos
                if(this.avatares.isEmpty()) {
                    System.out.println("Amig@ habrá que crear algún jugador antes de empezar no crees?");
                }
                else if(this.avatares.size()!=1) {
                    this.turno = 0; //El primer jugador creado tiene el turno

                    // Avisamos del inicio de la partida y cambiamos el texto dentro del tablero
                    System.out.printf(Texto.M_EMPIEZA_LA_PARTIDA + "\n", obtenerTurno().getNombre());
                    setTextoTablero(Texto.LISTA_COMANDOS);

                    //Este es el bucle de la partida básicamente: cada iteración es un turno
                    while(!this.partidaTerminada) {
                        bucleTurno();
                    }

                }
                else {
                    System.out.println("Creo que jugar una persona sola no tiene mucho sentido...");
                }

            } else {
                System.out.println(Texto.M_COMANDO_INVALIDO_INICIO);
            }

        }
        //Acabouse, liberar memoria estaría duro
        scanIniciarPartida.close();
        System.out.println("La partida ha finalizado, esperamos que disfrutáseis la experiencia.");
        System.exit(0);
    }

    /**Método que realiza las acciones asociadas al comando 'crear jugador'.
     * Solo se usa antes de empezar la partida, una vez empezada no se pueden crear más jugadores.
     * @param nombre Nombre del jugador
     * @param tipoAvatar Tipo de avatar
     */
    private void crearJugador(String nombre, String tipoAvatar) {

        String tipo = tipoAvatar.trim().toLowerCase();  // Eliminar espacios y convertir a minúsculas

        // Definir la casilla de inicio.
        Casilla casillaInicio = tablero.getCasilla(0);

        //Comprobamos que el tipo introducido es válido
        if(!esTipoAvatar(tipo)){
            System.out.println("Tipo de avatar incorrecto");
            return;
        }

        // Creamos el nuevo jugador con el tipo indicado
        Jugador nuevoJugador = new Jugador(nombre, tipo, casillaInicio, avatares);
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

    /**Método que gestiona el desarrollo de un turno
     * [1] Primero comprueba el atributo movimientos_pendientes del Menú por si estamos en un estado especial.
     * [2] Caso especial 1: un avatar tipo coche no puede tirar los dados este turno.
     * [3] Caso especial 2: a un avatar tipo pelota le faltan casillas por moverYEvaluar.
     * [4] Una vez gestionados esos casos llama a analizarComando() con el int que toque.
     */
    private void bucleTurno() {
        Scanner scanTurno = new Scanner(System.in);

        while (!partidaTerminada) {

            // DEJO ESTA PETADA COMENTADA POR SI ME HACE FALTA
            /**
            // Comprobamos si estamos en un estado especial
            if(!movimientosPendientesActual().isEmpty()) {
                // Caso especial 1: un avatar tipo coche no puede tirar los dados este turno.
                if(movimientosPendientesActual().get(0)==0) {
                    this.controlComandos=1;
                }
                // Caso especial 2: a un avatar tipo pelota le faltan casillas por moverYEvaluar.
                else {
                    // Recordar que esta maravillosa función ya llama a evaluarCasilla()
                    moverYEvaluar(obtenerTurno(), movimientosPendientesActual().get(0));
                    this.controlComandos=2; // En principio le voy a dar valores distintos por si no capan los mismo comandos
                }
            }
             */

            System.out.println();
            analizarComando(scanTurno.nextLine());

        }
    }

    /**Método para acabar la partida cuando alguien gana o no se quiere seguir.*/
    private void acabarPartida() {
        partidaTerminada = true;
    }



    //SECCIÓN DE MÉTODOS ÚTILES DEL MENÚ--------------------------------------------------------------------------------

    /**Método que devuelve el jugador que tiene el turno.*/
    public Jugador obtenerTurno() {
        return this.jugadores.get(this.turno);
    }

    /**Método para obtener el ArrayList de los movimientos pendientes del jugador actual directamente*/
    public ArrayList<Integer> movimientosPendientesActual() {
        return obtenerTurno().getMovimientos_pendientes();
    }

    /**Método que elimina al jugador correspondiente de la lista de jugadores.
     * Si se le pasa un jugador que no está en la lista de jugadores no hace nada.
     */
    public void eliminarJugador(Jugador jugador) {
        Iterator<Jugador> iterator = this.jugadores.iterator();
        while (iterator.hasNext()) {
            Jugador j = iterator.next();
            if (j.equals(jugador)) {
                iterator.remove(); // Elimina de manera segura el jugador actual
                // Si eliminamos al último jugador establecemos el turno del primero
                if(this.turno == this.jugadores.size()) {
                    this.turno = 0;
                }
                // Si sólo queda un jugador se acabó la partida!!!
                if(this.jugadores.size()==1) {
                    System.out.println("Increíble! Os ha dado tiempo a acabar una partida de Monopoly!" +
                            "\n¡¡Y el ganador es " + obtenerTurno().getNombre() + "!!");
                    this.partidaTerminada=true;
                }
            }
        }
    }

    //Se puede hacer desde aquí porque no existe encapsulación de sus elementos al ser un String[]
    /**Método para cambiar el texto que se muestra en medio del tablero.
     * Se presupone que el texto viene ya con los saltos de línea y cumple con el máximo de longitud de cada línea.
     * Si se intenta introducir un texto con más líneas de las posibles salta un error y no hace nada.
     */
    public void setTextoTablero(String nuevo_texto) {
        //Dividimos el String en partes en función de los saltos de línea
        String[] nuevo_texto_tablero = nuevo_texto.split("\n");

        if(nuevo_texto_tablero.length<17) {
            // Se empieza en el índice 1 porque la primera línea del centro del tablero se deja vacía
            // Nótese que para asignar bien el texto el índice de nuevo_texto_tablero es i-1
            for (int i = 1; i < nuevo_texto_tablero.length + 1; i++) {
                Texto.TABLERO[i] = nuevo_texto_tablero[i - 1];
            }
        }
        else {
            System.out.println("Se ha intentado meter más líneas de las que caben en el medio del tablero.");
        }
    }

    //SECCIÓN DE COMANDOS DEL MENÚ--------------------------------------------------------------------------------------

    /**Método que interpreta el comando introducido y toma la accion correspondiente.
     * En función del valor de control puede capar unas funciones u otras.
     * @param comando_entero Línea de comando que introduce el jugador
     */
    private void analizarComando(String comando_entero) {

        switch(comando_entero){
            // PRIMER BLOQUE DE COMANDOS: no dependen de una instancia----------------------------------------

            // Variado
            case "terminar partida": case "acabar partida": acabarPartida(); break;
            case "bancarrota": declararBancarrota(this.banca); break; // Se elimina al jugador de la partida!!
            case "ver tablero": verTablero(); break;
            case "jugador": infoJugadorTurno(); break;
            case "estadisticas": estadisticasGenerales(); break;
            case "salir carcel": salirCarcel(); break;
            case "ayuda": ayuda(); break;
            case "edificar pista de deporte": edificar("pista de deporte"); break;

            // Comandos de listar cosas
            case "listar enventa": listarVenta(); break;
            case "listar jugadores": listarJugadores(); break;
            case "listar avatares": listarAvatares(); break;
            case "listar edificios": listarEdificios(null); break;

            // Comandos que no se pueden ejecutar en ciertos casos-----------------------
            case "lanzar dados":
                if(this.controlComandos==0) {
                    lanzarDados(0, 0);
                }
                else {
                    System.out.println(Texto.M_COMANDO_BLOQUEADO);
                }
                break;

            case "acabar turno":
                // La pelota no puede pasar el turno si tiene movimientos pendientes
                if(this.controlComandos==1 && !movimientosPendientesActual().isEmpty()) {
                    System.out.println(Texto.M_MOVIMIENTOS_PENDIENTES);
                }
                else{
                    acabarTurno();
                }
                break;

            case "cambiar modo":
                if(!this.tirado){
                    cambiarModo();
                }
                else{
                    System.out.println("No se puede cambiar de modo una vez ya se ha tirado.");
                }
                 break;

            // Comando que se usa para ir avanzando en el movimiento con paradas de la pelota
            case "siguiente":
                if(this.controlComandos==1 && this.tirado && !movimientosPendientesActual().isEmpty()) {
                    // Cuando este comando tiene sentido hay movimientos pendientes del mismo turno, pues movemos
                    moverYEvaluar(obtenerTurno(), movimientosPendientesActual().get(0));
                    verTablero();
                    if(movimientosPendientesActual().isEmpty()) {
                        if(this.dado1.getValor()==this.dado2.getValor()) {
                            System.out.println(Texto.M_YA_SE_HICIERON_TODOS_LOS_MOVIMIENTOS_TIRADA);
                        }
                        else {
                            System.out.println(Texto.M_YA_SE_HICIERON_TODOS_LOS_MOVIMIENTOS_TURNO);
                        }
                    }
                }
                else {
                    System.out.println(Texto.M_COMANDO_BLOQUEADO);
                }
                break;


            // CHEATS----------------------------------------------------------
            // DINERO INFINITO (+mil millones)
            case "dinero infinito": dineroInfinito(); break;
            // AVANZAR 40 CASILLAS HASTA LA MISMA CASILLA
            case "dar vuelta": obtenerTurno().sumarVuelta(); break;
            // PROBANDO LA IMPRESIÓN DE CARTAS
            case "probar cartas": probarCartas(); break;
            case "coger carta caja": cogerCarta(this.cartas_caja); break;
            case "coger carta suerte": cogerCarta(this.cartas_suerte); break;

            // Algunos mensajes concretos a comandos inválidos
            case "empezar partida": System.out.println("¡La partida ya está empezada! \uD83D\uDE21"); break;


            // SEGUNDO BLOQUE DE COMANDOS: dependen de una instancia------------------------------------------
            default:
                //Dividimos el comando en partes
                String[] comando=comando_entero.split(" ");

                // IMPORTANTE: hacer las comprobaciones en función del número de palabras del comando
                // Si no podríamos querer acceder a un índice que no existe
                if(comando.length==1) {
                    System.out.println(comando_entero + " no es un comando válido.");
                }
                else if(comando.length==2) {

                    // Podría ser uno de los siguientes:
                    switch(comando[0]){

                        // Relacionados con casillas
                        case "describir": descCasilla(comando[1]); break;
                        // Cuando el coche avanzado ya compró una propiedad este turno no puede comprar más
                        case "comprar":
                            if(this.controlComandos!=3) {
                                comprar(comando[1]);
                            }
                            else {
                                System.out.println(Texto.M_UNA_CASILLA_POR_TURNO);
                            }
                            break;

                        // Comandos de edificar, hipotecar, deshipotecar (para pista de deporte en length==4)
                        case "edificar": edificar(comando[1]); break;
                        case "hipotecar": hipotecar(comando[1]); break;
                        case "deshipotecar": deshipotecar(comando[1]); break;

                        // Estadísticas de un jugador
                        case "estadisticas": estadisticasJugador(comando[1]); break;

                        // Comando inválido
                        default: System.out.println(comando_entero + " no es un comando válido.");
                    }
                }
                // Caso especial: describir jugador tiene un número variable por si el nombre es compuesto
                // Al llegar aquí ya se sabe que comando.length es como mínimo 3
                else if("describirjugador".equals(comando[0]+comando[1])) {
                    descJugador(comando);
                }
                // Mensaje de error por si se intenta crear un jugador con la partida empezada
                else if("crearjugador".equals(comando[0] + comando[1])){
                    System.out.println("No se pueden crear más jugadores con la partida empezada.");
                }
                else if(comando.length==3) {

                    if("describiravatar".equals(comando[0]+comando[1])) {
                        descAvatar(comando[2]);
                    }
                    else if("listaredificios".equals(comando[0]+comando[1])){
                        listarEdificios(comando[2]);
                    }
                    // CHEAT PARA SACAR LO QUE QUIERAS CON LOS DADOS
                    // Si alguno de los valores de dados introducidos no es válido
                    // se imprime un mensaje de error (lo hace dadoValido) y no hace nada
                    else if("dados".equals(comando[0])) {
                        if(this.controlComandos==0) {
                            int dado1 = dadoValido(comando[1]);
                            int dado2 = dadoValido(comando[2]);
                            // Si dado1 y dado2 no son números válidos se salta este if
                            if (dado1!=0 && dado2!=0) {
                                lanzarDados(dado1, dado2);
                            }
                        }
                        else {
                            System.out.println(Texto.M_COMANDO_BLOQUEADO);
                        }

                    }
                    else {
                        System.out.println(comando_entero + " no es un comando válido.");
                    }
                }
                else if(comando.length==4){
                    if(comando[0].equals("vender")){
                        // Desde aquí se permiten valores entre 1 y 6
                        int num_edificios = dadoValido(comando[3]);
                        if(num_edificios!=0) {
                            venderEdificios(comando[1], comando[2], num_edificios);
                        }
                    }
                    else{
                        System.out.println(comando_entero + " no es un comando válido.");
                    }
                }
                else {
                    System.out.println(comando_entero + " no es un comando válido.");
                }
        }
    }


    //SECCIÓN DE COMANDOS QUE NO DEPENDEN DE NINGUNA INSTANCIA----------------------------------------------------------

    /**Método que ejecuta todas las acciones relacionadas con el comando 'jugador'.
     * Imprime la información del jugador que tiene el turno.
     */
    private void infoJugadorTurno() {
        Jugador jugador = obtenerTurno(); // Obtener el jugador actual

        // Imprimir el nombre y el avatar en el formato requerido
        System.out.println("{\n\tnombre: " + jugador.getNombre() + ",");
        System.out.println("\tavatar: " + jugador.getAvatar().getId() + "\n}");
    }

    /**Método para comprobar si los jugadores llevan cuatro vueltas al tablero sin comprar.
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
            System.out.println("Todos los jugadores han dado 4 vueltas sin comprar! El precio de los solares aumenta.");
            reiniciarVueltasSinCompras();
        }
    }

    /**Método que reinicia las vueltas sin comprar a 0.*/
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
        System.out.printf("¡Al pasar por la salida ganaste %,.0f€!\n", Valor.SUMA_VUELTA);
        jugador.sumarFortuna(Valor.SUMA_VUELTA);
        jugador.sumarVuelta();
        jugador.sumarVueltas_sin_comprar();
        jugador.getEstadisticas().sumarDineroSalidaRecaudado(Valor.SUMA_VUELTA);
        cuatroVueltasSinCompras();
    }

    /**
     * Método que se llama cada vez que se pasa por la Salida. Marcha atras
     * [1] Avisa de la cantidad que se pierde y la resta a la fortuna del jugador correspondiente.
     */
    private void devolverCobrarSalida(Jugador jugador) {
        System.out.printf("¡Al pasar por la salida marcha atras perdiste %,.0f€!\n", Valor.SUMA_VUELTA);
        jugador.sumarFortuna(-Valor.SUMA_VUELTA);
        jugador.restarVuelta();
        jugador.setVueltas_sin_comprar(jugador.getVueltas_sin_comprar()-1);
        jugador.getEstadisticas().sumarDineroSalidaRecaudado(-Valor.SUMA_VUELTA);
    }

    /**Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
     * Hace las comprobaciones pertinentes: si realmente se pueden tirar los dados,
     * si el jugador está en la cárcel o no, etc.
     * Llama a la función moverYEvaluar() en caso de que el jugador pueda moverYEvaluar.
     * @param dadoTrucado1 Si se quiere obtener unos dados concretos se llama a la función con esos valores.
     *                     Si se quiere obtener unos dados aleatorios se llama con los valores (0,0)
     */
    private void lanzarDados(int dadoTrucado1, int dadoTrucado2) {

        //Obtenemos el avatar que tiene el turno
        Jugador jugador = obtenerTurno();
        Avatar avatar = jugador.getAvatar();

        // Comprobamos si acaba de ser encarcelado
        if (this.tirado && jugador.isEnCarcel() && jugador.getTiradasCarcel()==0) {
            System.out.println(Texto.M_RECIEN_ENCARCELADO);
        }
        // Un avatar coche puede llegar a tirar 4 veces en un mismo turno pero no más
        else if(this.lanzamientos==4) {
            System.out.println("No puedes tirar más de cuatro veces en el mismo turno.");
        }
        // Un avatar coche avanzado no puede tirar si sacó menos que 5
        else if(obtenerTurno().esCocheAvanzado() && this.tirado && this.dado1.getValor()+this.dado2.getValor()<5)  {
            System.out.println("Sacaste menos que 5, no puedes volver a tirar.");
        }
        else {
            // Comprobamos si aún no se ha tirado en el turno o vienes de haber sacado dobles
            // IMPORTANTE EL ORDEN DEL IF: el valor de los dados antes de la primera tirada de la partida es null
            if(!this.tirado || this.dado1.getValor()==this.dado2.getValor() ||
                    (obtenerTurno().esCocheAvanzado() && this.tirado && this.dado1.getValor()+this.dado2.getValor()>4)) {
                int resultado1, resultado2;
                if(dadoTrucado1==0 && dadoTrucado2==0) {
                    // Lanzamos 2 dados (aleatorios)
                    resultado1 = this.dado1.tirarDado();
                    resultado2 = this.dado2.tirarDado();
                }
                else {
                    // Son dados trucados
                    this.dado1.setValor(dadoTrucado1);
                    this.dado2.setValor(dadoTrucado2);
                    resultado1 = this.dado1.getValor();
                    resultado2 = this.dado2.getValor();
                }
                // Imprimimos los dados
                System.out.println("[" + resultado1 + "] [" + resultado2 + "]");
                // Actualizamos las stats~
                jugador.getEstadisticas().sumarVecesTirado(1);
                // Se vuelve a asignar true en segundas/terceras tiradas a no ser que hagamos un caso a parte
                this.tirado = true;
                this.lanzamientos += 1;

                // ¿Estás encarcelado?
                if(jugador.isEnCarcel()) {
                    // Si sacas dobles sales!
                    if(resultado1==resultado2) {
                        System.out.println("DOBLES!!\nSales de la cárcel.");
                        desencarcelar(jugador);
                    }
                    else {
                        // No sales a menos que sea la tercera vez que tiras, caso en el que estás obligado
                        // a pagar la fianza. Si no puedes te declaras en bancarrota
                        if(jugador.getTiradasCarcel()==2) {
                            System.out.println("No ha habido suerte con los dados, toca pagar la fianza...");
                            //Por cómo está implementado salirCarcel(), el comando no se puede usar si ya tiraste
                            this.tirado=false;
                            //Pagas la fianza y sales de la cárcel
                            salirCarcel();
                        }
                        else {
                            System.out.println("Continúas en la carcel.");
                            jugador.setTiradasCarcel(1+jugador.getTiradasCarcel());
                        }
                    }
                }
                else {
                    // No estás encarcelado: TURNO NORMAL
                    int sumaDados = resultado1+resultado2;

                    // Mensajes sobre dados dobles si toca
                    if(resultado1==resultado2) {
                        System.out.println("DOBLES!!");
                        this.dadosDoblesSeguidos++;
                        if(this.dadosDoblesSeguidos==3) {
                            jugador.encarcelar(this.tablero.getPosiciones());
                            System.out.println("Tres dobles son muchos, vas a la cárcel sin pasar por la salida.");
                            return;
                        }
                        // Vuelves a tirar a no ser: (1) que caigas en IrCarcel
                        // (2) que seas un avatar coche y sacases 2,2 o 1,1
                        else if(!avatar.getLugar().getNombre().equals("IrCarcel") &&
                                !(avatar.getTipo().equals("coche") && (sumaDados==4 || sumaDados==2) ) ) {
                            System.out.println("Vuelves a tirar.");
                        }
                    }
                    else{
                        // Reiniciamos el contador de dados dobles
                        this.dadosDoblesSeguidos = 0;
                    }

                    // Esta función es la cabra
                    moverYEvaluar(jugador, sumaDados);

                    // Si un avatar pelota está en modo avanzado y tiene movimientos pendientes le avisamos
                    if(this.controlComandos==1 && !movimientosPendientesActual().isEmpty()) {
                        System.out.println(Texto.M_MOVIMIENTOS_PENDIENTES);
                    }
                }
                // Si se ha podido tirar los dados imprimimos el tablero
                verTablero();
            }
            else {
                System.out.println(Texto.M_YA_SE_TIRO);
            }
        }
    }

    /**Método que mueve el avatar de jugador en el tablero (gestiona si está en modo normal o modo avanzado).
     * Llama a evaluarCasilla() para realizar las acciones que corresponden.
     * @param jugador Jugador cuyo avatar hay que moverYEvaluar
     * @param tirada Suma del resultado al tirar los dados (SE PRESUPONE UN VALOR ENTRE 2 Y 12)
     */
    private void moverYEvaluar(Jugador jugador, int tirada) {
        // Establecemos el avatar, ya que lo usaremos varias veces directamente
        Avatar avatar = jugador.getAvatar();
        // Guardamos la casilla de salida para luego
        Casilla origen = avatar.getLugar();

        // Boolean que nos va a servir para imprimir mejor los mensajes del movimiento que se realiza
        boolean primerMovimientoPelota = false;

        // Comprobamos si el jugador está en movimiento avanzado
        if(avatar.getMovimientoAvanzado()) {
            if(avatar.getTipo().equals("coche")) {
                /*
                SOLO PUEDE COMPRAR 1 CASILLA EN ESOS 3 LANZAMIENTOS MAXIMOS
                 */
                if(tirada>4) {
                    // El jugador puede volver a tirar hasta un total de cuatro veces (lo limita lanzarDados())
                    avatar.moverAvatar(this.tablero.getPosiciones(), tirada);
                    System.out.println("Has sacado más de 4! Vuelves a tirar!");
                }
                else {
                    // Si saca menos de un 4 retrocede ese número de casillas
                    avatar.moverAvatar(this.tablero.getPosiciones(), -tirada);
                    // No puede lanzar los dados ni este turno ni los 2 siguientes
                    jugador.dosTurnosSinTirar();
                    System.out.println(Texto.M_DOS_TURNOS_SIN_TIRAR);
                }
            }
            else if(avatar.getTipo().equals("pelota")) {

                if(movimientosPendientesActual().isEmpty()) {
                    // Cuando se llama a moverYEvaluar() al tirar los dados calculamos los movimientos pendientes
                    jugador.calcularMovimientosPendientes(tirada);
                    primerMovimientoPelota = true;
                    avatar.moverAvatar(this.tablero.getPosiciones(), movimientosPendientesActual().get(0));
                }
                else {
                    // Si se llama a moverYEvaluar() para seguir moviendo se hace el movimiento que toca directamente
                    avatar.moverAvatar(this.tablero.getPosiciones(), tirada);
                }

                obtenerTurno().eliminarMovimientoPendiente();

                // Si aún tiene movimientos pendientes ponemos controlComandos a 1
                // Si era el último movimiento pendiente ponemos el controlComandos a 0
                if(movimientosPendientesActual().isEmpty()) {
                    this.controlComandos=0;
                }
                else {
                    this.controlComandos=1;
                }
            }
            else {
                System.out.println("Modo avanzado de la esfinge y el sombrero no implementado. Movimiento normal.");
                avatar.moverAvatar(this.tablero.getPosiciones(), tirada);
            }
        }
        else {
            // Si está en movimiento normal simplemente movemos el valor de la tirada y evaluamos la casilla
            avatar.moverAvatar(this.tablero.getPosiciones(), tirada);
        }

        // Guardamos la casilla de destino
        Casilla destino = avatar.getLugar();

        // Avisamos del movimiento del jugador en el tablero
        if(tirada > 4 || !jugador.getAvatar().getMovimientoAvanzado()){
            if(obtenerTurno().esPelotaAvanzado() && primerMovimientoPelota) {
                // Si un avatar en modo avanzado saca 5 o más siempre avanza primero 1 casilla
                System.out.println("El avatar " + avatar.getId() + " avanza 5 casillas desde " +
                        origen.getNombre() + " hasta " + destino.getNombre() + ".");
            }
            else {
                System.out.println("El avatar " + avatar.getId() + " avanza " + tirada +
                        " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");
            }
        }
        else{
            if(obtenerTurno().esPelotaAvanzado() && primerMovimientoPelota) {
                // Si un avatar en modo avanzado saca 4 o menos siempre retrocede primero 1 casilla
                System.out.println("El avatar " + avatar.getId() + " retrocede 1 casilla desde" +
                        origen.getNombre() + " hasta " + destino.getNombre() + ".");
            }
            else {
                System.out.println("El avatar " + avatar.getId() + " retrocede " + (tirada>0 ? tirada : -tirada) +
                        " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");
            }
        }


        /*
        Si pasamos por la salida hay que cobrar!
        Nótese que "pasar por la salida" implica que origen.posicion>destino.posicion, pero para no confundirlo con
        retroceder casillas (por ejemplo de la 28 a la 24) le sumamos a destino.posicion el máximo número de casillas
        que se pueden retroceder: 4
         */
        if (origen.getPosicion()>destino.getPosicion()+4) {
            cobrarSalida(jugador);
        }
        /*
        Si en los movimientos avanzados de pelota y coche avanzados pasamos por la Salida marcha atrás devolvemos
        el dinero que se cobra al pasar por la salida ¡Pero sólo si ha cobrado alguna vez la salida!
        Nótese que "pasar por la salida marcha atrás" implica que destino.posicion>origen.posicion, pero para no
        confundirlo con avanzar casillas de manera normal (por ejemplo de la 24 a la 36) le sumamos a origen.posicion
        el valor máximo que se puede avanzar tirando los dados: 12
         */
        if( destino.getPosicion()>origen.getPosicion()+12 && jugador.getEstadisticas().getDineroSalidaRecaudado()>0) {
            // Este if no hace falta pero nunca está mal la programación defensiva
            if (obtenerTurno().esCocheAvanzado() || obtenerTurno().esPelotaAvanzado()) {
                devolverCobrarSalida(jugador);
            }
        }

        // EVALUAR
        // FALTA AÑADIR AQUÍ EL BUCLE DE BANCARROTA :)
        evaluarCasilla(avatar.getLugar());
    }

    /**Método para gestionar cuando un jugador no puede pagar un alquiler
     * [1] Comprueba si un jugador puede pagar un alquiler con su dinero (fortuna).
     * [2] Si puede devuelve TRUE.
     * [3] Si no pudiese pero tiene propiedades hipotecables se pide que las hipoteque.
     * [4] A cada propiedad que hipoteca se comprueba si ya puede pagar (y en ese caso devuelve TRUE).
     * [5] Si al acabar este proceso sigue sin poder pagar se le obliga a declararse en bancarrota.
     * @param pagador Jugador que tiene que pagar la cantidad
     * @param cobrador Jugador al que se tiene que pagar la cantidad
     * @param cantidad
     * @return true si se acaba pudiendo pagar, false si el jugador se tuvo que declarar en bancarrota
     */
    public boolean bucleBancarrota(Jugador pagador, Jugador cobrador, float cantidad) {
        if(cantidad>pagador.getFortuna()) {
            Scanner scannerBancarrota = new Scanner(System.in);

            // Mientras tenga propiedades hipotecables puede ganar dinero (también se puede declarar en bancarrota)
            while(pagador.tienePropiedadesHipotecables()) {
                System.out.println(Texto.M_NO_HAY_DINERO_OPCIONES);

                String comando_entero = scannerBancarrota.nextLine();
                String[] comando = comando_entero.split(" ");

                while(cantidad>pagador.getFortuna()) {
                    // Mientras no sea uno de estos tres el comando no es válido
                    while( !( comando_entero.equals("bancarrota") || comando[0].equals("hipotecar") ||
                            comando[0].equals("vender") ) ) {

                        System.out.println("Comando inválido.");
                    }
                    if(comando_entero.equals("bancarrota")) {
                        declararBancarrota(cobrador);
                        return false;
                    }
                    if(comando[0].equals("hipotecar")) {
                        hipotecar(comando[1]);
                    }
                    if(comando[0].equals("vender")) {
                        // Desde aquí se permiten valores entre 1 y 6
                        int num_edificios = dadoValido(comando[3]);
                        if(num_edificios!=0) {
                            venderEdificios(comando[1], comando[2], num_edificios);
                        }
                    }

                    // Si después de la operación ya puede pagar devolvemos true
                    if(cantidad<=pagador.getFortuna()) {
                        System.out.println("Ya conseguiste dinero para pagar! Realizas el pago.");
                        return true;
                    }
                }
            }

            // No tiene ni dinero ni propiedades hipotecables
            do{
                System.out.println(Texto.M_BANCARROTA_OBLIGATORIA);
            } while(!scannerBancarrota.nextLine().equals("bancarrota"));
            declararBancarrota(cobrador);
            return false;
        }
        else {
            return true;
        }
    }

    /** Método para evaluar qué hacer en una casilla concreta.
     * @param casilla Casilla que tenemos que evaluar
     * @return TRUE en caso de ser solvente (es decir, de cumplir las deudas), y FALSE en caso de no serlo
     */
    public boolean evaluarCasilla(Casilla casilla) {
        // Variables que vamos a necesitar
        Jugador jugadorActual = obtenerTurno();
        int tirada = this.dado1.getValor() + this.dado2.getValor();
        // Atributos de casilla que se usan varias veces (pa no andar llamando getters constantemente)
        String nombreCasilla = casilla.getNombre();
        Jugador duenhoCasilla = casilla.getDuenho();
        float impuestoCasilla = casilla.getImpuesto();
        //sumamos en uno las veces visitadas
        casilla.sumarVecesVisitada(1);

        if (jugadorActual != duenhoCasilla) {
            switch (casilla.getTipo()) {
                case "Solar":
                    if (!casilla.estaHipotecada()) {
                        if (duenhoCasilla != this.banca) {

                            float precio = casilla.evaluarAlquiler();
                            System.out.printf("%s debe pagarle el alquiler de %s a %s: %,.0f€\n",
                                    jugadorActual.getNombre(), nombreCasilla, duenhoCasilla.getNombre(), precio);

                            // Si puede pagarlo de alguna manera se cobra
                            if(bucleBancarrota(jugadorActual, duenhoCasilla, precio)) {
                                jugadorActual.pagar(duenhoCasilla, precio);
                                return true;
                            }
                            return false;

                        } else {
                            System.out.println("La casilla " + nombreCasilla + " está a la venta.\n");
                        }
                    } else {
                        System.out.println("La casilla " + nombreCasilla + " está hipotecada. No hay que pagar.");
                    }
                    break;

                case "Especial":
                    switch (nombreCasilla) {
                        case "Carcel":
                            System.out.println("Has caído en la Cárcel. Disfruta de la visita.");
                            break;
                        case "Parking":
                            //v2: ahora el bote del Parking se guarda en Parking.valor
                            System.out.printf("Has ganado el bote del Parking: %,.0f€\n", casilla.getValor());
                            jugadorActual.sumarFortuna(casilla.getValor());
                            jugadorActual.getEstadisticas().sumarDineroRecaudadoBote(casilla.getValor());
                            casilla.setValor(0f);
                            break;
                        case "IrCarcel":
                            System.out.println("Mala suerte, te vas a la cárcel.");
                            System.out.println("Vas directamente sin pasar por la Salida ni cobrar.");
                            jugadorActual.encarcelar(this.tablero.getPosiciones());
                            break;
                        case "Salida":
                            System.out.println("Has llegado a la casilla de Salida.");
                            break;
                        default:
                            System.out.println("Error en evaluarCasilla.");
                            return false;
                    }
                    return true;

                case "Transporte":
                    if (duenhoCasilla != this.banca) {
                        if (!casilla.estaHipotecada()) {
                            float precio = casilla.evaluarAlquiler();
                            System.out.printf("%s debe pagar el servicio de transporte a %s: %,.0f€\n",
                                    jugadorActual.getNombre(), duenhoCasilla.getNombre(), precio);

                            // Si puede pagarlo de alguna manera se cobra
                            if(bucleBancarrota(jugadorActual, duenhoCasilla, precio)) {
                                jugadorActual.pagar(duenhoCasilla, precio);
                                return true;
                            }
                            return false;

                        } else {
                            System.out.println("La casilla " + nombreCasilla + " está hipotecada. No hay que pagar.");
                        }
                    } else {
                        System.out.println("La casilla " + nombreCasilla + " está a la venta.");
                    }
                    break;

                case "Impuestos":
                    System.out.printf("Debes pagar tus impuestos a la banca: %,.0f€\n", impuestoCasilla);

                    // Si puede pagarlo de alguna manera se cobra
                    if(bucleBancarrota(jugadorActual, this.banca, impuestoCasilla)) {
                        jugadorActual.pagar(impuestoCasilla, this.banca);
                        this.tablero.getCasilla(20).sumarValor(impuestoCasilla);
                        return true;
                    }
                    return false;

                case "Servicio":
                    if (duenhoCasilla != this.banca) {
                        if (!casilla.estaHipotecada()) {
                            float precio = casilla.evaluarAlquiler(tirada);
                            System.out.printf("%s debe pagar el servicio a %s: %,.0f€\n",
                                    jugadorActual.getNombre(), duenhoCasilla.getNombre(), precio);

                            // Si puede pagarlo de alguna manera se cobra
                            if(bucleBancarrota(jugadorActual, duenhoCasilla, precio)) {
                                jugadorActual.pagar(duenhoCasilla, precio);
                                return true;
                            }
                            return false;

                        } else {
                            System.out.println("La casilla " + nombreCasilla + " está hipotecada. No hay que pagar.");
                        }
                    } else {
                        System.out.println("La casilla " + nombreCasilla + " está a la venta.");
                    }
                    break;

                case "Caja de comunidad":
                    cogerCarta(this.cartas_caja);
                    break;
                case "Suerte":
                    cogerCarta(this.cartas_suerte);
                    break;

                default:
                    System.out.println("Error en evaluarCasilla(): tipo de casilla inválido.");
                    return false;
            }
        } else {
            System.out.println("Esta casilla te pertenece.");
            if(casilla.getTipo().equals("Solar")){
                casilla.sumarVecesVisitadaPorDuenho(1);
            }
        }
        return true;
    }



    /**Método que resetea los atributos relacionados con la cárcel del jugador que se desencarcela.
     * También resetea el boolean de la tirada y el número de lanzamientos del turno.
     * Nótese que por ese motivo al salir de la cárcel puedes tirar los dados como un turno normal.
     * Lo usa salirCarcel pero tb se llama cuando sales por sacar dobles.
     * Por este motivo de que se use en varios casos NO SE COBRA LA FIANZA AQUÍ.
     * @param jugador Jugador que vamos a desencarcelar
     */
    private void desencarcelar(Jugador jugador) {
        jugador.setEnCarcel(false);
        jugador.setTiradasCarcel(0);
        this.tirado=false;
        this.lanzamientos=0;
    }
    
    /**Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'.
     * Si el jugador está encarcelado y aún no tiró en su turno, paga la fianza y sale de la cárcel.
     * NOTA: cuando sales de la cárcel puedes tirar como en un turno normal.
     * CASO ESPECIAL: si es el tercer turno que tiras los dados para salir de la cárcel y no sacas dobles estás obligado
     *  a pagar la fianza. En ese caso el método lanzarDados() llama a este método haciendo primero this.tirado=false.
     */
    private void salirCarcel() {
        //Establecemos el jugador actual
        Jugador jugador = this.jugadores.get(this.turno);
        if (jugador.isEnCarcel()) {
            // Si ya ha tirado este turno no puede pagar la fianza!
            if (!this.tirado) {
                if(jugador.getFortuna() >= Valor.SALIR_CARCEL) {
                    desencarcelar(jugador);
                    jugador.restarFortuna(Valor.SALIR_CARCEL);
                    this.banca.sumarFortuna(Valor.SALIR_CARCEL);
                    System.out.printf("%s paga la fianza de %,.0f € y sale de la cárcel. Puedes tirar los dados.\n",
                            jugador.getNombre(), Valor.SALIR_CARCEL);
                }
                else {
                    System.out.println("¡No tienes dinero suficiente para pagar la fianza!");
                }
            } else {
                System.out.println("Ya has lanzado los dados.");
            }

        } else System.out.println("El jugador " + jugador.getNombre() + " no está en la cárcel.");
    }

    /**Método que realiza las acciones asociadas al comando 'acabar turno'.*/
    private void acabarTurno() {
        // Si aún no tiraste este turno...
        if(!this.tirado) {
            // ...a no ser que tengas el turno actual bloqueado (no puedes lanzar dados) no puedes acabar el turno
            // IMPORTANTE: comprobar que el arraylist no esté vacío antes de intentar acceder a un elemento
            if( !(!movimientosPendientesActual().isEmpty() && movimientosPendientesActual().get(0)==0) ) {
                System.out.println("Aún no has lanzado los dados este turno!");
                return;
            }
        }
        // Si NO acabas de ser encarcelado...
        else if( !(obtenerTurno().isEnCarcel() && obtenerTurno().getTiradasCarcel()==0) ) {
            // ...vemos si es un coche en modo avanzado
            if(obtenerTurno().esCocheAvanzado()) {
                // Si es un coche en modo avanzado el único caso en el que no puede pasar turno es si saca >4
                if(this.dado1.getValor()+this.dado2.getValor()>4) {
                    System.out.println("Sacaste más que 4, tienes que volver a tirar.");
                    return;
                }
            }
            else {
                // Si no es un coche en modo avanzado NO puede acabar el turno si sacó dobles
                if (this.dado1.getValor()==this.dado2.getValor()) {
                    System.out.println("¡Sacaste dobles! Tienes que volver a tirar.");
                    return;
                }
            }
        }

        // CUANDO SE LLEGA AQUÍ EL JUGADOR SÍ PUEDE ACABAR EL TURNO

        // Si un avatar coche acaba un turno en el que no pudo tirar los dados...
        // ...eliminamos el primer elemento ya que es el que acabamos de gestionar este turno
        if(obtenerTurno().esCocheAvanzado()) {
            obtenerTurno().eliminarMovimientoPendiente();
        }

        // Vamos a pasar el turno: actualizamos los atributos correspondientes
        this.tirado = false; // Reiniciar el estado de "tirado"
        this.lanzamientos = 0; // Reiniciar los lanzamientos
        this.dadosDoblesSeguidos = 0; // Reiniciar el contador de dados dobles

        // Incrementar el turno y asegurar que no exceda el tamaño del array
        this.turno += 1;
        if (this.turno >= this.jugadores.size()) {
            this.turno = 0; // Reiniciar el turno si llegamos al final de la lista
        }

        // Imprimir el nombre del nuevo jugador actual
        System.out.println("El jugador actual es: " + obtenerTurno().getNombre());

        // Actualizamos controlComandos para el nuevo jugador que tiene el turno
        this.controlComandos = 0;
        if(!movimientosPendientesActual().isEmpty()) {
            // Si hay un 0 en movimientos_pendientes el jugador no va a poder mover este turno
            if(movimientosPendientesActual().get(0)==0) {
                this.controlComandos = 2;
                System.out.println("Este turno no puedes lanzar los dados.");
            }
        }

    }

    /**Método para cambiar el modo de movimiento del avatar que tiene el turno*/
    private void cambiarModo() {

        Avatar avatar = obtenerTurno().getAvatar();
        if(avatar.getMovimientoAvanzado()) {
            if(movimientosPendientesActual().isEmpty()){
                System.out.println("El avatar " + avatar.getId() + " vuelve el movimiento normal.");
                this.controlComandos=0;
                obtenerTurno().getAvatar().cambiarMovimiento();
            }else{
                System.out.println("El avatar " + avatar.getId() + " no puede cambiar de modo ya que esta bloqueado.");
            }

        } else {
            System.out.printf(Texto.M_ACTIVAR_MOVIMIENTO_AVANZADO + "\n", avatar.getId(), avatar.getTipo());
            obtenerTurno().getAvatar().cambiarMovimiento();
        }

    }

    /**Método que realiza las acciones asociadas al comando 'ver tablero'*/
    private void verTablero() {
        System.out.println(tablero.toString());
    }

    /**Método que realiza las acciones asociadas al comando 'listar jugadores'.*/
    private void listarJugadores() {
        for(Jugador j: jugadores){
            j.infoJugador();
        }
    }

    /**Método que realiza las acciones asociadas al comando 'listar avatares'.*/
    private void listarAvatares() {
        for (Avatar a : avatares) {
            if (a != null) {
                a.infoAvatar(); // Llama a la función infoAvatar para imprimir los detalles del avatar
            }
        }
    }

    /**Método que realiza las acciones asociadas al comando 'listar enventa'.*/
    private void listarVenta() {
        System.out.println("Propiedades en venta:");
        Casilla casilla_aux;
        for (int i = 0; i < 40; i++) {
            casilla_aux = tablero.getCasilla(i);
            if (casilla_aux.esTipoComprable() && casilla_aux.getDuenho() == banca) {
                System.out.printf("%s - Precio: %,.0f€\n", casilla_aux.getNombre(), casilla_aux.getValor());
            }
        }
    }



    //SECCIÓN DE COMANDOS QUE DEPENDEN DE UNA INSTANCIA-----------------------------------------------------------------

    //NOTA PARA LA SEGUNDA ENTREGA: añadiría las comprobaciones que hace comprarCasilla aquí
    //para que la función comprarCasilla se llame solo cuando ya se sabe que se puede comprar
    //pero bueno en general es lioso que se necesiten 2 métodos para hacer 1 cosa de esta manera
    /**Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * @param nombre Cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
        Casilla c=tablero.encontrar_casilla(nombre);

        // Comprobamos si la casilla existe y ya se ha tirado (se hacen otras comprobaciones dentro de comprarCasilla)
        if(c != null) {
            if (this.tirado || lanzamientos > 0) {
                //le paso el jugador que tiene el turno y eljugador 0 (la banca)
                Jugador jugador = obtenerTurno();
                if(c.getDuenho() == banca && jugador.getAvatar().getLugar()==c &&
                        (c.getValor() <= jugador.getFortuna()) && c.esTipoComprable()) {
                    reiniciarVueltasSinCompras();
                }
                c.comprarCasilla(jugador, this.banca);

                // Si un avatar tipo coche en modo avanzado compra ya no va a poder comprar más en el turno
                if(jugador.getAvatar().getTipo().equals("coche") && jugador.getAvatar().getMovimientoAvanzado()) {
                    this.controlComandos=3;
                }

            }
            else {
                System.out.println("¡Primero tienes que tirar!");
            }
        }
        else {
            System.out.println("No hay ninguna casilla que se llame " + nombre);
        }
    }

    /** Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
     * Imprime la información sobre la casilla correspondiente si es descriptible
     *
     * @param nombre_casilla Nombre de la casilla a describir
     */
    private void descCasilla(String nombre_casilla) {

        // Hay dos casos para los que no podemos llamar a infoCasilla: Carcel y Parking
        switch (nombre_casilla) {
            case "Carcel":
                // Imprimir el valor para salir de la cárcel
                System.out.printf("{\n\tPago para salir: %,.0f€\n", Valor.SALIR_CARCEL);

                // Si hubiera avatares se imprimen
                jugadoresEnCarcel();
                System.out.println("}");
                break;

            // Este caso hay que hacerlo desde aquí por no poder editar los argumentos de infoCasilla
            // ya que la fortuna de la banca es un valor que no se puede acceder desde la clase casilla
            case "Parking":
                // Imprimir el bote
                System.out.printf("{\n\tBote acumulado: %,.0f€\n", this.tablero.getCasilla(20).getValor());

                // Imprimimos los jugadores de la casilla si los hubiera
                // Línea jodida por como está implementado jugadoresEnCasilla pero confíen en el proceso
                System.out.print(tablero.encontrar_casilla(nombre_casilla).jugadoresEnCasilla());

                System.out.println("}");
                break;

            case "IrCarcel": case "Caja": case "Suerte":
                System.out.println("No se puede describir esta casilla.");
                // No es necesario imprimir nada aquí
                break;
                
            default:
                // Usamos infoCasilla si el nombre es válido entre los que nos queda por comprobar
                Casilla casilla = tablero.encontrar_casilla(nombre_casilla);
                if(casilla!=null) {
                    System.out.print(casilla.infoCasilla());
                }
                else {
                    System.out.println(nombre_casilla + " no es un nombre de casilla válido.");
                }
        }
    }

    /**Modificación del método jugadoresEnCasilla (Casilla.java) para la casilla Cárcel,
     * ya que se deben imprimir los turnos que llevan para salir o si están de visita.
     * Si no hay ningún jugador en la casilla no hace nada.
     * Nota: los imprime con salto de línea al final.
     */
    private void jugadoresEnCarcel() {

        // Obtenemos la lista de avatares que hay en la casilla cárcel (pos=10)
        ArrayList<Avatar> avataresEnCasilla = tablero.getCasilla(10).getAvatares();

        // Si hubiera avatares se imprimen
        if(!avataresEnCasilla.isEmpty()) {

            // Recorremos la lista de avatares y mostramos todos los jugadores en la misma línea
            System.out.print("\tJugadores: ");
            for (Avatar avatar : avataresEnCasilla) {
                Jugador jugador = avatar.getJugador();
                // Es diferente si está encarcelado que si está de visita
                if (jugador.isEnCarcel()) {
                    // Imprimimos el jugador que está encarcelado con el número de tiradas en la cárcel
                    System.out.print("[" + jugador.getNombre() + ", " + jugador.getTiradasCarcel() + "]  ");
                } else {
                    // Imprimimos el jugador que está de visita
                    System.out.print("[" + jugador.getNombre() + " (visita)]  ");
                }
            }
            System.out.print("\n");
        }
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
    private void descJugador(String[] partes) {
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
        if(jugador!=null) {
            jugador.infoJugador();
        }
        else {
            System.out.println("No se ha encontrado al jugador buscado.");
        }
        
    }
    
    /**Método que realiza las acciones asociadas al comando 'describir avatar'.
     * @param ID id del avatar a describir
     */
    private void descAvatar(String ID) {
        // lista llamada avatares se recorre
        for (Avatar avatar : avatares) { // Busca el avatar en la lista
            if (avatar.getId().equals(ID)) {
                avatar.infoAvatar(); // Salida si encuentra
                return;
            }
        }
        // Si no encuentra el avatar, muestra un mensaje de error
        System.out.println("Avatar con ID " + ID + " no encontrado.");
    }



    //SECCIÓN DE COMANDOS QUE DEPENDEN DE DOS INSTANCIAS----------------------------------------------------------------





    // SECCIÓN DE MÉTODOS RELACIONADOS CON LAS CARTAS TIPO SUERTE Y CAJA DE COMUNIDAD-----------------------------------

    /**Método para cuando se cae en una casilla de tipo Suerte o Caja de comunidad
     * [1] Reordena de manera aleatoria el ArrayList de cartas correspondiente
     * [2] Le pide al usuario el número de la carta que quiere escoger (del 1 al 6)
     */
    public void cogerCarta(ArrayList<Carta> baraja) {
        System.out.println("Barajando las cartas...");
        //Collections.shuffle(baraja); //Barajamos las cartas //ESTÁ COMENTADO PORQUE EN ESTA ENTREGA NO SE BARAJAN
        cartasAlReves(); //Mostramos el reverso de las cartas
        System.out.println("Escoge una carta con un número del 1 al 6.");
        int n=leerDadoValido(); //Leemos input hasta que sea un número válido
        mostrarCartaEscogida(baraja, n); //Volvemos a mostrar las cartas con la escogida dada la vuelta
        evaluarCarta(baraja.get(n-1)); //Ojo con los índices del ArrayList que empiezan en 0!!
    }

    /**
     * Función que dada una carta ejecuta las acciones que dice la misma.
     * @param carta Carta que tenemos que evaluar
     * @return TRUE si el jugador es solvente, FALSE en caso contrario
     */
    public boolean evaluarCarta(Carta carta) {
        Jugador jugadorActual = obtenerTurno();
        Avatar avatarActual = jugadorActual.getAvatar();
        int posicion = avatarActual.getLugar().getPosicion();

        if(carta.getTipo().equals("Suerte")) {
            switch(carta.getID()) {
                case 1: //Ir a Transportes1 (pos=5). Si pasas por la Salida cobrar
                    //Siempre se pasa por la salida ya que no hay ninguna casilla Suerte entre la Salida y Trans1
                    avatarActual.moverAvatar(this.tablero.getPosiciones(), 45-posicion);
                    cobrarSalida(jugadorActual);
                    evaluarCasilla(this.tablero.getCasilla(5));
                    break;
                case 2: //Ir a Solar15 (pos=26) sin pasar por la Salida (y por tanto sin cobrar)
                    avatarActual.moverAvatar(this.tablero.getPosiciones(), posicion<26 ? 26-posicion : 66-posicion);
                    evaluarCasilla(this.tablero.getCasilla(26));
                    break;
                case 3: //Cobrar 500.000€
                    jugadorActual.sumarFortuna(500000);
                    break;
                case 4: //Ir a Solar3 (pos=6). Si pasas por la Salida cobrar
                    //Siempre se pasa por la salida ya que no hay ninguna casilla Suerte entre la Salida y Solar3
                    avatarActual.moverAvatar(this.tablero.getPosiciones(), 46-posicion);
                    cobrarSalida(jugadorActual);
                    evaluarCasilla(this.tablero.getCasilla(6));
                    break;
                case 5: //Ir a la cárcel (encarcelado) sin pasar por la Salida (y por tanto sin cobrar)
                    avatarActual.moverAvatar(this.tablero.getPosiciones(), posicion<10 ? 10-posicion : 50-posicion);
                    jugadorActual.encarcelar(this.tablero.getPosiciones());
                    break;
                case 6: //Cobrar 1.000.000€
                    jugadorActual.sumarFortuna(1000000);
                    break;
            }
        }
        else if(carta.getTipo().equals("Caja")) {
            switch(carta.getID()) {
                case 1: //Pagar 500.000€ (a la banca)
                    if(bucleBancarrota(jugadorActual, this.banca, 500000f)) {
                        jugadorActual.pagar(500000f, this.banca);
                        this.tablero.getCasilla(20).sumarValor(500000f);
                        return true;
                    }
                    return false;
                case 2: //Ir a la cárcel (encarcelado) sin pasar por la Salida (y por tanto sin cobrar)
                    avatarActual.moverAvatar(this.tablero.getPosiciones(), posicion<10 ? 10-posicion : 50-posicion);
                    jugadorActual.encarcelar(this.tablero.getPosiciones());
                    break;
                case 3: //Ir a Salida (pos=0=40) y cobrar
                    avatarActual.moverAvatar(this.tablero.getPosiciones(), 40-posicion);
                    cobrarSalida(jugadorActual);
                    break;
                case 4: //Cobrar 2.000.000€
                    jugadorActual.sumarFortuna(2000000);
                    break;
                case 5: //Pagar 1.000.000€ (a la banca)
                    if(bucleBancarrota(jugadorActual, this.banca, 1000000f)) {
                        jugadorActual.pagar(1000000f, this.banca);
                        this.tablero.getCasilla(20).sumarValor(1000000f);
                        return true;
                    }
                    return false;
                case 6: //Pagar 200.000€ a cada jugador
                    float total_a_pagar = 200000 * (this.jugadores.size()-1);
                    if(bucleBancarrota(jugadorActual, this.banca, total_a_pagar)) {
                        jugadorActual.restarFortuna(total_a_pagar);
                        this.tablero.getCasilla(20).sumarValor(500000);
                        //Recorremos el ArrayList de jugadoresda: si no es el jugador actual sumamos 200.000€
                        for(Jugador j : this.jugadores) {
                            if(!j.equals(jugadorActual)) {
                                j.sumarFortuna(200000);
                            }
                        }
                        return true;
                    }
                    return false;
            }
        }
        else {
            System.out.println("Error en evaluarCarta(): esta carta tiene un tipo inválido.");
        }

        // Si llega a aquí es que el jugador es solvente
        return true;
    }

    /**Método para imprimir 6 cartas al revés en fila.*/
    private void cartasAlReves() {
        // Vamos imprimiendo línea por línea
        for(int i=0; i<Valor.NLINEAS_CARTA; i++) {
            // Función repeat() muy útil pa este caso
            System.out.println(this.carta_del_reves.getTexto().get(i).repeat(6));
        }
    }

    /**
     * Método para imprimir 6 cartas, todas al revés menos la que indica el índice
     * @param baraja Baraja de cartas de entre las cuales se escoge
     * @param n Posición de la carta que se escoge en el ArrayList
     */
    private void mostrarCartaEscogida(ArrayList<Carta> baraja, int n) {
        // Para cada línea...
        for(int i=0; i<Valor.NLINEAS_CARTA; i++) {
            // ...iteramos para cada carta (en total 6)...
            for(int j=0; j<6; j++) {
                // ...vemos si la carta es la que escogió el jugador o no
                // Nótese que el jugador escoge del 1 al 6 pero los índices empiezan en 0
                if(j==n-1) {
                    System.out.print(baraja.get(n-1).getTexto().get(i));
                }
                else {
                    System.out.print(this.carta_del_reves.getTexto().get(i));
                }
            }
            System.out.println(); //Imprimimos un salto de línea al haber iterado las 6 cartas
        }
    }



    //SECCIÓN DE CHEATS DE MENÚ-----------------------------------------------------------------------------------------

    /**Método para conseguir mucho dinero.*/
    private void dineroInfinito() {
        Jugador jugador = obtenerTurno();
        jugador.sumarFortuna(1000000000); //mil millones
    }

    /**PRUEBA DE CÓMO QUEDAN LAS CARTAS*/
    private void probarCartas() {
        // CARTAS SUERTE
        for(int j=0; j<11; j++) {
            for(int i=0; i<6; i++) {
                System.out.print(this.cartas_suerte.get(i).getTexto().get(j));
            }
            System.out.println();
        }
        // CARTAS CAJA
        for(int j=0; j<11; j++) {
            for(int i=0; i<6; i++) {
                System.out.print(this.cartas_caja.get(i).getTexto().get(j));
            }
            System.out.println();
        }
    }

    // MÉTODOS SIN GRUPO:

    /**Método que transforma un String a un entero si el String es un número entre 1 y 6.
     * Si no es un número válido imprime un mensaje de error y devuelve 0.
     * Método auxiliar para leerDadoValido() pero también sirve para lanzarDados().
     */
    public int dadoValido(String numero) {
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
                System.out.println("Número inválido.");
                return 0;
            }
        }
        else {
            System.out.println("Número inválido.");
            return 0;
        }
    }

    /**Método que lee input hasta que el valor introducido sea un número entre 1 y 6.*/
    public int leerDadoValido() {
        // Creamos un escaneador para introducir el número
        Scanner scannerDado = new Scanner(System.in);
        int n=0;

        // Bucle que para cuando metemos un número entre 1 y 6 por teclado
        while(n==0) {
            String numero = scannerDado.nextLine();
            // dadoValido() transforma el String en un número
            // Si no es un número válido devuelve 0
            n=dadoValido(numero);
        }

        //scannerDado.close();
        return n;
    }

    private boolean esTipoAvatar(String tipo) {
        return "coche".equals(tipo) ||
                "esfinge".equals(tipo) ||
                "sombrero".equals(tipo) ||
                "pelota".equals(tipo);
    }

    /**Método que imprime las estadísticas de un jugador de la partida.*/
    private void estadisticasJugador(String nombre_jugador) {
        Jugador jugador=encontrarJugador(nombre_jugador);

        if(jugador!=null) {
            jugador.infoEstadisticas();
        }
        else {
            System.out.printf("No se ha encontrado el jugador %s.\n", nombre_jugador);
        }
    }

    /**
     * Método que imprime las estadísticas generales de la partida.
     * Muestra información como las casillas más visitadas, casillas más rentables,
     * grupos más rentables, jugadores con más vueltas, tiradas y fortuna.
     */
    private void estadisticasGenerales() {
        System.out.println("{");


        //.clear() limpia la lista
        //.add()añade a la lista


        // Casillas más visitadas
        List<String> nombresCasillas = new ArrayList<>();
        int vecesMaxima = 0;

        // Determinar la cantidad máxima de visitas entre todas las casillas
        for (int i = 0; i < 40; i++) {
            Casilla casillaAux = tablero.getCasilla(i);

            // Si la casilla tiene más visitas que el máximo actual, se actualiza el máximo
            if (casillaAux.getVecesVisitada() > vecesMaxima) {
                vecesMaxima = casillaAux.getVecesVisitada();
                nombresCasillas.clear(); // Reinicia la lista con la nueva casilla más visitada
                nombresCasillas.add(casillaAux.getNombre());
            }
            // Si tiene el mismo número de visitas que el máximo, se añade a la lista
            else if (casillaAux.getVecesVisitada() == vecesMaxima) {
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
            if (casillaAux.getDinero_recaudado() > recaudacionMaxima) {
                recaudacionMaxima = casillaAux.getDinero_recaudado();
                nombresRentables.clear();
                nombresRentables.add(casillaAux.getNombre());
            }
            // Si la recaudación es igual a la máxima, verificar su tipo y añadirla si aplica
            else if (casillaAux.getDinero_recaudado() == recaudacionMaxima) {
                if (casillaAux.getTipo().equals("Solar") || casillaAux.getTipo().equals("Servicio")
                        || casillaAux.getTipo().equals("Transporte")) {
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
            if (casillaAux.getTipo().equals("Solar")) {
                float recaudacionGrupo = casillaAux.getGrupo().getRecaudacionGrupo();

                // Actualizar si el grupo tiene una mayor recaudación
                if (recaudacionGrupo > recaudacionGrupoMax) {
                    recaudacionGrupoMax = recaudacionGrupo;
                    gruposRentables.clear();
                    gruposRentables.add(casillaAux.getGrupo().getColorGrupo());
                }
                // Si la recaudación es igual al máximo, añadir el grupo si no está en la lista
                else if (recaudacionGrupo == recaudacionGrupoMax) {
                    if (!gruposRentables.contains(casillaAux.getGrupo().getColorGrupo())) {
                        gruposRentables.add(casillaAux.getGrupo().getColorGrupo());
                    }
                }
            }
        }

        // Imprimir los resultados de las estadísticas
        System.out.println("Casillas más visitadas: " + String.join(", ", nombresCasillas));
        System.out.println("Casillas más rentables: " + String.join(", ", nombresRentables));
        System.out.println("Grupos más rentables: " + String.join(", ", gruposRentables));
        System.out.println("Jugadores con más vueltas: " + String.join(", ", jugadoresVueltas));
        System.out.println("Jugadores con más tiradas: " + String.join(", ", jugadoresDados));
        System.out.println("Jugadores con más fortuna: " + String.join(", ", jugadoresRicos));

        System.out.println("\n}");
    }
    public static boolean esEdificioValido(String tipo) {
        return tipo.equals("casa") ||
                tipo.equals("hotel") ||
                tipo.equals("pista de deporte") ||
                tipo.equals("piscina");
    }



    //SECCIÓN DE MÉTODOS QUE HIZO ADRI LA ÚLTIMA VEZ Y NO ESTÁN BIEN ORDENADOS------------------------------------------
    public void declararBancarrota(Jugador cobrador) {
        Jugador jugador = obtenerTurno();

        if (cobrador.equals(this.banca)) {
            System.out.println("El jugador " + jugador.getNombre() + " se declara en bancarrota. " +
                    "Sus propiedades pasan a estar de nuevo en venta al precio al que estaban.");
            ArrayList<Casilla> propiedades = new ArrayList<>(jugador.getPropiedades());

            for (Casilla c : propiedades) {
                if(c.getTipo().equals("Solar")){
                    c.eliminarTodosLosEds();
                }

                c.setDeshipotecada();
                jugador.eliminarPropiedad(c);
            }
        } else {
            System.out.println("El jugador " + jugador.getNombre() + " se declara en bancarrota. " +
                    "Sus propiedades pasan a ser de " + cobrador.getNombre());
            ArrayList<Casilla> propiedades = new ArrayList<>(jugador.getPropiedades());

            for (Casilla c : propiedades) {

                if (c.getTipo().equals("Solar")){
                    c.eliminarTodosLosEds();
                }
                c.setDeshipotecada();
                cobrador.anhadirPropiedad(c);
                jugador.eliminarPropiedad(c);
            }
        }

        eliminarJugador(jugador);
    }



    public void hipotecar(String nombre){
        Casilla casilla= tablero.encontrar_casilla(nombre);
        Jugador jugador= obtenerTurno();


        if(casilla.getDuenho().equals(jugador)){
            if(casilla.esHipotecable()){
                System.out.println("El jugador "+ jugador.getNombre()+" recibe "+ casilla.getHipoteca()+
                        " por la hipoteca de " + casilla.getNombre()+
                        ". No puede recibir alquileres ni edificar en el grupo "+ casilla.getGrupo().getColorGrupo());
                jugador.sumarFortuna(casilla.getHipoteca());
                
            }
        }
        else{
            System.out.println("El jugador "+jugador.getNombre() +" no puede hipotecar "
                    + casilla.getNombre()+". No es una propiedad que le pertenezca.");
        }
    }

    public void deshipotecar(String nombre){
        Casilla casilla = tablero.encontrar_casilla(nombre);
        Jugador jugador =obtenerTurno();
        if(casilla.getDuenho().equals(jugador)){
            if(casilla.getHipoteca()<=jugador.getFortuna()){
                if(casilla.esDesHipotecable()){
                    System.out.println("El jugador "+ jugador.getNombre()+" paga "+ (casilla.getHipoteca()+casilla.getHipoteca()*0.10f)+
                            " por la hipoteca de " + casilla.getNombre()+
                            ". Ahora puede recibir alquileres y edificar en el grupo "+ casilla.getGrupo().getColorGrupo());
                    jugador.sumarFortuna(-(casilla.getHipoteca()+casilla.getHipoteca()*0.10f));
                    jugador.sumarGastos(casilla.getHipoteca()+casilla.getHipoteca()*0.10f);
                }
            }
            else{
                System.err.println("No tienes suficiente dinero como para deshipotecar la casilla.");
            }
        }
        else{
            System.out.println("El jugador "+jugador.getNombre() +" no puede deshipotecar "
                    + casilla.getNombre()+". No es una propiedad que le pertenezca.");
        }

    }


    public void edificar(String tipo) {
        if (esEdificioValido(tipo)) {
            if (obtenerTurno().getAvatar().getLugar().getTipo().equals("Solar")) {
                Jugador jugador = obtenerTurno(); // Obtener el jugador cuyo turno es actualmente
                Casilla casilla = jugador.getAvatar().getLugar(); // Obtener la casilla en la que se encuentra el jugador

                // En un solar se puede construir una casa si dicho solar pertenece al jugador cuyo avatar se encuentra
                // en la casilla y si (1) el avatar ha caído más de dos veces en esa misma casilla o (2) el jugador posee el
                // grupo de casillas al que pertenece dicha casilla.
                if ((casilla.getVecesVisitadaPorDuenho() > 2 || casilla.getGrupo().esDuenhoGrupo(jugador))) {
                    if (casilla.getDuenho().equals(jugador)) {
                        if (casilla.esEdificable(tipo, jugador)) {
                            Edificio edificio = new Edificio(tipo, casilla);

                            if (jugador.getFortuna() >= edificio.getCoste()) {
                                casilla.anhadirEdificio(edificio); // Añadir el edificio a la casilla
                                jugador.sumarGastos(edificio.getCoste()); // Restar el coste del edificio
                                jugador.sumarFortuna(-edificio.getCoste()); // Reducir la fortuna del jugador

                                System.out.println("El jugador " + jugador.getNombre() + " ha comprado el edificio " +
                                        edificio.getId() + " por " + edificio.getCoste());

                                // Si es un hotel, eliminar todas las casas de la casilla
                                if (tipo.equals("hotel")) {
                                    casilla.eliminarCasasDeCasilla();
                                }
                            } else {
                                System.out.println("No tienes suficiente dinero para edificar.");
                            }
                        } else {
                            System.out.println("No puedes edificar en esta casilla porque no cumple los requisitos necesarios.");
                        }
                    } else {
                        System.out.println("No puedes edificar en esta casilla porque no te pertenece.");
                    }
                } else {
                    System.out.println("Para edificar debes ser dueño del grupo o haber caído más de dos veces en la casilla siendo el propietario.");
                }
            } else {
                System.out.println("No puedes edificar en una casilla que no es de tipo solar.");
            }
        } else {
            System.out.println("El tipo de edificio " + tipo + " no es válido para edificar.");
        }
    }


    public void listarEdificios(String color) {
        // Si no se proporciona un color, lista todos los edificios
        if (color == null) {
            for (Jugador j : jugadores) {
                for (Casilla c : j.getPropiedades()) {
                    // Lista las edificaciones de la casilla
                    System.out.println(c.listarEdificaciones());
                }
            }
        } else {
            // Si se proporciona un color, lista solo los edificios del grupo de ese color
            for (Jugador j : jugadores) {
                for (Casilla c : j.getPropiedades()) {
                    // Si el color del grupo coincide con el proporcionado, listar las edificaciones
                    if (c.getGrupo().getColorGrupo().equalsIgnoreCase(color)) {
                        System.out.println(c.listarEdificaciones());
                    }
                }
            }
        }
    }

    public void venderEdificios(String tipo, String solar, int n) {
        Jugador jugador = obtenerTurno(); // Obtener el jugador cuyo turno es
        Casilla casilla =tablero.encontrar_casilla(solar);

        // Verificar si la casilla es del tipo correcto (solar)
        if (casilla.getDuenho().equals(jugador)) {
            if (casilla.getTipo().equals("Solar")) {
                ArrayList<Edificio> eds = casilla.getEdificiosPorTipo(tipo);
                int tamanho = eds.size();

                if (tamanho >= n) {
                    float suma = 0.0f;
                    Iterator<Edificio> iterator = eds.iterator();
                    int count = 0;

                    // Usamos el iterador para eliminar los edificios, motivo nº 01974182347123 de porqé odio java
                    while (iterator.hasNext() && count < n) {
                        Edificio edificio = iterator.next();
                        suma += (edificio.getCoste())/2;
                        iterator.remove();
                        count++;
                    }

                    jugador.sumarFortuna(suma);
                    System.out.println("El jugador " + jugador.getNombre() + " ha vendido " +
                            n + " " + tipo + " en " + solar + ", recibiendo " + suma +
                            "€. En la propiedad queda " + eds.size() + " " + tipo + ".");
                }
                else if(tamanho==0){
                    System.out.println("No puedes vender ninguna propiedad del tipo "+tipo+
                            " porque no hay ninguna en la casilla "+ solar);
                }

                else {
                    System.out.println("Solamente se puede vender " + eds.size() + " " + tipo +
                            ", recibiendo " + (eds.size() > 0 ? (eds.get(0).getCoste() / 2) * eds.size() : 0) + "€.");
                }
            } else {
                System.out.println("Tipo de edificio inválido (correctos: casa/hotel/piscina/pista de deporte).");
            }
        } else {
            System.out.println("No puedes vender las edificaciones de esta propiedad porque no te pertenece.");
        }
    }
    private void ayuda() {
        System.out.println("Lista de comandos disponibles:");
        System.out.println("- terminar partida / acabar partida: Termina la partida.");
        System.out.println("- bancarrota: Declararse en bancarrota.");
        System.out.println("- ver tablero: Muestra el estado actual del tablero.");
        System.out.println("- jugador: Muestra la información del jugador actual.");
        System.out.println("- estadisticas: Muestra estadísticas generales.");
        System.out.println("- salir carcel: Salir de la cárcel.");
        System.out.println("- listar enventa: Lista las casillas en venta.");
        System.out.println("- listar jugadores: Lista todos los jugadores.");
        System.out.println("- listar avatares: Lista todos los avatares.");
        System.out.println("- listar edificios: Lista todos los edificios.");
        System.out.println("- lanzar dados: Lanza los dados (si está permitido).");
        System.out.println("- acabar turno: Finaliza el turno del jugador actual.");
        System.out.println("- cambiar modo: Cambia el modo del avatar.");
        System.out.println("- siguiente: Realiza el siguiente movimiento.");
        System.out.println("- dinero infinito: Activa el modo dinero infinito.");
        System.out.println("- dar vuelta: Avanza 40 casillas.");
        System.out.println("- probar cartas: Prueba la impresión de cartas.");
        System.out.println("- coger carta caja: Coge una carta de Caja de Comunidad.");
        System.out.println("- coger carta suerte: Coge una carta de Suerte.");
        System.out.println("- describir [nombre_casilla]: Describe la casilla indicada.");
        System.out.println("- comprar [nombre_casilla]: Compra la casilla indicada.");
        System.out.println("- edificar [nombre_casilla]: Construye un edificio.");
        System.out.println("- hipotecar [nombre_casilla]: Hipoteca una propiedad.");
        System.out.println("- deshipotecar [nombre_casilla]: Deshipoteca una propiedad.");
        System.out.println("- estadisticas [nombre_jugador]: Muestra estadísticas de un jugador.");
        System.out.println("- describir jugador [nombre_jugador]: Describe un jugador.");
        System.out.println("- crear jugador [nombre_avatar]: Crea un jugador (solo antes de empezar la partida).");
        System.out.println("- describir avatar [Letra avatar]: Describe un avatar.");
        System.out.println("- listar edificios [gruoi]: Lista edificios de un tipo.");
        System.out.println("- dados [valor1] [valor2]: Lanza dados con valores específicos.");
        System.out.println("- vender [nombre_propiedad] [tipo_edificio] [cantidad]: Vende edificios.");
        System.out.println("- edificar/deshipotecar/hipotecar [Tipo edificio]e: Gestiona pistas de deporte.");
    }


}