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
    private int turno; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.
    private float bote;
    private boolean partidaTerminada =false;

    //Hay que asignar un valor por defecto para cada atributo
    public Menu(){
        this.jugadores = new ArrayList<Jugador>();
        this.avatares = new ArrayList<Avatar>();
        this.turno = -1; //Aún no ha empezado la partida
        this.lanzamientos = 0;
        this.banca = new Jugador();
        this.tablero=new Tablero(this.banca);
        this.dado1= new Dado();
        this.dado2= new Dado();
        this.tirado=false;
        this.solvente=true;
    }


    // Método para inciar una partida: crea los jugadores y avatares.
    public void iniciarPartida() {
        //Creamos un escaneador para introducir comandos
        Scanner scan= new Scanner(System.in);

        //Antes de empezar la partida hay que crear los jugadores
        //System.out.println(Valor.TEXTO_BIENVENIDA);
        setTextoTablero(Valor.TEXTO_BIENVENIDA);
        verTablero();

        //Bucle para crear los jugadores (máximo 6)
        //Dentro del propio bucle se empieza la partida
        int i=0;
        while(!partidaTerminada) {

            String comando_entero = scan.nextLine();
            String[] comando = comando_entero.split(" ");

            //IMPORTANTE comprobar la longitud para no acceder a un índice que no existe
            if (comando.length==4 && "crearjugador".equals(comando[0] + comando[1])) {
                if(i<6) {
                    crearJugador(comando[2], comando[3]);
                    i++;
                }
                else {
                    System.out.println("¡No se pueden crear más de 6 jugadores! Empieza la partida con el comando " +
                            Valor.BOLD_STRING + "empezar partida" + Valor.RESET + ".");
                }
            }
            else if ("empezar partida".equals(comando_entero)) {

                if(i!=0 && i!=1) {
                    this.turno = 0; //El primer jugador creado tiene el turno
                    System.out.println("¡Que comienze la partida!\nEs el turno de " + obtenerTurno().getNombre() +
                            ". Puedes tirar los dados con el comando " + Valor.BOLD_STRING + "lanzar dados" +
                            Valor.RESET + ".");

                    //Empezamos la partida ahora que ya tenemos los jugadores
                    setTextoTablero(Valor.LISTA_COMANDOS);

                    //Este es el bucle de la partida básicamente
                    while (!partidaTerminada) {
                        System.out.println();
                        analizarComando(scan.nextLine());
                    }
                }
                else if(i==0) {
                    System.out.println("Amig@ habrá que crear algún jugador antes de empezar no crees?");
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

            //Acabar la partida
            case "terminar partida":
            case "acabar partida":
                partidaTerminada= true;
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

                //IMPORTANTE hacer las comprobaciones en función del número de palabras del comando
                //Si no podría darse el caso de querer acceder a un índice que no existe
                if(comando.length==1) {
                    System.out.println(comando_entero + " no es un comando válido.");
                }
                else if(comando.length==2) {

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
                    else {
                        System.out.println(comando_entero + " no es un comando válido.");
                    }
                }
                else {
                    System.out.println(comando_entero + " no es un comando válido.");
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
        System.out.println("\tnombre: " + jugador.getNombre() + ",");
        System.out.println("\tavatar: " + jugador.getAvatar().getId());
        System.out.println("}");
    }

    /**Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.*/
    private void lanzarDados() {

        //Obtenemos el avatar que tiene el turno
        Jugador jugador = obtenerTurno();
        Avatar avatar = jugador.getAvatar();

        // Si aún no se ha tirado en el turno o vienes de haber sacado dobles
        if (!this.tirado || (this.tirado && this.dado1==this.dado2)) {

            // Moito ollo que se levas 3 tiradas é que estás no calabozo
            if(this.lanzamientos<3) {
                //Lanzamos 2 dados e imprimimos sus resultados
                int resultado1 = this.dado1.tirarDado();
                int resultado2 = this.dado2.tirarDado();
                System.out.println("[" + resultado1 + "] [" + resultado2 + "]");
                //Se vuelve a asignar true en segundas/terceras tiradas a no ser que hagamos un caso a parte
                this.tirado = true;
                this.lanzamientos += 1;

                // Estás encarcelado
                if(jugador.isEnCarcel()) {
                    // Si sacas dobles sales!
                    if(resultado1==resultado2) {
                        System.out.println("DOBLES!!\nSales de la cárcel.");
                        jugador.setEnCarcel(false);
                    }
                    else {
                        // No sales a menos que sea la tercera vez que tiras, caso en el que estás obligado
                        // a pagar la fianza. Si no puedes te declaras en bancarrota
                        if(jugador.getTiradasCarcel()==3) {
                            System.out.println("No ha habido suerte con los dados, toca pagar la fianza...");
                            //Por cómo está implementado salirCarcel(), básicamente pa poder tirar cuando salgas
                            this.tirado=false;
                            this.lanzamientos=0;
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
                    if(resultado1==resultado2) {
                        System.out.println("DOBLES!!");
                        if(this.lanzamientos==3) {
                            jugador.encarcelar(tablero.getPosiciones());
                            System.out.println("Tres dobles son muchos, vas a la cárcel sin pasar por la salida.");
                            return;
                        }
                    }

                    //Variables útiles
                    int sumaDados = resultado1+resultado2;
                    Casilla origen = avatar.getLugar();

                    //POR FIN Movemos al avatar a la casilla y evaluamos qué hacer
                    avatar.moverAvatar(tablero.getPosiciones(), sumaDados);
                    avatar.getLugar().evaluarCasilla(jugador,this.banca,sumaDados);

                    Casilla destino = avatar.getLugar();

                    System.out.println("El avatar " + avatar.getId() + " avanza " + (sumaDados) +
                            " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre());

                    // Si pasamos por la salida hay que cobrar!
                    if (origen.getPosicion() > destino.getPosicion()) {
                        System.out.println("¡Al pasar por la salida ganaste " + Valor.SUMA_VUELTA + "€!");
                        jugador.sumarFortuna(Valor.SUMA_VUELTA);
                        jugador.sumarVuelta();
                        cuatroVueltas();
                    }

                }

            }
            else {
                System.out.println("Acabas de ir a la cárcel, no puedes volver a tirar hasta el siguiente turno.");
            }
        }
        // Si ya se ha tirado y no han salido dobles
        else {
            System.out.println("¡Ya has tirado! Si no tienes nada más que hacer usa el comando " +
                    Valor.BOLD_STRING + "acabar turno" + Valor.RESET);
        }

    }

    /**Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. */
    private void salirCarcel() {//saca al avatar de la carcel
        //Establecemos el jugador actual
        Jugador jugador = this.jugadores.get(this.turno);
        if (jugador.isEnCarcel()) {
            if (!this.tirado) {
                if(!jugador.estaEnBancarrota()) {
                    jugador.setEnCarcel(false);
                    jugador.setTiradasCarcel(0);
                    jugador.sumarFortuna(-Valor.SALIR_CARCEL);
                    System.out.printf("%s paga la fianza de %,.0f € y sale de la cárcel. Puedes tirar los dados.\n",
                            jugador.getNombre(), Valor.SALIR_CARCEL);
                }
                else {
                    System.out.println("¡No puedes pagar la fianza porque estás en bancarrota!");
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
            this.tirado = false; // Reiniciar el estado de "tirado"
            this.lanzamientos = 0; // Reiniciar los lanzamientos

            // Incrementar el turno y asegurar que no exceda el tamaño del array
            this.turno += 1;
            if (this.turno >= this.jugadores.size()) {
                this.turno = 0; // Reiniciar el turno si llegamos al final de la lista
            }

            // Imprimir el nombre del nuevo jugador actual
            System.out.println("El jugador actual es: " + this.jugadores.get(turno).getNombre());
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
            if ((casilla_aux.getTipo().equals("Solar") || casilla_aux.getTipo().equals("Transporte")
                    || casilla_aux.getTipo().equals("Servicio")) && casilla_aux.getDuenho() == banca) {
                System.out.println(casilla_aux.getNombre() + " - Precio: " + casilla_aux.getValor());
            }
        }
    }

    //Métodos de comandos que dependen de una instancia

    /**Método para avanzar casillas de manera manual.
     * IMPORTANTE: NO CUENTA COMO LANZAR LOS DADOS!!
     * Usado durante el desarrollo, en la versión final no se podrá usar
     * @param n Número de casillas que se debe avanzar
     */
    private void avanzar(int n) {
        // Establecemos el avatar que tiene el turno
        Jugador jugador = obtenerTurno();
        Avatar avatar = jugador.getAvatar();

        // Establecemos la casilla de salida (solo para imprimirla después)
        Casilla salida = avatar.getLugar();

        //Movemos al avatar
        avatar.moverAvatar(this.tablero.getPosiciones(), n);

        // Establecemos la casilla de desitno (solo para imprimirla después)
        Casilla destino = avatar.getLugar();

        // Imprimimos el mensaje de que movimos al avatar
        System.out.println("El avatar " + avatar.getId() + " avanza " + (n) +
                " casillas desde " + salida.getNombre() + " hasta " + destino.getNombre());

        // Caso especial: IrCarcel nos encarcela
        if (this.avatares.get(this.turno).getLugar().getNombre().equals("IrCarcel")) {
            this.jugadores.get(this.turno).encarcelar(this.tablero.getPosiciones());
        }
    }


    /**Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
     * @param nombre Cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) { //REVISAR
        Casilla c=tablero.encontrar_casilla(nombre);
        if(c != null) {
            if (this.tirado || lanzamientos > 0) {
                c.comprarCasilla(this.jugadores.get(turno), this.banca);//le paso el jugador que tiene el turno y eljugador 0 (la banca)
                Jugador jugador = obtenerTurno();
                jugador.setVueltas(0);
            }
        }
        else {
            System.out.println("No hay ninguna casilla que se llame " + nombre);
        }
    }

    /**Método que realiza las acciones asociadas al comando 'crear jugador'.
     * Solo se usa antes de empezar la partida, una vez empezada no se pueden crear más jugadores.
     * @param nombre Nombre del jugador
     * @param avatar Tipo de avatar
     */
    private void crearJugador(String nombre, String avatar) {

        String tipo = avatar.trim().toLowerCase();  // Eliminar espacios y convertir a minúsculas

        // Definir la casilla de inicio. Por ejemplo, la primera casilla del tablero
        Casilla casillaInicio = tablero.getCasilla(0);  // Asumiendo que tienes un método para obtener la casilla inicial
        if(!esTipoAvatar(tipo)){
            System.out.println("Tipo Incorrecto");
            return;
        }
        // Crear nuevo jugador tipo coche porque si
        Jugador nuevoJugador = new Jugador(nombre, tipo, casillaInicio, avatares);


        // Añadir el jugador a la lista de jugadores
        this.jugadores.add(nuevoJugador);

        // Hacemos que sea el turno del jugador recién creado
        // Nótese que por defecto this.turno = -1
        this.turno += 1;

        // Imprimir detalles del jugador recién creado
        jugadorTurno();
        casillaInicio.anhadirAvatar(nuevoJugador.getAvatar());


        verTablero();
    }

    /** Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
     * @param nombre Nombre de la casilla a describir
     */
    private void descCasilla(String nombre) {
        switch (nombre) {
            case "Salida":
                // Imprimir el valor de vuelta con separador de miles
                System.out.printf("{\n\tPago por vuelta: %,.0f", Valor.SUMA_VUELTA);

                // Encontrar la casilla de "Salida"
                Casilla casillaSalida = tablero.encontrar_casilla("Salida");

                ArrayList<Avatar> avataresEnSalida = casillaSalida.getAvatares();
                if (avataresEnSalida.isEmpty()) {
                    System.out.println("Nadie en Salida");
                } else {
                    // Recorremos la lista de avatares y mostramos todos los jugadores en la misma línea
                    for (int i = 0; i < avataresEnSalida.size(); i++) {
                        Avatar avatar = avataresEnSalida.get(i);
                        Jugador jugador = avatar.getJugador();

                        // Imprimimos el jugador en la casilla de salida
                        System.out.print("[" + jugador.getNombre() + "]  ");
                    }
                }
                System.out.println("\n}");
                break;

            case "Carcel":
                // Imprimir el valor para salir de la cárcel con separador de miles
                System.out.printf("{\n\tPago salir: %,.0f \n", Valor.SALIR_CARCEL);

                // Encontrar la casilla de la cárcel
                Casilla casillaCarcel = tablero.encontrar_casilla("Carcel");  // Asumo que la casilla se llama "Carcel"

                ArrayList<Avatar> avataresEnCarcel = casillaCarcel.getAvatares();
                if (avataresEnCarcel.isEmpty()) {
                    System.out.println("No hay Jugadores");
                } else {


                    // Recorremos la lista de avatares y mostramos todos los jugadores en la misma línea
                    for (int i = 0; i < avataresEnCarcel.size(); i++) {
                        Avatar avatar = avataresEnCarcel.get(i);
                        Jugador jugador = avatar.getJugador();

                        if (jugador.isEnCarcel()) {
                            // Imprimimos el jugador que está encarcelado con el número de tiradas en la cárcel
                            System.out.print("[" + jugador.getNombre() + "," + jugador.getTiradasCarcel() + "]");
                        } else {
                            // Imprimimos el jugador que está de visita
                            System.out.print("[" + jugador.getNombre() + " (visita)]");
                        }

                        // Agregar una coma y un espacio después de cada jugador, excepto el último
                        if (i < avataresEnCarcel.size() - 1) {
                            System.out.print(" ");
                        }
                    }
                }

                System.out.println("\n}");
                break;


            case "Parking":
                // Imprimir el bote con separador de miles
                System.out.printf("{\n\tBote acumulado: %,.0f", bote);

                // Encontrar la casilla de "Parking"
                Casilla casillaParking = tablero.encontrar_casilla("Parking");

                ArrayList<Avatar> avataresEnParking = casillaParking.getAvatares();
                if (avataresEnParking.isEmpty()) {
                    System.out.println(" Nadie en Parking");
                } else {
                    System.out.print("\n\tAvatares en Parking: ");

                    // Recorremos la lista de avatares y mostramos todos los jugadores en la misma línea
                    for (int i = 0; i < avataresEnParking.size(); i++) {
                        Avatar avatar = avataresEnParking.get(i);
                        Jugador jugador = avatar.getJugador();

                        // Imprimimos el jugador en la casilla de parking
                        System.out.print("[" + jugador.getNombre() + "]  ");
                    }
                }
                System.out.println("\n}");
                break;

            case "IrCarcel": case "Caja": case "Suerte":
                System.out.println("El comando no esta pensado para esta casilla");
                System.out.println("}");
                // No es necesario imprimir nada aquí
                break;

            case "Serv1": case "Serv2": case "Trans1": case "Trans2": case "Trans3": case "Trans4":
                System.out.println("{");
                Casilla casillaEncontrada1 = tablero.encontrar_casilla(nombre);
                System.out.println("\tTipo: " + casillaEncontrada1.getTipo());
                System.out.println("\tDuenho: " + casillaEncontrada1.getDuenho().getNombre());
                // Imprimir el valor de la casilla y el valor de hipoteca con separador de miles
                System.out.printf("\tPrecio: %,.0f\n", casillaEncontrada1.getValor());
                System.out.printf("\tHipoteca: %,.0f\n", casillaEncontrada1.getValor()/2f);
                System.out.println("}");
                break;

            case "Solar1": case "Solar2": case "Solar3": case "Solar4":
            case "Solar5": case "Solar6": case "Solar7": case "Solar8": case "Solar9": case "Solar10":
            case "Solar11": case "Solar12": case "Solar13": case "Solar14": case "Solar15":
            case "Solar16": case "Solar17": case "Solar18": case "Solar19": case "Solar20": case "Solar21": case "Solar22":
                Casilla casillaEncontrada3 = tablero.encontrar_casilla(nombre);
                System.out.println(casillaEncontrada3.infoCasilla());
                break;

            case "Imp1": case "Imp2":
                System.out.println("{");
                Casilla casillaEncontrada2 = tablero.encontrar_casilla(nombre);
                System.out.println("\tTipo: " + casillaEncontrada2.getTipo());
                // Imprimir el impuesto con separador de miles
                System.out.printf("\tapagar: %,.0f\n", casillaEncontrada2.getImpuesto());
                System.out.println("}");
                break;

            default:
                System.out.println(nombre + " no es un nombre de casilla válido.");
        }
    }


    /**Método que realiza las acciones asociadas al comando 'describir jugador'.
     * @param partes comando introducido
     */
    private void descJugador(String[] partes) {
        boolean encontrado = false;

        // en partes esta el nombre comprueba que el nombre del jugador existe y saca su info si existe
        // miguel meu que tiene que poder sacar la info de varios jugadores
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
        return jugadores.get(turno);
    }

    //Petadinha (longitud máxima de líneas de nuevo_texto=17)
    //Se puede hacer desde aquí porque no existe encapsulación al ser un String[]
    public void setTextoTablero(String nuevo_texto) {
        //Dividimos el String en partes en función de los saltos de línea
        String[] nuevo_texto_tablero = nuevo_texto.split("\n");

        //Se empieza en el índice 1 porque la primera línea del centro del tablero se deja vacía
        //Nótese que para asignar bien el texto el índice de texto_tablero es i-1
        for(int i = 1; i< nuevo_texto_tablero.length+1; i++) {
            Valor.TEXTO_TABLERO[i] = nuevo_texto_tablero[i-1];
        }
    }
    private void cuatroVueltas() {
        boolean todosCumplen = true;
        for (Jugador j : jugadores) {
            if (j.getVueltas() < 4) { // Si algún jugador no cumple la condición
                todosCumplen = false; // Cambiamos la variable a false
                break; // detenenemos el bucle ya que no es necesario seguir
            }
        }
        if (todosCumplen) {
            this.tablero.aumentarCoste(banca);
            System.out.println(("Todos los jugadores han dado 4 vueltas! El precio de las propiedades aumenta."));;
        }
    }


    private boolean esTipoAvatar(String tipo) {
        return "coche".equals(tipo) ||
                "esfinge".equals(tipo) ||
                "sombrero".equals(tipo) ||
                "pelota".equals(tipo);
    }
}




