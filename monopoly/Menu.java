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

    //SECCIÓN DE CONSTRUIR EL MENÚ--------------------------------------------------------------------------------------
    //Hay que asignar un valor por defecto para cada atributo
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

    //SECCIÓN DE MÉTODOS ÚTILES DEL MENÚ--------------------------------------------------------------------------------

    /**Método que devuelve el jugador que tiene el turno.*/
    public Jugador obtenerTurno() {
        return this.jugadores.get(this.turno);
    }

    /**Método que elimina al jugador correspondiente de la lista de jugadores.
     * Si se le pasa un jugador que no está en la lista de jugadores no hace nada.
     */
    public void eliminarJugador(Jugador jugador){
        for(Jugador j: this.jugadores){
            if(j.equals(jugador)){
                this.jugadores.remove(j);
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


    //SECCIÓN DE MÉTODOS QUE HIZO ADRI LA ÚLTIMA VEZ Y NO ESTÁN BIEN ORDENADOS------------------------------------------
    public void declararBancarrota(){
        Jugador jugador= obtenerTurno();
        if(jugador.estaEnBancarrota()){
            Jugador cobrador=jugador.getDeudaConJugador();

            if(cobrador.equals(banca)){
                System.out.println("El jugador"+ jugador.getNombre()+
                " se declara en bancarrota. Sus propiedades pasan a estar de nuevo en venta al precio al que estaban.");
                ArrayList<Casilla> propiedades =jugador.getPropiedades();

                for( Casilla c : propiedades){
                    jugador.eliminarPropiedad(c);
                }
                eliminarJugador(jugador);
            }
            else{
                System.out.println("El jugador "+jugador.getNombre()+
                " se declara en bancarrota. Sus propiedades pasan a ser de "+cobrador.getNombre());
                ArrayList<Casilla> propiedades= jugador.getPropiedades();
                for (Casilla c: propiedades){
                    cobrador.anhadirPropiedad(c);
                    jugador.eliminarPropiedad(c);

                }
                eliminarJugador(jugador);
            }
        }
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
                if(casilla.esDesHipotecable()){
                    System.out.println("El jugador "+ jugador.getNombre()+" paga "+ (casilla.getHipoteca()+casilla.getHipoteca()*0.10f)+
                    " por la hipoteca de " + casilla.getNombre()+
                    ". Ahora puede recibir alquileres y edificar en el grupo "+ casilla.getGrupo().getColorGrupo());
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

        //modificar if para que sea dueño del grupo
        //En un solar se puede construir una casa si dicho solar pertenece al jugador cuyo avatar se encuentra
        //en la casilla y si (1) el avatar ha caído más de dos veces en esa misma casilla o (2) el jugador posee el
        //grupo de casillas a la que pertenece dicha casilla.


        if(casilla.getDuenho().equals(jugador)){
            if (casilla.esEdificable(tipo, jugador)) {
                Edificio edificio = new Edificio(tipo, casilla);

                if (jugador.getFortuna() >= edificio.getCoste()) {
                    casilla.anhadirEdificio(edificio); // Añadir el edificio a la casilla
                    jugador.sumarGastos(edificio.getCoste()); // Restar el coste del edificio
                    jugador.sumarFortuna(-edificio.getCoste()); // Reducir la fortuna del jugador
                    this.banca.sumarFortuna(edificio.getCoste()); // Aumentar la fortuna de la banca

                    System.out.println("El jugador " + jugador.getNombre() + " ha comprado el edificio " +
                            edificio.getId() + " por " +edificio.getCoste());

                    // Si es un hotel, eliminar todas las casas de la casilla
                    if (tipo.equals("hotel")) {
                        casilla.eliminarCasasDeCasilla();
                    }
                } else {
                    System.out.println("No tienes suficiente dinero para edificar.");
                }
            } 
        }
        else{
            System.out.println("No puedes edificar en esta casilla porque no te pertenece.");
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



    //SECCIÓN DE CONTROL DEL FLUJO DE LA PARTIDA------------------------------------------------------------------------

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
                if(this.jugadores.size()<7) {
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
        System.out.println("La partida ha finalizado, esperamos que disfrutáseis la experiencia.");
        System.exit(0);
    }

    /**Método para acabar la partida cuando se termina de jugar o cuando alguien pierde*/
    private void acabarPartida() {
        partidaTerminada = true;
    }



    //SECCIÓN DE COMANDOS DEL MENÚ--------------------------------------------------------------------------------------

    /**Método que interpreta el comando introducido y toma la accion correspondiente.
     * @param comando_entero Línea de comando que introduce el jugador
     */
    private void analizarComando(String comando_entero) {

        switch(comando_entero){
            //Primer bloque de comandos: no dependen de una instancia

            //Acabar la partida
            case "terminar partida": case "acabar partida": acabarPartida(); break;

            //Indicar jugador que tiene el turno
            case "jugador": infoJugadorTurno(); break;

            //Lanzar los dados (hay que imprimir el tablero después)
            case "lanzar dados": lanzarDados(); verTablero(); break;

            //Salir de la cárcel
            case "salir carcel": salirCarcel(); break;

            //Pasar el turno al siguiente jugador
            case "acabar turno": acabarTurno(); break;

            //Imprimir el tablero
            case "ver tablero": verTablero(); break;

            //Cambiar el modo de movimiento del avatar
            case "cambiar modo": cambiarModo(); break;

            //Listar todas las propiedades en venta
            case "listar enventa": listarVenta(); break;

            //Listar todas las propiedades en venta
            case "listar jugadores": listarJugadores(); break;

            //Listar todas las propiedades en venta
            case "listar avatares": listarAvatares(); break;

            //Listar edificios (cuando se llama sin especificar el color del grupo)
            case "listar edificios": listarEdificios(null); break;

            //CHEATS
            //DINERO INFINITO (+mil millones)
            case "dinero infinito": dineroInfinito(); break;
            //AVANZAR 40 CASILLAS HASTA LA MISMA CASILLA
            case "dar vuelta": obtenerTurno().sumarVuelta(); break;
            //PROBANDO LA IMPRESIÓN DE CARTAS
            case "probar cartas": probarCartas(); break;
            case "coger carta caja": cogerCarta(this.cartas_caja); break;
            case "coger carta suerte": cogerCarta(this.cartas_suerte); break;


            // Algunos mensajes concretos a comandos inválidos
            case "empezar partida": System.out.println("¡La partida ya está empezada! \uD83D\uDE21"); break;

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

                        //Para describir una casilla
                        case "describir": descCasilla(comando[1]); break;

                        //Para comprar una casilla
                        case "comprar": comprar(comando[1]); break;

                        // Comandos de edificar, hipotecar, deshipotecar (para pista de deporte en length==4)
                        case "edificar": edificar(comando[1]); break;
                        case "hipotecar": hipotecar(comando[1]); break;
                        case "deshipotecar": deshipotecar(comando[1]); break;

                        //Comando inválido
                        default: System.out.println(comando_entero + " no es un comando válido."); break;
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
                    else if("listaredificios".equals(comando[0]+comando[1])){
                        listarEdificios(comando[2]);
                    }



                    // CHEAT PARA SACAR LO QUE QUIERAS CON LOS DADOS
                    // Si alguno de los valores de dados introducidos no es válido
                    // se imprime un mensaje de error (lo hace dadoValido) y no hace nada
                    else if("dados".equals(comando[0])) {
                        int dado1 = dadoValido(comando[1]);
                        int dado2 = dadoValido(comando[2]);
                        if (dado1!=0 && dado2!=0) {
                            Jugador jugador_actual = obtenerTurno();
                            if(jugador_actual.getAvatar().getMovimientoAvanzado()){
                                dadosTrampaAvanzado(dado1, dado2);
                                verTablero();
                            }else{
                                dadosTrampa(dado1, dado2);
                                verTablero();
                            }

                        }
                    }
                    else {
                        System.out.println(comando_entero + " no es un comando válido.");
                    }
                }

                else if(comando.length==4){
                    if(comando[0].equals("vender")){
                        venderEdificios(comando[1], comando[2], Integer.parseInt(comando[3]));
                    }
                    else if("pistadedeporte".equals(comando[1]+comando[2]+comando[3])) {
                        // Comandos de edificar, hipotecar, deshipotecar PARA PISTA DE DEPORTE
                        if("edificar".equals(comando[0])) {
                            edificar("pista de deporte");
                        }
                        else if("hipotecar".equals(comando[0])) {
                            hipotecar("pista de deporte");
                        }
                        else if("deshipotecar".equals(comando[0])) {
                            deshipotecar("pista de deporte");
                        }
                        else {
                            System.out.println(comando_entero + " no es un comando válido.");
                        }
                    }
                    else if("crearjugador".equals(comando[0] + comando[1])){
                        System.out.println("No se pueden crear más jugadores con la partida empezada.");
                    }
                    else{
                        System.out.println("Comando inválido. Si esta relacionado con edificaciones recuerda <casa, hotel, piscina, pista de deporte>.");
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
        System.out.println("{");
        System.out.println("\tnombre: " + jugador.getNombre() + ",");
        System.out.println("\tavatar: " + jugador.getAvatar().getId());
        System.out.println("}");
    }

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
        jugador.getEstadisticas().sumarVecesSalida(1);
        jugador.getEstadisticas().sumarDineroSalidaRecaudado(Valor.SUMA_VUELTA);
        cuatroVueltasSinCompras();
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
                            obtenerTurno().getEstadisticas().sumarVecesTirado(1);
                            jugador.setTiradasCarcel(1+jugador.getTiradasCarcel());
                        }
                    }
                }
                else {
                    // No estás encarcelado: TURNO NORMAL
                    // Variables útiles
                    obtenerTurno().getEstadisticas().sumarVecesTirado(1);
                    int sumaDados = resultado1+resultado2;
                    Casilla origen = avatar.getLugar();

                    // POR FIN Movemos al avatar a la casilla
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
                        // Encarcelado por caer
                        else {
                            jugador.encarcelar(this.tablero.getPosiciones());
                        }
                    }

                    System.out.println("El avatar " + avatar.getId() + " avanza " + (sumaDados) +
                            " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");

                    // Si pasamos por la salida hay que cobrar!
                    if (origen.getPosicion() > destino.getPosicion()) {
                        cobrarSalida(jugador);
                    }

                    // EVALUAMOS QUÉ HAY QUE HACER EN FUNCIÓN DE LA CASILLA
                    if (!evaluarCasilla(avatar.getLugar())) {
                        if(jugador.estaEnBancarrota()){
                            Scanner scan = new Scanner(System.in);
                            
                            String respuesta;
                            do {//con esto forzamos a que se declare en bancarrota
                                System.out.println("Debes declararte en bancarrota");
                                respuesta = scan.nextLine();
                            } while (!respuesta.equals("bancarrota"));
                            
                            analizarComando(respuesta);
                            scan.close();
                        }
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
    private void lanzarDadoAvanzados() {

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
                    // avatar.moverAvatar(tablero.getPosiciones(), sumaDados);

                    Casilla destino = avatar.getLugar();


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


                    // EVALUAMOS QUÉ HAY QUE HACER EN FUNCIÓN DE LA CASILLA
                    if (evaluarCasilla(destino)) {
                        Scanner scan = new Scanner(System.in);

                        String respuesta;
                        do {//con esto forzamos a que se declare en bancarrota
                            System.out.println("Debes declararte en bancarrota");
                            respuesta = scan.nextLine();
                        } while (!respuesta.equals("bancarrota"));

                        analizarComando(respuesta);
                        scan.close();
                    }


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
        Grupo grupoCasilla = casilla.getGrupo();
        //sumamos en uno las veces visitadas
        casilla.sumarVecesVisitada(1);


        if (jugadorActual != duenhoCasilla) {
            switch (casilla.getTipo()) {
                case "Solar":
                    if (!casilla.estaHipotecada()) {
                        if (duenhoCasilla != this.banca) {
                            Jugador propietario = casilla.getDuenho();
                            casilla.evaluarAlquiler();
                            impuestoCasilla = casilla.getImpuesto();
                            System.out.printf("%s debe pagarle el alquiler de %s a %s: %,.0f€\n",
                                    jugadorActual.getNombre(), nombreCasilla, propietario.getNombre(), impuestoCasilla);
                            jugadorActual.sumarGastos(impuestoCasilla);
                            jugadorActual.getEstadisticas().sumarPagoDeAlquileres(impuestoCasilla);
                            duenhoCasilla.getEstadisticas().sumarCobroDeAlquileres(impuestoCasilla);
                            jugadorActual.getAvatar().getLugar().sumarDinero_recaudado(impuestoCasilla);

                            jugadorActual.restarFortuna(impuestoCasilla);

                            if (jugadorActual.estaEnBancarrota()){
                                jugadorActual.setDeudaConJugador(duenhoCasilla);
                                return false;
                            }

                            propietario.sumarFortuna(impuestoCasilla);
                            return true;
                        } else {
                            System.out.println("La casilla " + nombreCasilla + " está a la venta.\n");
                        }
                    } else {
                        System.out.println("La casilla " + nombreCasilla +   " esta hipotecada. No es necesario pagar el alquiler.");
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
                            ;
                            jugadorActual.encarcelar(this.tablero.getPosiciones());
                            break;
                        case "Salida":
                            System.out.println("Has llegado a la casilla de Salida.");
                            break;
                        default:
                            System.out.println("Error en evaluar casilla.");
                            return false;
                    }
                    return true;

                case "Transporte":
                    if (!casilla.estaHipotecada()) {
                        if (duenhoCasilla != this.banca) {
                            casilla.evaluarAlquiler();
                            System.out.printf("%s debe pagar el servicio de transporte a %s: %,.0f€\n",
                                    jugadorActual.getNombre(), duenhoCasilla.getNombre(), impuestoCasilla);
                            jugadorActual.sumarGastos(impuestoCasilla);
                            jugadorActual.restarFortuna(impuestoCasilla);
                            jugadorActual.getEstadisticas().sumarPagoDeAlquileres(impuestoCasilla);
                            duenhoCasilla.getEstadisticas().sumarCobroDeAlquileres(impuestoCasilla);
                            jugadorActual.getAvatar().getLugar().sumarDinero_recaudado(impuestoCasilla);
                            if (jugadorActual.estaEnBancarrota()) {
                                jugadorActual.setDeudaConJugador(duenhoCasilla);
                                return false;
                            }

                            duenhoCasilla.sumarFortuna(impuestoCasilla);
                            return true;
                        } else {
                            System.out.println("La casilla " + nombreCasilla + " está a la venta.");
                        }
                    } else {
                        System.out.println("La casilla " + nombreCasilla + " está hipotecada, ¡No pagas!");
                    }
                    break;

                case "Impuestos":
                    System.out.printf("Debes pagar tus impuestos a la banca: %,.0f€\n", impuestoCasilla);
                    jugadorActual.sumarGastos(impuestoCasilla);
                    jugadorActual.restarFortuna(impuestoCasilla);
                    jugadorActual.getEstadisticas().sumartImpuestosPagados(impuestoCasilla);
                    //NO SE SI EST UN GASTO
                    jugadorActual.sumarGastos(impuestoCasilla);

                    if (jugadorActual.estaEnBancarrota()){
                        jugadorActual.setDeudaConJugador(this.banca);
                        return false;
                    }

                    this.banca.sumarFortuna(impuestoCasilla);
                    return true;

                case "Servicio":
                    if (!casilla.estaHipotecada()) {
                        if (duenhoCasilla != this.banca) {
                            float pagar = tirada * (duenhoCasilla.numeroCasillasTipo("Servicio") == 1 ? 4 : 10) * impuestoCasilla;
                            System.out.printf("%s debe pagar el servicio a %s: %,.0f€\n",
                                    jugadorActual.getNombre(), duenhoCasilla.getNombre(), pagar);
                            jugadorActual.sumarGastos(pagar);
                            jugadorActual.restarFortuna(pagar);
                            jugadorActual.getEstadisticas().sumarPagoDeAlquileres(pagar);
                            duenhoCasilla.getEstadisticas().sumarCobroDeAlquileres(pagar);
                            jugadorActual.getAvatar().getLugar().sumarDinero_recaudado(pagar);

                            if (jugadorActual.estaEnBancarrota()){
                                jugadorActual.setDeudaConJugador(duenhoCasilla);
                                return false;
                            }

                            duenhoCasilla.sumarFortuna(pagar);
                            return true;
                        } else {
                            System.out.println("La casilla " + nombreCasilla + " está a la venta.");
                        }
                    } else {
                        System.out.println("La casilla " + nombreCasilla + " Esta hipotecada");
                    }
                    break;

                case "Caja de comunidad":
                    cogerCarta(this.cartas_caja);
                    break;
                case "Suerte":
                    cogerCarta(this.cartas_suerte);
                    break;

                default:
                    System.out.println("Error en evaluarCasilla: tipo de casilla inválido.");
                    return false;
            }
        } else {
            System.out.println("Esta casilla te pertenece.");
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
        // Comprobar si el jugador actual ya lanzó los dados en su turno
        if (this.tirado) {
            // Si los lanzó y sacó dobles está obligado a volver a tirar!
            // A no ser que lo acaben de encarcelar
            if (this.dado1.getValor()==this.dado2.getValor() && !obtenerTurno().isEnCarcel()) {
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

    /**Método para cambiar el modo de movimiento del avatar que tiene el turno*/
    private void cambiarModo() {
        obtenerTurno().getAvatar().cambiarMovimiento();
        Avatar avatar = obtenerTurno().getAvatar();
        if(avatar.getMovimientoAvanzado()) {
            System.out.println("El avatar " + avatar + " vuelve el movimiento normal.");
        }
        else {
            System.out.println("El avatar " + avatar.getId() + " activa al movimiento avanzado (" + avatar.getTipo() + ")");
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



    //SECCIÓN DE COMANDOS QUE DEPENDEN DE DOS INSTANCIAS----------------------------------------------------------------

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



    // SECCIÓN DE MÉTODOS RELACIONADOS CON LAS CARTAS TIPO SUERTE Y CAJA DE COMUNIDAD-----------------------------------

    /**Método para cuando se cae en una casilla de tipo Suerte o Caja de comunidad
     * [1] Reordena de manera aleatoria el ArrayList de cartas correspondiente
     * [2] Le pide al usuario el número de la carta que quiere escoger (del 1 al 6)
     */
    public void cogerCarta(ArrayList<Carta> baraja) {
        System.out.println("Barajando las cartas...");
        Collections.shuffle(baraja); //Barajamos las cartas
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
            switch(carta.getIndice()) {
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
            switch(carta.getIndice()) {
                case 1: //Pagar 500.000€ (a la banca)
                    jugadorActual.restarFortuna(500000);
                    this.banca.sumarFortuna(500000);
                    break;
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
                    jugadorActual.restarFortuna(1000000);
                    this.banca.sumarFortuna(1000000);
                    break;
                case 6: //Pagar 200.000€ a cada jugador
                    jugadorActual.restarFortuna(200000 * (this.jugadores.size()-1) );
                    //Recorremos el ArrayList de jugadoresda: si no es el jugador actual sumamos 200.000€
                    for(Jugador j : this.jugadores) {
                        if(!j.equals(jugadorActual)) {
                            j.sumarFortuna(200000);
                        }
                    }
                    break;
            }
        }
        else {
            System.out.println("Esta carta tiene un tipo inválido.");
        }

        //Falta implementar si el jugador es solvente o no
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
                            obtenerTurno().getEstadisticas().sumarVecesTirado(1);
                            jugador.setTiradasCarcel(1+jugador.getTiradasCarcel());
                        }
                    }
                }
                else {
                    // No estás encarcelado: TURNO NORMAL
                    // Establecemos la casilla de salida (solo para imprimirla después)
                    obtenerTurno().getEstadisticas().sumarVecesTirado(1);

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
                        cobrarSalida(jugador);
                    }

                    //EVALUAMOS QUÉ HAY QUE HACER EN FUNCIÓN DE LA CASILLA
                    evaluarCasilla(avatar.getLugar());

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


    private void dadosTrampaAvanzado(int d1, int d2) {

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
                            obtenerTurno().getEstadisticas().sumarVecesTirado(1);
                            jugador.setTiradasCarcel(1+jugador.getTiradasCarcel());
                        }
                    }
                }
                else {
                    // No estás encarcelado: TURNO NORMAL
                    // Establecemos la casilla de salida (solo para imprimirla después)
                    obtenerTurno().getEstadisticas().sumarVecesTirado(1);

                    //Variables útiles
                    int sumaDados = resultado1+resultado2;
                    Casilla origen = avatar.getLugar();

                    moverpersonajes(jugador,sumaDados,true);

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

                    System.out.println("El avatar " + avatar.getId() + " se movio " + (sumaDados) +
                            " casillas desde " + origen.getNombre() + " hasta " + destino.getNombre() + ".");

                    // a pensar esta
                    if (origen.getPosicion() > destino.getPosicion()) {
                        cobrarSalida(jugador);
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

    // MÉTODOS SIN GRUPO:

    /**Método que transforma un String a un entero si el String es un número entre 1 y 6.
     * Si no es un número válido imprime un mensaje de error y devuelve 0.
     * Método auxiliar para leerDadoValido() pero que también sirve para dadosTrampa().
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
        Scanner scan= new Scanner(System.in);
        int n=0;

        // Bucle que para cuando metemos un número entre 1 y 6 por teclado
        while(n==0) {
            String numero = scan.nextLine();
            // dadoValido() transforma el String en un número
            // Si no es un número válido devuelve 0
            n=dadoValido(numero);
        }
        
        return n;
    }

    private boolean esTipoAvatar(String tipo) {
        return "coche".equals(tipo) ||
                "esfinge".equals(tipo) ||
                "sombrero".equals(tipo) ||
                "pelota".equals(tipo);
    }
    public void moverpersonajes(Jugador jugador, int valorTirada, boolean trucado){
        //pillamos la casilla donde se encuentra y su valor en posicion de dicha casilla
        Casilla posicionActualC = jugador.getAvatar().getLugar();
        int posicionActual = posicionActualC.getPosicion();
        //reiniciamos parametro comprado EL COCHE SOLO PUEDE COMPRAR UNA VEZ
        boolean haComprado = false;

        //switch de los 4 tipos de avatares posibles
        switch (jugador.getAvatar().getTipo()) {
            case "coche":
                /*
                EL COCHE PUEDE TIRAR HASTA 3 VECES MIENTRAS EL RESULTADO DE LOSS DADOS SEA MAYOR QUE 4
                SOLO PUEDE COMPRAR 1 CASILLA EN ESOS 3 LANZAMIENTOS MAXIMOS
                PUEDE EDIFICAR LAS VECES QUE QUIERA
                SI SACA MENOS DE 4 NO PUEDE LANZAR EN 2 TURNOS PERO TIENE DERECHO A ACABAR SU TURNO ES DECIR EJECUTAR ACCIONES
                 */
                int lanzamientosRestantes = 3; // Máximo de tres lanzamientos adicionales

                while (valorTirada > 4 && lanzamientosRestantes > 0) {
                    jugador.getAvatar().moverAvatar(this.tablero.getPosiciones(), valorTirada);
                    posicionActual = jugador.getAvatar().getLugar().getPosicion();

                    // SI NO HA COMPRADO , LA CASILLA ES COMPRABLE O LA CASILA LE PERTENECE
                    if (!haComprado && jugador.getAvatar().getLugar().esTipoComprable() || jugador.getNombre().equals( jugador.getAvatar().getLugar().getDuenho().getNombre()) ) {
                        //HACE LA ACCION DE LA CASILLA EN EVALUAR
                        if( evaluarCasilla(jugador.getAvatar().getLugar())){
                            //YA QUE NO PODEMOS ACCEDER AL ANALIZADOR DE COMANDOS
                            //PREGUNTAMOS SI QUIERE COMPRAR Y LLAMAMOS COMPRAR DESDE AQUI
                            Scanner scanner = new Scanner(System.in);
                            System.out.print("¿Quieres comprar la casilla " + jugador.getAvatar().getLugar().getNombre() + "? (0/1): ");
                           //SI QUIERE COMPRAR == 1 HACE LO SIGUIENTE
                            if (scanner.nextInt() == 1) {
                                comprar(jugador.getAvatar().getLugar().getNombre());
                                haComprado = true; // Marca que ya se ha realizado una compra en este turno
                            }
                        }

                    }

                    // Permitir edificación en cada intento
                    String nombre_casilla = obtenerTurno().getAvatar().getLugar().getNombre();
                    //ES EDIFICABLE MIRA SI ES EL DUENHO Y SI ES POSIBLE EDIFICAR EN ELLA
                    if( obtenerTurno().getAvatar().getLugar().esEdificable(nombre_casilla,obtenerTurno())){
                        System.out.print("¿Quieres edificar en esta casilla? (0/1): ");
                        Scanner scanner = new Scanner(System.in);
                        if (scanner.nextInt() == 1) {
                            //Le preguntamos el tipo de edificacion
                            System.out.print("Los tipos de edificacion son 'C' casa, 'H' hotel, 'P' piscina, 'D' pista de deporte \n");
                            System.out.print("¿Que quieres edificar?");
                            Scanner scanner2 = new Scanner(System.in);
                            String tipo = scanner2.next();
                            //el if comprueba que sea posible eddificar el tipo elegido
                            if(obtenerTurno().getAvatar().getLugar().esEdificable(tipo,obtenerTurno())){
                                //EDIFICA EL TIPO ELEGIDO
                                switch (tipo){
                                    case "C":
                                        edificar("casa");
                                        break;
                                    case "H":
                                        edificar("hotel");
                                        break;
                                    case "P":
                                        edificar("piscina");
                                        break;
                                    case "D":
                                        edificar("pista de deporte");
                                        break;
                                }
                            }else {
                                System.out.print("No es posible edificar una casilla de "+ tipo + " en esta casilla");
                            }


                        }

                    }

                    // Reducir el contador de lanzamientos restantes y volver a tirar los dados
                    lanzamientosRestantes--;
                    //SI EL DADO ES TRUCADO  HAY QUE ELEGIR LOS NUMEROS
                    if(trucado){
                        System.out.print("introduce el valor del primer dado ");
                        Scanner scanner3 = new Scanner(System.in);
                        int dado1 = scanner3.nextInt();
                        System.out.print("introduce el valor del segundo dado ");
                        int dado2 = scanner3.nextInt();
                        valorTirada = dado1+ dado2;
                    } else {
                        //los porcentajes es para asegurar que el numero es valido
                        valorTirada = this.dado1.tirarDado()%7 + this.dado2.tirarDado()%7;
                    }
                }

                if (valorTirada <= 4) {
                    // Retroceder el número de casillas correspondiente y bloquear lanzamientos en los siguientes dos turnos
                    jugador.getAvatar().moverAvatar(this.tablero.getPosiciones(), -valorTirada);
                    evaluarCasilla(jugador.getAvatar().getLugar());
                    jugador.setBloqueado(3);
                }

                break;
            case "pelota":
                /*
                LA PELOTA SI EL RESULTADO ES MAYOR QUE 4 SE MUEVE LAS CASILLAS DE FORMA NORMAL PERO EJECUTA LAS ACCIONES
                DE LAS CASILLAS IMPARES QUE HAY DE CAMINO ES DECIR PAGA IMPUESTOS, EJECUTA CARTAS, ALQUILERES IR CARCEL COMPRAR

                SI SACA MENOS DE 4 ESTA HARA LO MISMO PERO MARCHA ATRAS
                 */
                int incremento = (valorTirada > 4) ? 1 : -1;
                int posicionFinal =   (posicionActual + valorTirada * incremento)% 40;
                int posicion_inicial = posicionActual;

                while (posicionActual != posicionFinal) {
                    jugador.getAvatar().moverAvatar(this.tablero.getPosiciones(),incremento);
                    posicionActual= jugador.getAvatar().getLugar().getPosicion();
                    if (posicionActual%2 == 1){

                        verTablero();
                        if(evaluarCasilla(jugador.getAvatar().getLugar())) {
                            if (!jugador.isEnCarcel()) {
                                if (jugador.getAvatar().getLugar().esTipoComprable()) {
                                    Scanner scanner = new Scanner(System.in);
                                    System.out.print("Quieres comprarla la casilla " + jugador.getAvatar().getLugar().getNombre()+" pon 0/1 segun tu decision: ");
                                    int numero = scanner.nextInt();
                                    if (numero == 1) {
                                        comprar(jugador.getAvatar().getLugar().getNombre());


                                    }
                                }
                            }
                        }
                    }

                    }

                break;
            case "esfinge":
                System.out.println("Pues esta es pa la soiguiente entrega :)");
                break;
            case "sombrero":
                System.out.println("Pues esto es pa la siguiente entrega :).");
                break;
            default:
                System.out.println("Caso no reconocido.");
                break;

    }

}
    private void estadisticasGenerales() {
        System.out.println("{");

        // Obtener la casilla más visitada
        Casilla casillaAux;
        int vecesMaxima = 0;

        // Primer for: determinar la cantidad máxima de visitas
        for (int i = 0; i < 40; i++) {
            casillaAux = tablero.getCasilla(i);
            if (vecesMaxima < casillaAux.getVecesVisitada()) {
                vecesMaxima = casillaAux.getVecesVisitada();
            }
        }

        // Segundo for: construir la lista de nombres de las casillas más visitadas
        StringBuilder nombresCasillas = new StringBuilder();
        for (int i = 0; i < 40; i++) {
            casillaAux = tablero.getCasilla(i);
            if (vecesMaxima == casillaAux.getVecesVisitada()) {
                if (nombresCasillas.length() > 0) {
                    nombresCasillas.append(", "); // Agrega una coma solo si ya hay un nombre en la cadena
                }
                nombresCasillas.append(casillaAux.getNombre());
            }
        }
        String nombre_max = "NULL";
        float recaudacionMaxima = 0;
        for (int i = 0; i < 40; i++) {
            casillaAux = tablero.getCasilla(i);
            if (recaudacionMaxima < casillaAux.getDinero_recaudado()) {
                recaudacionMaxima = casillaAux.getDinero_recaudado();
                nombre_max = casillaAux.getNombre();
            }
        }
        String nombre_vueltas = "Null";
        int max_vueltas  = 0;
        for(Jugador j: jugadores) {
            if (max_vueltas < j.getVueltas()) {
                max_vueltas = j.getVueltas();
                nombre_vueltas = j.getNombre();
            }
        }
        String nombre_dados = "Null";
        int max_tiradas  = 0;
        for(Jugador j: jugadores) {
            if (max_tiradas < j.getEstadisticas().getVecesTirado()) {
                max_tiradas = j.getEstadisticas().getVecesTirado();
                nombre_dados = j.getNombre();
            }
        }
        String nombre_cabwza = "Null";
        float max_dinero  = 0;
        for(Jugador j: jugadores) {
            if (max_dinero < j.getFortuna()) {
                max_dinero = j.getFortuna();
                nombre_cabwza = j.getNombre();
            }
        }



        // Imprimir los nombres de las casillas más visitadas en una sola línea
        System.out.println("Casillas más visitadas: " + nombresCasillas.toString());
        System.out.print("La casilla mas visitada es :" + nombre_max);
        System.out.print("El jugador con mas vueltas  es :" + nombre_vueltas);
        System.out.print("El jugador con mas tiradas  es :" + nombre_dados);
        System.out.print("El jugador en cabeza es  es :" + nombre_cabwza);

        System.out.println("}");
    }
}





