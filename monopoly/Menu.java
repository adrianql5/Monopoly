package monopoly;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections; // Lo usamos para barajar las cartas

import partida.Avatar;
import partida.Dado;
import partida.Jugador;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.

    private float bote; //El bote del Parking se guarda en este atributo
    private ArrayList<Carta> cartas_suerte;
    private ArrayList<Carta> cartas_caja;
    private Carta carta_del_reves;
    private boolean partidaTerminada; //Booleano para acabar la partida

    //SECCIÓN DE CONSTRUIR EL MENÚ
    //Hay que asignar un valor por defecto para cada atributo
    public Menu(){
        this.jugadores = new ArrayList<Jugador>();
        this.avatares = new ArrayList<Avatar>();
        this.turno = -1; //Aún no ha empezado la partida, cuando se crear el primer jugador ya se pone a 0
        this.lanzamientos = 0;
        this.banca = new Jugador();
        this.tablero = new Tablero(this.banca);
        this.dado1 = new Dado();
        this.dado2 = new Dado();
        this.tirado = false;
        this.solvente = true;
        this.bote = 0;
        anhadirBarajas();
        this.partidaTerminada = false;

    }

    /**Método para generar las cartas de tipo Suerte y de tipo Caja de comunidad (más la carta dada la vuelta).*/
    private void anhadirBarajas() {
        this.cartas_suerte = new ArrayList<Carta>();
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_1));
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_2));
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_3));
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_4));
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_5));
        cartas_suerte.add(new Carta(Texto.CARTA_SUERTE_6));
        this.cartas_caja = new ArrayList<Carta>();
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_1));
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_2));
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_3));
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_4));
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_5));
        cartas_caja.add(new Carta(Texto.CARTA_CAJA_6));
        this.carta_del_reves = new Carta();
    }


    public void hipotecar(String nombre){
        Casilla casilla= tablero.encontrar_casilla(nombre);
        Jugador jugador= obtenerTurno();
        if(casilla.getDuenho().equals(jugador)){
            casilla.hipotecar();
            if(!casilla.estaHipotecada()){
                System.out.println("El jugador "+ jugador.getNombre()+" recibe "+ casilla.getHipoteca()+
                " por la hipoteca de " + casilla.getNombre()+
                ". No puede recibir alquileres ni edificar en el grupo "+ casilla.getGrupo().getColorGrupo());
                jugador.sumarFortuna(casilla.getHipoteca());
                banca.sumarFortuna(-casilla.getHipoteca());
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
                casilla.desHipotecar();
                if(casilla.estaHipotecada()){
                    System.out.println("El jugador "+ jugador.getNombre()+" recibe "+ casilla.getHipoteca()+
                    " por la hipoteca de " + casilla.getNombre()+
                    ". No puede recibir alquileres ni edificar en el grupo "+ casilla.getGrupo().getColorGrupo());
                    jugador.sumarFortuna(-(casilla.getHipoteca()+casilla.getHipoteca()*0.10f));
                    banca.sumarFortuna((casilla.getHipoteca()+casilla.getHipoteca()*0.10f));
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
        Jugador jugador = obtenerTurno(); // Obtener el jugador cuyo turno es actualmente
        Casilla casilla = jugador.getAvatar().getLugar(); // Obtener la casilla en la que se encuentra el jugador

        if (casilla.esEdificable(tipo, jugador)) {
            Edificio edificio = new Edificio(tipo, casilla);

            if (jugador.getFortuna() >= edificio.getCoste()) {
                casilla.anhadirEdificio(edificio); // Añadir el edificio a la casilla
                jugador.sumarGastos(edificio.getCoste()); // Restar el coste del edificio
                jugador.sumarFortuna(-edificio.getCoste()); // Reducir la fortuna del jugador
                this.banca.sumarFortuna(edificio.getCoste()); // Aumentar la fortuna de la banca

                System.out.println("El jugador " + jugador.getNombre() + " ha comprado el edificio " + edificio.getId() +" por "+edificio.getCoste());

                // Si es un hotel, eliminar todas las casas de la casilla
                if (tipo.equals("hotel")) {
                    casilla.eliminarCasasDeCasilla();
                }
            } else {
                System.out.println("No tienes suficiente dinero.");
            }
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
        Casilla casilla = jugador.getAvatar().getLugar(); // Obtener la casilla donde se encuentra el jugador

        // Verificar si la casilla es del tipo correcto (solar)
        if (!casilla.getNombre().equals(solar)) {
            System.out.println("No se pueden vender " + tipo + " en " + solar + ". Esta propiedad no pertenece a " + jugador.getNombre() + ".");
            return; // Salir si la propiedad no pertenece al jugador
        }

        if(casilla.getDuenho().equals(jugador)){
            ArrayList<Edificio> eds = casilla.getEdificiosPorTipo(tipo); // Obtener edificios del tipo especificado
            if (eds.size() >= n) { // Comprobar si hay suficientes edificios para vender
                float suma = 0;
                for(int i=n-1; i>=0; i--){ //odio java
                    eds.remove(i);
                    suma+=eds.get(i).getCoste();
                }   
                this.banca.sumarFortuna(-suma); // Restar de la fortuna de la banca
                jugador.sumarFortuna(suma); // Sumar a la fortuna del jugador

                // Mensaje de éxito
                System.out.println("El jugador " + jugador.getNombre() + " ha vendido " + n + " " + tipo + " en " + solar + ", recibiendo " + suma + "€. En la propiedad queda " + (eds.size() - n) + " " + tipo + ".");
            } else {
                // Si no hay suficientes edificios para vender, mostrar mensaje
                if (eds.size() > 0) {
                    System.out.println("Solamente se puede vender " + eds.size() + " " + tipo + ", recibiendo " + (eds.size() > 0 ? (eds.get(0).getCoste() / 2) * eds.size() : 0) + "€.");
                } else {
                    System.out.println("No hay " + tipo + " para vender en " + solar + ".");
                }
            }
        }
        else{
            System.out.println("No puedes vender las propiedades porque no te pertencen.");
        }
    }


    //SECCIÓN DE CONTROL DEL FLUJO DE LA PARTIDA

    // Método para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida() {
        //Creamos un escaneador para introducir comandos
        Scanner scan= new Scanner(System.in);

        //Antes de empezar la partida hay que crear los jugadores
        setTextoTablero(Texto.BIENVENIDA);
        verTablero();

        //Bucle para crear los jugadores (máximo 6)
        //Dentro del propio bucle se empieza la partida
        while(!partidaTerminada) {

            String comando_entero = scan.nextLine();
            String[] comando = comando_entero.split(" ");

            //IMPORTANTE comprobar la longitud para no acceder a un índice que no existe
            if (comando.length==4 && "crearjugador".equals(comando[0] + comando[1])) {
                if(this.avatares.size()<7) {
                    crearJugador(comando[2], comando[3]);
                }
                else {
                    System.out.println("¡No se pueden crear más de 6 jugadores! Empieza la partida con el comando " +
                            Valor.BOLD_STRING + "empezar partida" + Valor.RESET + ".");
                }
            }
            else if ("empezar partida".equals(comando_entero)) {

                //Si hay al menos 2 jugadores empezamos
                if(this.avatares.isEmpty()) {
                    System.out.println("Amig@ habrá que crear algún jugador antes de empezar no crees?");
                }
                else if(this.avatares.size()!=1) {
                    this.turno = 0; //El primer jugador creado tiene el turno
                    System.out.println("¡Que comienze la partida!\nEs el turno de " + obtenerTurno().getNombre() +
                            ". Puedes tirar los dados con el comando " + Valor.BOLD_STRING + "lanzar dados" +
                            Valor.RESET + ".");

                    //Empezamos la partida ahora que ya tenemos los jugadores
                    setTextoTablero(Texto.LISTA_COMANDOS);

                    //Este es el bucle de la partida básicamente
                    while (!partidaTerminada) {
                        System.out.println();
                        analizarComando(scan.nextLine());
                    }
                }
                else {
                    System.out.println("Creo que jugar una persona sola no tiene mucho sentido...");
                }

            } else {
                System.out.println("Usa " + Valor.BOLD_STRING + "crear jugador <tuNombre> <tipoJugador>" +
                        Valor.RESET + " o introduce " + Valor.BOLD_STRING + "empezar partida" + Valor.RESET +
                        " si ya no quieres crear más.");
            }

        }
        //Acabouse, liberar memoria estaría duro
        scan.close();
        terminarPartida();
    }

    /**Método para acabar la partida cuando se termina de jugar o cuando alguien pierde*/
    private void terminarPartida(){
        System.out.println("La partida ha finalizado, esperamos que disfrutáseis la experiencia.");
        System.exit(0);
    }



    //SECCIÓN DE COMANDOS DEL MENÚ

    /**Método que interpreta el comando introducido y toma la accion correspondiente.
     * @param comando_entero Línea de comando que introduce el jugador
     */
    private void analizarComando(String comando_entero) {

        switch(comando_entero){
            //Primer bloque de comandos: no dependen de una instancia

            //Acabar la partida
            case "terminar partida":
            case "acabar partida":
                acabarPartida();
                break;

            //Indicar jugador que tiene el turno
            case "jugador":
                infoJugadorTurno();
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

            //CHEAT PARA DINERO INFINITO (+mil millones)
            case "dinero infinito":
                dineroInfinito();
                break;

            //PROBANDO LA IMPRESIÓN DE CARTAS
            case "probar cartas":
                probarCartas();
                break;
            case "coger carta caja":
                cogerCarta(this.cartas_caja);
                break;
            case "coger carta suerte":
                cogerCarta(this.cartas_suerte);
                break;
            

            case "listar edificios":
                listarEdificios(null);//si no especifica grupo
                break;

            //Segundo bloque de comandos: dependen de una instancia
            default:
                //Dividimos el comando en partes
                String[] comando=comando_entero.split(" ");

                //IMPORTANTE hacer las comprobaciones en función del número de palabras del comando
                //Si no podría darse el caso de querer acceder a un índice que no existe
                if(comando.length==1) {
                    System.out.println(comando_entero + " no es un comando válido.");
                }
                else if(comando.length==2) {

                    //Podría ser uno de los siguientes:
                    switch(comando[0]){
                        //probando funciones de comprar
                        
                        case "edificar":
                            edificar(comando[1]);
                            break;

                        case "hipotecar":
                            hipotecar(comando[1]);
                            break;

                        case "deshipotecar":
                            deshipotecar(comando[1]);
                    
                        //Para comprar una casilla
                        case "comprar":
                            comprar(comando[1]);
                            break;

                        //Para describir una casilla
                        case "describir":
                            descCasilla(comando[1]);
                            break;

                        default:
                            System.out.println(comando_entero + " no es un comando válido.");
                            break;
                    }
                }
                //Mucho ojo que puede usarse con varios nombres a la vez (comando.length() varía hasta 8 máximo)
                //Al llegar aquí ya se sabe que comando.length es como mínimo 3
                else if("describirjugador".equals(comando[0]+comando[1]) && comando.length<9) {
                    descJugador(comando); //El método pide que se pasen las partes del comando ojo!
                }
                else if(comando.length==3) {

                    //De momento solo los hay de tipo "describir"
                    if("describiravatar".equals(comando[0]+comando[1])) {
                        descAvatar(comando[2]);
                    }

                    //prueba para listar
                    else if("listaredificios".equals(comando[0]+comando[1])){//pistas de deportes hay q hacer gitanada
                        listarEdificios(comando[2]);
                    }



                    //CHEAT PARA SACAR LO QUE QUIERAS CON LOS DADOS
                    else if("dados".equals(comando[0])) {
                        // SE DA POR HECHO QUE SE INTRODUCE UN VALOR CONVERTIBLE A ENTERO!! HAY QUE CAMBIARLO
                        int dado1 = Integer.parseInt(comando[1]);
                        int dado2 = Integer.parseInt(comando[2]);
                        dadosTrampa(dado1,dado2);
                        verTablero();
                    }
                    else {
                        System.out.println(comando_entero + " no es un comando válido.");
                    }
                }

                else if(comando.length==4){
                    if(comando[0].equals("vender")){
                        venderEdificios(comando[1], comando[2], Integer.parseInt(comando[3]));
                    }
                }


                else {
                    System.out.println(comando_entero + " no es un comando válido.");
                }

        }
    }


    //SECCIÓN DE COMANDOS QUE NO DEPENDEN DE NINGUNA INSTANCIA

    /**Método que ejecuta todas las acciones relacionadas con el comando 'jugador'.
     * Imprime la información del jugador que tiene el turno.
     */
    private void infoJugadorTurno() {
        Jugador jugador = obtenerTurno(); // Obtener el jugador actual

        // Imprimir el nombre y el avatar en el formato requerido
        System.out.println("{");
        System.out.println("\tnombre: " + jugador.getNombre() + ",");
        System.out.println("\tavatar: " + jugador.getAvatar().getId());
        System.out.println("}");
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
            System.out.println("Todos los jugadores han dado 4 vueltas sin comprar! El precio de las propiedades aumenta.");
            reiniciarVueltasSinCompras();
        }
    }

    /**Método que reinicia las vueltas sin comprar a 0.*/
    private void reiniciarVueltasSinCompras() {
        for (Jugador j : jugadores) {
            j.setVueltas_sin_comprar(0);
        }
    }

    /**Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
     * Hace las comprobaciones pertinentes: primero si realmente se pueden tirar los dados,
     * luego si el jugador está en la cárcel o no, etc.
     */
    private void lanzarDados() {

        //Obtenemos el avatar que tiene el turno
        Jugador jugador = obtenerTurno();
        Avatar avatar = jugador.getAvatar();

        // Comprobamos si acaba de ser encarcelado
        if (this.tirado && jugador.isEnCarcel() && jugador.getTiradasCarcel()==0) {
            System.out.println("Acabas de ir a la cárcel, no puedes volver a tirar hasta el siguiente turno.\n" +
                    "Si no tienes nada más que hacer usa el comando " +
                    Valor.BOLD_STRING + "acabar turno" + Valor.RESET);

        }
        else {
            // Comprobamos si aún no se ha tirado en el turno o vienes de haber sacado dobles
            // IMPORTANTE EL ORDEN DEL IF: el valor de los dados antes de la primera tirada de la partida es null
            if(!this.tirado || this.dado1.getValor()==this.dado2.getValor()) {
                // Lanzamos 2 dados e imprimimos sus resultados
                int resultado1 = this.dado1.tirarDado();
                int resultado2 = this.dado2.tirarDado();
                System.out.println("[" + resultado1 + "] [" + resultado2 + "]");
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
                    // Establecemos la casilla de salida (solo para imprimirla después)


                    //Variables útiles
                    int sumaDados = resultado1+resultado2;
                    Casilla origen = avatar.getLugar();

                    //POR FIN Movemos al avatar a la casilla
                    avatar.moverAvatar(tablero.getPosiciones(), sumaDados);

                    Casilla destino = avatar.getLugar();

                    if(resultado1==resultado2) {
                        System.out.println("DOBLES!!");
                        if(this.lanzamientos==3) {
                            jugador.encarcelar(this.tablero.getPosiciones());
                            System.out.println("Tres dobles son muchos, vas a la cárcel sin pasar por la salida.");
                            return;
                        }
                        // Vuelves a tirar a no ser que caigas en IrCarcel
                        else if(!destino.getNombre().equals("IrCarcel")) {
                            System.out.println("Vuelves a tirar.");
                        }
                        // No podemos encarcelar al jugador desde evaluarCasilla
                        else {
                            jugador.encarcelar(this.tablero.getPosiciones());
                        }
                    }

                    System.out.println("El avatar " + avatar.getId() + " avanza " + (sumaDados) +
                            " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");

                    // Si pasamos por la salida hay que cobrar!
                    if (origen.getPosicion() > destino.getPosicion()) {
                        System.out.printf("¡Al pasar por la salida ganaste %,.0f€!\n", Valor.SUMA_VUELTA);
                        jugador.sumarFortuna(Valor.SUMA_VUELTA);
                        jugador.sumarVuelta();
                        jugador.sumarVueltas_sin_comprar();
                        cuatroVueltasSinCompras();

                    }

                    //EVALUAMOS QUÉ HAY QUE HACER EN FUNCIÓN DE LA CASILLA
                    avatar.getLugar().evaluarCasilla(jugador,this.banca,sumaDados);

                    // No podemos encarcelar al jugador desde evaluarCasilla
                    if(destino.getNombre().equals("IrCarcel")) {
                        jugador.encarcelar(this.tablero.getPosiciones());
                    }

                }

            }
            else {
                System.out.println("¡Ya has tirado! Si no tienes nada más que hacer usa el comando " +
                        Valor.BOLD_STRING + "acabar turno" + Valor.RESET);
            }
        }
        if(jugador.estaEnBancarrota()){
            System.out.println("El jugador " + jugador.getNombre() + " esta en bancarrota \n");
            acabarPartida();
        }

    }

    /**Método que resetea los atributos relacionados con la cárcel del jugador que se desencarcela.
     * También resetea el boolean de la tirada y el número de lanzamientos del turno.
     * Nótese que por ese motivo al salir de la cárcel puedes tirar los dados como un turno normal.
     * Lo usa salirCarcel pero tb se llama cuando sales por sacar dobles.
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
                if(jugador.getFortuna() < Valor.SALIR_CARCEL) {
                    desencarcelar(jugador);
                    jugador.sumarFortuna(-Valor.SALIR_CARCEL);
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
        // Comprobar si el jugador actual ya lanzó los dados en su turno
        if (this.tirado) {
            // Si los lanzó y sacó dobles está obligado a volver a tirar!
            // A no ser que lo acaben de encarcelar por sacar 3 dobles seguidos
            if (this.dado1.getValor()==this.dado2.getValor() && this.lanzamientos<3) {
                System.out.println("Sacaste dobles, tienes que volver a tirar.");
            } else {
                this.tirado = false; // Reiniciar el estado de "tirado"
                this.lanzamientos = 0; // Reiniciar los lanzamientos

                // Incrementar el turno y asegurar que no exceda el tamaño del array
                this.turno += 1;
                if (this.turno >= this.jugadores.size()) {
                    this.turno = 0; // Reiniciar el turno si llegamos al final de la lista
                }
                // Imprimir el nombre del nuevo jugador actual
                System.out.println("El jugador actual es: " + this.jugadores.get(turno).getNombre());
            }
        } else {
            // Si no ha tirado los dados aún, informar al jugador
            System.out.println("Aún no has lanzado los dados este turno!");
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


    //SECCIÓN DE COMANDOS QUE DEPENDEN DE UNA INSTANCIA

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
                System.out.printf("{\n\tBote acumulado: %,.0f€\n", banca.getFortuna());

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

        // Obtenemos la lista de avatares que hay en la casilla cárcel
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


    /**Método que realiza las acciones asociadas al comando 'describir jugador'.
     * @param partes comando introducido
     */
    private void descJugador(String[] partes) {
        boolean encontrado = false;

        // en partes están los nombres de los jugadores, comprueba que el nombre existe y saca su info si existe
        for (Jugador jugador : jugadores) {
            for(int i=2; i<partes.length; i++) {
                if (jugador.getNombre().equals(partes[i])) {
                    encontrado = true;
                    // Llamar a la función infoJugador del jugador encontrado
                    jugador.infoJugador();
                    break;
                }
            }
        }
        if(!encontrado){
            System.out.println("No se ha encontrado el jugador buscado.");
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

    public Jugador obtenerTurno() {
        return this.jugadores.get(this.turno);
    }

    //SECCIÓN DE COMANDOS QUE DEPENDEN DE DOS INSTANCIAS

    /**Método que realiza las acciones asociadas al comando 'crear jugador'.
     * Solo se usa antes de empezar la partida, una vez empezada no se pueden crear más jugadores.
     * @param nombre Nombre del jugador
     * @param avatar Tipo de avatar
     */
    private void crearJugador(String nombre, String avatar) {

        String tipo = avatar.trim().toLowerCase();  // Eliminar espacios y convertir a minúsculas

        // Definir la casilla de inicio.
        Casilla casillaInicio = tablero.getCasilla(0);

        //Comprobamos que el tipo introducido es válido
        if(!esTipoAvatar(tipo)){
            System.out.println("Tipo de avatar incorrecto");
            return;
        }

        // Creamos el nuevo jugador con el tipo indicado
        Jugador nuevoJugador = new Jugador(nombre, tipo, casillaInicio, avatares);

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


    //SECCIÓN DE MÉTODOS ÚTILES DE MENÚ

    //Petadinha (longitud máxima de líneas de nuevo_texto=17)
    //Se puede hacer desde aquí porque no existe encapsulación de sus elementos al ser un String[]
    /**Método para cambiar el texto que se muestra en medio del tablero*/
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

    // SECCIÓN DE MÉTODOS RELACIONADOS CON LAS CARTAS TIPO SUERTE Y CAJA DE COMUNIDAD

    /**Método para cuando se cae en una casilla de tipo Suerte o Caja de comunidad
     *
     *
     */
    public void cogerCarta(ArrayList<Carta> baraja) {
        System.out.println("Barajando las cartas...");
        Collections.shuffle(baraja); //Barajamos las cartas
        cartasAlReves(); //Mostramos el reverso de las cartas
        System.out.println("Escoge una carta con un número del 1 al 6.");
        //Creamos un escaneador para introducir el número
        Scanner scan= new Scanner(System.in);
        String num_carta = scan.nextLine();
        // SE DA POR HECHO QUE SE INTRODUCE UN VALOR CONVERTIBLE A ENTERO!! HAY QUE CAMBIARLO
        int n = Integer.parseInt(num_carta);
        mostrarCartaEscogida(baraja, n); //Volvemos a mostrar las cartas con la escogida dada la vuelta
        //evaluarCarta(); work in progress.....
    }

    /**Método para imprimir 6 cartas al revés en fila*/
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



    //SECCIÓN DE CHEATS DE MENÚ

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

    /**Método igual a 'lanzar dados' que saca lo que le indicas (sólo valores posibles de los dados!!).
     * Copypaste criminal
     */
    private void dadosTrampa(int d1, int d2) {

        //Obtenemos el avatar que tiene el turno
        Jugador jugador = obtenerTurno();
        Avatar avatar = jugador.getAvatar();

        // Comprobamos si acaba de ser encarcelado
        if (this.tirado && jugador.isEnCarcel() && jugador.getTiradasCarcel()==0) {
            System.out.println("Acabas de ir a la cárcel, no puedes volver a tirar hasta el siguiente turno.\n" +
                    "Si no tienes nada más que hacer usa el comando " +
                    Valor.BOLD_STRING + "acabar turno" + Valor.RESET);

        }
        else {
            // Comprobamos si aún no se ha tirado en el turno o vienes de haber sacado dobles
            // MUY IMPORTANTE EL ORDEN DEL IF: el valor de los dados antes de la primera tirada de la partida es null
            if(!this.tirado || this.dado1.getValor()==this.dado2.getValor()) {
                // ESTABLECEMOS EL VALOR DE LOS 2 DADOS COMO LOS ENTEROS QUE PASAMOS
                this.dado1.setValor(d1);
                this.dado2.setValor(d2);
                int resultado1 = this.dado1.getValor();
                int resultado2 = this.dado2.getValor();
                System.out.println("[" + resultado1 + "] [" + resultado2 + "]");
                //Se vuelve a asignar true en segundas/terceras tiradas a no ser que hagamos un caso a parte
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
                    // Establecemos la casilla de salida (solo para imprimirla después)


                    //Variables útiles
                    int sumaDados = resultado1+resultado2;
                    Casilla origen = avatar.getLugar();

                    //POR FIN Movemos al avatar a la casilla
                    avatar.moverAvatar(tablero.getPosiciones(), sumaDados);

                    Casilla destino = avatar.getLugar();

                    if(resultado1==resultado2) {
                        System.out.println("DOBLES!!");
                        if(this.lanzamientos==3) {
                            jugador.encarcelar(this.tablero.getPosiciones());
                            System.out.println("Tres dobles son muchos, vas a la cárcel sin pasar por la salida.");
                            return;
                        }
                        // Vuelves a tirar a no ser que caigas en IrCarcel
                        else if(!destino.getNombre().equals("IrCarcel")) {
                            System.out.println("Vuelves a tirar.");
                        }
                        // No podemos encarcelar al jugador desde evaluarCasilla
                        else {
                            jugador.encarcelar(this.tablero.getPosiciones());
                        }
                    }

                    System.out.println("El avatar " + avatar.getId() + " avanza " + (sumaDados) +
                            " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");

                    // Si pasamos por la salida hay que cobrar!
                    if (origen.getPosicion() > destino.getPosicion()) {
                        System.out.printf("¡Al pasar por la salida ganaste %,.0f€!\n", Valor.SUMA_VUELTA);
                        jugador.sumarFortuna(Valor.SUMA_VUELTA);
                        jugador.sumarVuelta();
                        jugador.sumarVueltas_sin_comprar();
                        cuatroVueltasSinCompras();

                    }

                    //EVALUAMOS QUÉ HAY QUE HACER EN FUNCIÓN DE LA CASILLA
                    avatar.getLugar().evaluarCasilla(jugador,this.banca,sumaDados);

                    // No podemos encarcelar al jugador desde evaluarCasilla
                    if(destino.getNombre().equals("IrCarcel")) {
                        jugador.encarcelar(this.tablero.getPosiciones());
                    }


                }

            }
            else {
                System.out.println("¡Ya has tirado! Si no tienes nada más que hacer usa el comando " +
                        Valor.BOLD_STRING + "acabar turno" + Valor.RESET);
            }
        }
        if(jugador.estaEnBancarrota()){
            System.out.println("El jugador " + jugador.getNombre() + " esta en bancarrota \n");
            acabarPartida();
        }

    }

    private boolean esTipoAvatar(String tipo) {
        return "coche".equals(tipo) ||
                "esfinge".equals(tipo) ||
                "sombrero".equals(tipo) ||
                "pelota".equals(tipo);
    }

    private void acabarPartida() {
        partidaTerminada = true;
    }
}





