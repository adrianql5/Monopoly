package monopoly;

import java.util.ArrayList;
import java.util.Scanner;

import partida.Avatar;
import partida.Dado;
import partida.Jugador;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.

    private boolean partidaTerminada =false;
    private boolean partidaEmpezada = false;

    public Menu(){
        iniciarPartida();
    }


    // Método para inciar una partida: crea los jugadores y avatares.
    private void iniciarPartida() {
        this.dado1= new Dado();
        this.dado2= new Dado();
        Jugador banca = new Jugador();

        this.jugadores = new ArrayList<Jugador>();
        this.avatares = new ArrayList<Avatar>();
        
        //lathis.jugadores.add(banca);
       // this.avatares.add(null);

        this.tablero=new Tablero(banca);
        
        this.turno=0;
        Scanner scan= new Scanner(System.in);
        
        System.out.println("La partida ha iniciado, esperamos que disfruteis la experiencia.");


        while (!partidaTerminada){
            System.out.println("\n ");
            analizarComando(scan.nextLine());

        }
        scan.close();
        terminarPartida();

    }

    private void terminarPartida(){
        System.out.println("La partida ha finalizado, esperamos que disfrutáseis la experiencia.");
        System.exit(0);
    }


    /**Método que interpreta el comando introducido y toma la accion correspondiente.
     * @param comando_entero Línea de comando que introduce el jugador
     */
    private void analizarComando(String comando_entero) {

        switch(comando_entero){
            //Primer bloque de comandos: no dependen de una instancia

            case "terminar":
                partidaTerminada= true;
                break;


            case "iniciar":
                iniciarPartida();
                break;

            //Indicar jugador que tiene el turno
            case "jugador":
                jugadorTurno();
                break;

            //Lanzar los dados
            case "lanzar dados":
                lanzarDados();
                verTablero();
                break;

            //Salir de la cárcel
            case "salir carcel":
                salirCarcel();
                break;

            //Pasar el turno al siguiente jugador
            case "acabar turno":
                acabarTurno();
                break;

            //Imprimir el tablero
            case "ver tablero":
                verTablero();
                break;

            //Listar todas las propiedades en venta
            case "listar enventa":
                listarVenta();
                break;

            //Listar todas las propiedades en venta
            case "listar jugadores":
                listarJugadores();
                break;

            //Listar todas las propiedades en venta
            case "listar avatares":
                listarAvatares();
                break;

            //Segundo bloque de comandos: dependen de una instancia
            default:
                //Dividimos el comando en partes
                String[] comando=comando_entero.split(" ");
                //REVISAR COMO SE COMPRUEBAN LOS ERRORES

                if(comando.length==2) {

                    //Podría ser uno de los siguientes:
                    switch(comando[0]){

                        //Para avanzar cualquier número de casillas de forma manual
                        case "avanzar":
                            //Importante en este caso convertir comando[1] a un int primero
                            //También es cierto que esto se podría convertir en el propio método
                            int ncasillas = Integer.parseInt(comando[1]);
                            //Ahora sí podemos usar el método avanzar
                            avanzar(ncasillas);
                            verTablero();
                            break;

                        //Para comprar una casilla
                        case "comprar":
                            comprar(comando[1]);
                            break;

                        //Para describir una casilla
                        case "describir":
                            descCasilla(comando[1]);
                            break;

                        default:
                            System.out.println(comando_entero + " no es un comando válido.\n");
                            break;
                    }
                }
                else if(comando.length==3) {

                    //De momento solo los hay de tipo "describir"
                    if("describiravatar".equals(comando[0]+comando[1])) {
                        descAvatar(comando[2]);
                    }
                    else if("describirjugador".equals(comando[0]+comando[1])) {
                        descJugador(comando); //El método pide que se pasen todas las partes aunque sólo haga falta un String
                    }
                    else {
                        System.out.println(comando_entero + " no es un comando válido.\n");
                    }
                }
                else if(comando.length==4) {

                    //De momento solo hay un comando de cuatro palabras:
                    if("crearjugador".equals(comando[0]+comando[1])) {
                        crearJugador(comando[2],comando[3]);
                        break;
                    }
                    else {
                        System.out.println(comando_entero + " no es un comando válido.\n");
                    }
                }
                else {
                    System.out.println(comando_entero + " no es un comando válido.\n");
                }

        }
    }


    //IMPLEMENTACIÓN DE LOS MÉTODOS
    //Métodos de comandos que no dependen de una instancia

    /**Método que ejecuta todas las acciones relacionadas con el comando 'jugador'.*/
    private void jugadorTurno() {
        Jugador jugador = obtenerTurno(); // Obtener el jugador actual

        // Imprimir el nombre y el avatar en el formato requerido
        System.out.println("{");
        System.out.println("    nombre: " + jugador.getNombre() + ",");
        System.out.println("    avatar: " + jugador.getAvatar().getId());
        System.out.println("}");
    }

    /**Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.*/
    private void lanzarDados() {
        System.out.println("{");
        Jugador jugador = obtenerTurno();
        Avatar avatar = jugador.getAvatar();
        int resultado1 = dado1.tirarDado();
        int resultado2 = dado2.tirarDado();
        Casilla salida = avatar.getLugar();
        if(resultado2 ==resultado1){
            if (jugador.isEnCarcel()){
                System.out.println("Sales de la carcel");
                jugador.setEnCarcel(false);
            }
        }
        else if (jugador.isEnCarcel()) {

            System.out.println("Continúas en la carcel.");
            jugador.sumarTiradaCarcel();
            System.out.println("}");
            return;}
        int suma_ambas = resultado1 + resultado2;


        avatar.moverAvatar(tablero.getPosiciones(),  suma_ambas);

        Casilla destino = avatar.getLugar();

        System.out.println("El avatar " + avatar.getId() + " avanza " + (suma_ambas) + " casillas desde " + salida.getNombre() + " hasta " + destino.getNombre());

        if (destino.getNombre().equals("IrCarcel")){
            Casilla carcel = tablero.encontrar_casilla("Carcel"); // Asumiendo que existe la función en Tablero

            jugador.getAvatar().setLugar(carcel);
            jugador.setEnCarcel(true);
        }

        System.out.println("}");
    }

    /**Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. */
    private void salirCarcel() {//saca al avatar de la carcel
        /*
         * Maria paga 500000€ y sale de la cárcel. Puede lanzar los dados.
         */

    }

    /**Método que realiza las acciones asociadas al comando 'acabar turno'.*/
    private void acabarTurno() {//estoy cambia del avatar actual al siguiente y devuelve esto
        /*
         * El jugador actual es Maria. donde maría es el siguiente jugador
         */
    }

    /**Método que realiza las acciones asociadas al comando 'ver tablero'*/
    private void verTablero() {
        System.out.println(tablero.toString());
    }

    //"Sobrecarga" del método asociado al comando 'listar'

    /**Método que realiza las acciones asociadas al comando 'listar jugadores'.*/
    private void listarJugadores() {
        System.out.println("{");
        for(Jugador j: jugadores){
            if(j!=banca){
                j.infoJugador();
                System.out.println("\n");
            }
        }
        System.out.println("}");

    }

    /**Método que realiza las acciones asociadas al comando 'listar avatares'.*/
    private void listarAvatares() {
        System.out.println("{");
        for (Avatar a : avatares) {
            if (a != null) {
                a.infoAvatar(); // Llama a la función infoAvatar para imprimir los detalles del avatar
            }
        }
        System.out.println("}");
    }

    /**Método que realiza las acciones asociadas al comando 'listar enventa'.*/
    private void listarVenta() {
        System.out.println("{");
        int i;
        for(i=0; i<40; i++){
            if((this.tablero.getCasilla(i).getDuenho()==banca) &&
                    (this.tablero.getCasilla(i).getTipo()=="Solar" || this.tablero.getCasilla(i).getTipo()=="Transporte"
                            || this.tablero.getCasilla(i).getTipo()=="Servicios" )){
                this.tablero.getCasilla(i).infoCasilla();
            }
        }
        System.out.println("}");
    }


    //Métodos de comandos que dependen de una instancia

    /**Método para avanzar casillas de manera manual.
     * Usado durante el desarrollo, en la versión final no se podrá usar
     * @param n Número de casillas que se debe avanzar
     */
    private void avanzar(int n) {
        System.out.println("{");
        Jugador jugador = obtenerTurno();
        Avatar avatar = jugador.getAvatar();

        Casilla salida = avatar.getLugar();


        jugador.sumarTiradaCarcel();

        avatar.moverAvatar(tablero.getPosiciones(),  n);

        Casilla destino = avatar.getLugar();

        System.out.println("El avatar " + avatar.getId() + " avanza " + (n) + " casillas desde " + salida.getNombre() + " hasta " + destino.getNombre());

        if (destino.getNombre().equals("IrCarcel")){
            Casilla carcel = tablero.encontrar_casilla("Carcel"); // Asumiendo que existe la función en Tablero

            jugador.getAvatar().setLugar(carcel);
            jugador.setEnCarcel(true);
        }
    }

    /**Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * @param nombre Cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) { //REVISAR
        System.out.println("{");
        Jugador jugador = obtenerTurno();
        Casilla casilla = obtenerTurno().getAvatar().getLugar();


        if(casilla.posiblecompra(jugador, banca)) {
            System.out.println(jugador.getNombre() + " compra la propiedad " + nombre + " por " + casilla.getValor() + ".");
            casilla.comprarCasilla(jugador, banca);
        } else {
            System.out.println("No  es posible comprar " + nombre);
    }
    }

    /**Método que realiza las acciones asociadas al comando 'crear jugador'.
     * @param nombre Nombre del jugador
     * @param avatar Tipo de avatar
     */
    private void crearJugador(String nombre, String avatar) {
       /* if(!Avatar.esTipoAvatar(nombre)){
            System.out.println("Tipo Incorrecto");
            return;
        }
        */


        // Definir la casilla de inicio. Por ejemplo, la primera casilla del tablero
        Casilla casillaInicio = tablero.getCasilla(0);  // Asumiendo que tienes un método para obtener la casilla inicial

        // Crear nuevo jugador tipo coche porque si
        Jugador nuevoJugador = new Jugador(nombre, "Coche", casillaInicio, avatares);

        // Añadir el jugador a la lista de jugadores
        this.jugadores.add(nuevoJugador);


        // Imprimir detalles del jugador recién creado

        jugadorTurno();

        casillaInicio.anhadirAvatar(nuevoJugador.getAvatar());


        verTablero();
    }

    //"Sobrecarga" del método asociado al comando 'describir'
    //DUDA: aquí hay que hacer un método describir que llame a cada uno de estos 3 en función del dato introducido?
    //(o en función de lo que va después de 'describir' vamos)
    //Y en caso de que sí, ¿por qué pasar un String de partes SOLO en descJugador?

    /** Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
     * @param nombre Nombre de la casilla a describir
     */
    private void descCasilla(String nombre) {
        System.out.println("{");
        switch (nombre) {
            case "Salida":
                // Imprimir el valor de vuelta con separador de miles
                System.out.printf("Pago por vuelta: %,.0f%n", Valor.DINERO_VUELTA);
                break;

            case "Carcel":
                // Imprimir el valor para salir de la cárcel con separador de miles
                System.out.printf("Pago salir: %,.0f%n", Valor.DINERO_SALIR_CARCEL);
                Casilla casilla_a_buscar = tablero.encontrar_casilla(nombre);
                break;

            case "Parking":
                // Añadir lógica para imprimir el bote si se añade más tarde
                break;

            case "IrCarcel": case "Caja": case "Suerte":
                // No es necesario imprimir nada aquí
                break;

            case "Serv1": case "Serv2": case "Trans1": case "Trans2": case "Trans3": case "Trans4":
                Casilla casillaEncontrada1 = tablero.encontrar_casilla(nombre);
                System.out.println("Tipo: " + casillaEncontrada1.getTipo());
                System.out.println("Duenho: " + casillaEncontrada1.getDuenho());
                // Imprimir el valor de la casilla y el valor de hipoteca con separador de miles
                System.out.printf("Precio: %,.0f%n", casillaEncontrada1.getValor());
                System.out.printf("Hipoteca: %,.0f%n", casillaEncontrada1.getHipoteca());
                break;

            case "Solar1": case "Solar2": case "Solar3": case "Solar4":
            case "Solar5": case "Solar6": case "Solar7": case "Solar8": case "Solar9": case "Solar10":
            case "Solar11": case "Solar12": case "Solar13": case "Solar14": case "Solar15":
            case "Solar16": case "Solar17": case "Solar18": case "Solar19": case "Solar20": case "Solar21": case "Solar22":
                Casilla casillaEncontrada3 = tablero.encontrar_casilla(nombre);
                casillaEncontrada3.infoCasilla();
                break;

            case "Imp1": case "Imp2":
                Casilla casillaEncontrada2 = tablero.encontrar_casilla(nombre);
                System.out.println("Tipo: " + casillaEncontrada2.getTipo());
                // Imprimir el impuesto con separador de miles
                System.out.printf("apagar: %,.0f%n", casillaEncontrada2.getImpuesto());
                break;

            default:
                System.out.println(nombre + " no es un nombre de casilla válido.\n");
        }
        System.out.println("}");
    }


    /**Método que realiza las acciones asociadas al comando 'describir jugador'.
     * @param partes comando introducido
     */
    private void descJugador(String[] partes) {
        boolean encontrado = false;
        System.out.println("{");
        // en partes esta el nombre comprueba que el nombre del jugador existe y saca su info si existe
        for (Jugador jugador : jugadores) {
            if (jugador.getNombre().equals(partes[2])) {
                encontrado = true;
                // Llamar a la función infoJugador del jugador encontrado
                jugador.infoJugador();
                break;
            }
        }
        if(!encontrado){
            System.out.println("No se ha encontrado el jugador buscado");
        }
        System.out.println("}");
    }

    /**Método que realiza las acciones asociadas al comando 'describir avatar'.
     * @param ID id del avatar a describir
     */
    private void descAvatar(String ID) {
        System.out.println("{");
        // lista llamada avatares se recorre
        for (Avatar avatar : avatares) { // Busca el avatar en la lista
            if (avatar.getId().equals(ID)) {
                avatar.infoAvatar(); // Salida si encuentra
                return;
            }
        }
        // Si no encuentra el avatar, muestra un mensaje de error
        System.out.println("Avatar con ID " + ID + " no encontrado.");
        System.out.println("}");
    }

    public Jugador obtenerTurno() {
        return jugadores.get(turno);
    }
}


