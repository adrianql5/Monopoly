package monopoly;

import java.util.ArrayList;

import partida.*;



public class Casilla {

    //ATRIBUTOS
    private String nombre;//Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicio, Comunidad).
    private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicio/transportes o impuestos.
    private float hipoteca; //Valor otorgado por hipotecar una casilla
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.
    private ArrayList<Edificacion> edificaciones;


    //SECCIÓN DE CONSTRUCTORES DE CASILLA
    public Casilla() {
    }//Parámetros vacíos

    /**Constructor para casillas tipo Solar, Servicio y Transporte.
     * @param nombre Nombre de la casilla
     * @param tipo Debe ser Solar, Servicio o Transporte
     * @param posicion Posición en el tablero
     * @param valor Valor de la casilla
     * @param duenho Dueño de la casilla
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this.nombre=nombre;
        this.tipo= tipo;
        this.posicion= posicion;
        this.valor= valor;
        this.impuesto= valor * 0.10f;
        this.hipoteca= valor/2f;
        this.duenho= duenho;
        this.avatares= new ArrayList<Avatar>();
        this.edificaciones= new ArrayList<Edificacion>();
    }

    /**Constructor para casillas de tipo Impuestos.
     * @param nombre Nombre de la casilla
     * @param posicion Posición en el tablero
     * @param impuesto Impuesto establecido
     * @param duenho Dueño de la casilla
     */
    public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.nombre=nombre;
        this.posicion= posicion;
        this.tipo="Impuestos";
        this.impuesto=impuesto;
        this.duenho= duenho;
        this.avatares=new ArrayList<Avatar>();
    }

    /**Constructor para casillas tipo Suerte, Caja de comunidad y Especiales.
     * @param nombre Nombre de la casilla
     * @param tipo Suerte, Caja de comunidad o Especiales
     * @param posicion Posición en el tablero
     * @param duenho Dueño de la casilla
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this.nombre=nombre;
        this.tipo= tipo;
        this.posicion= posicion;
        this.duenho= duenho;
        this.avatares= new ArrayList<Avatar>();

    }


    //SECCIÓN DE MÉTODOS ÚTILES DE CASILLA

    /**Método utilizado para añadir un avatar al array de avatares en casilla.*/
    public void anhadirAvatar(Avatar av) {
        this.avatares.add(av);
    }

    /**Método utilizado para eliminar un avatar del array de avatares en casilla.*/
    public void eliminarAvatar(Avatar av) {
        this.avatares.remove(av);
    }

 



    /**Método para añadir valor a una casilla. Utilidad:
     * (1) Sumar valor a la casilla de parking.
     * (2) Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
     * @param suma Cantidad a añadir al valor de la casilla
     */
    public void sumarValor(float suma) {
        this.valor +=suma;
    }

    /**Método escoger una carta si se cae en una casilla del tipo Suerte o Caja de comunidad
     * [1] Baraja las cartas ---de momento no implementado
     * [2] Se pregunta al jugador qué carta quiere coger de las 6 que hay
     * [3] Se muestra la descripción de la carta que escogió el jugador
     * [4] Se realizan las acciones pertinentes ---de momoento no implementado
     */
    private void coger() {

    }

    /** Método para evaluar qué hacer en una casilla concreta.
     * @param actual Jugador cuyo avatar está en la casilla
     * @param banca Se usa para ciertas comprobaciones
     * @param tirada Valor de la tirada (para determinar impuesto a pagar en casillas de servicio)
     * @return TRUE en caso de ser solvente (es decir, de cumplir las deudas), y FALSE en caso de no serlo
     */
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if(actual != this.duenho) {
            switch (this.tipo) {
                case "Solar":
                    // Si pertenece a otro jugador le debe pagar el alquiler
                    if (this.duenho!=banca) {
                        //Teoricamente si no tiene dinero para pagar se queda en negativo y se acaba la partida
                        Jugador propietario = this.duenho;
                        float alquiler;
                        if(this.grupo.esDuenhoGrupo(propietario)) {
                            alquiler = this.impuesto * 2f;
                        }else{
                            alquiler = this.impuesto;
                        }
                        System.out.printf("%s debe pagarle el alquiler de %s a %s: %,.0f€\n",
                                actual.getNombre(), this.nombre, propietario.getNombre(), alquiler);
                        actual.sumarGastos(alquiler);
                        actual.restarFortuna(alquiler);

                        // Si está en bancarrota se acaba la partida (y no se le ingresa nada al propietario)
                        if (actual.estaEnBancarrota()) {
                            return false;
                        }

                        propietario.sumarFortuna(alquiler);
                        return true;
                    } else {
                        System.out.println("La casilla " + this.getNombre() + " está a la venta.\n");
                        return true;
                    }

                case "Especial":
                    if (this.nombre.equals("Carcel")) {
                        System.out.println("Has caído en la Carcel. Disfruta de la visita.");
                        //System.out.println("Has caído en la Carcel. Tienes 3 opciones para salir: Pagar, Usar Carta de Suerte o Sacar Dados Dobles.\n");
                        return true;
                    } else if (this.nombre.equals("Parking")) {
                        // Nota: el valor del bote se imprime desde la función que llama a esta (lanzar dados)
                        // porque es un atributo del menú y habría que pasarlo como argumento
                        System.out.printf("Enhorabuena, has ganado el bote almacenado por impuestos, tasas y multas: %,.0f€\n", banca.getFortuna());
                        actual.sumarFortuna(banca.getFortuna());
                        banca.setFortuna(0f);
                        return true;
                    } else if (this.nombre.equals("IrCarcel")) {
                        System.out.println("Mala suerte, te vas a la cárcel.");
                        return true;
                    } else if (this.nombre.equals("Salida")) {
                        // Todo lo que hay que hacer en este caso ya lo hace lanzarDados()
                        // ya que va a haber que cobrar cuando se pase NO SOLO cuando se caiga
                        return true;
                    }
                    System.out.println("Error en evaluar casilla.");
                    break;

                case "Transporte":

                    // Se paga el 25% del valor total de la casilla Transporte por cada casilla que tengas de este tipo
                    int multiplicador = this.duenho.numeroCasillasTipo("Transporte");

                    if (this.duenho!=banca) {
                        float total = multiplicador * 0.25f * this.impuesto;
                        System.out.printf("%s debe pagarle el servicio de transporte a %s: %,.0f€\n",
                                actual.getNombre(), this.duenho.getNombre(), total);
                        actual.sumarGastos(total);
                        actual.restarFortuna(total);
                        if (actual.estaEnBancarrota()) return false;

                        this.duenho.sumarFortuna(total);
                        return true;
                    } else {
                        System.out.println("La casilla " + this.getNombre() + " está a la venta.");
                        return true;
                    }


                case "Impuestos":
                    System.out.printf("Vaya! Debes pagar tus impuestos a la banca: %,.0f€\n", this.impuesto);
                    actual.sumarGastos(this.impuesto);
                    actual.restarFortuna(this.impuesto);
                    if (actual.estaEnBancarrota()) return false;

                    banca.sumarFortuna(this.impuesto);
                    return true;


                case "Servicio":
                    if (this.duenho!=banca) {
                        // Se paga el valor de los dados*impuesto*factor_servicio
                        // El factor de servicio es 4 si el dueño posee 1 casilla de servicio, 10 si posee las 2.
                        float pagar;
                        if(this.duenho.numeroCasillasTipo("Servicio")==1) {
                            pagar = tirada * 4 * this.impuesto;
                        } else {
                            pagar = tirada * 10 * this.impuesto;
                        }

                        System.out.printf("%s debe pagarle el servicio a %s: %,.0f€\n",
                                actual.getNombre(), this.duenho.getNombre(), pagar);
                        actual.sumarGastos(pagar);
                        actual.restarFortuna(pagar);
                        if (actual.estaEnBancarrota()) return false;

                        this.duenho.sumarFortuna(pagar);
                        return true;
                    } else {
                        System.out.println("La casilla " + this.getNombre() + " está a la venta.");
                        return true;
                    }

                case "Caja de comunidad":
                case "Suerte":
                    //sin implementar
                    break;

                default:
                    System.out.println("Error en evaluarCasilla, tipo invalido.");
                    return false;
            }
        }else {
            System.out.println("Esta casilla te pertenece.");
        }
        return false;
    }

    /** Método usado para comprar una casilla determinada.
     * @param solicitante Jugador que solicita la compra de la casilla
     * @param banca La banca es el dueño de las casillas no compradas aún
     */
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        // ¿La casilla es de un tipo que se pueda comprar?
        if(this.esTipoComprable()) {
            // ¿El jugador que la quiere comprar está encima de la casilla?
            if(solicitante.getAvatar().getLugar()==this) {
                // ¿La casilla pertenece a la banca?
                if(this.duenho==banca) {
                    // ¿El jugador tiene suficiente dinero para poder pagarla?
                    if (solicitante.getFortuna()>=this.valor) {
                        solicitante.restarFortuna(this.valor);
                        solicitante.sumarGastos(this.valor);
                        banca.sumarFortuna(this.valor);  //Ojo que la banca acumula el precio de las compras en el bote
                        banca.eliminarPropiedad(this);
                        solicitante.anhadirPropiedad(this);
                        this.duenho=solicitante;

                        System.out.printf("%s ha comprado la propiedad %s por el precio de %,.0f€\n",
                                solicitante.getNombre(), this.nombre, this.valor);
                    }
                    else {
                        System.out.println("No tienes dinero para comprar esta casilla.");
                    }
                }
                else {
                    System.out.println("Esta casilla no está en venta.");
                }
            }
            else {
                System.out.println("¡Tienes que caer en la casilla para poder comprarla!");
            }
        }
        else {
            System.out.println("¡¡Esta casilla no se puede comprar!! \uD83D\uDE21");
        }
    }


    /**Método para verificar si el tipo de una casilla la hace comprable*/
    public boolean esTipoComprable() {
        return (this.tipo.equals("Solar") || this.tipo.equals("Transporte") || this.tipo.equals("Servicio"));
    }

    // Nota: creo q esta no es la implementación que pedían pero de todos modos no lo usamos
    /** Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() {
        if(this.duenho==null || this.duenho.getNombre().equals("Banca")){
            return "La casilla" + this.nombre + "está en venta por un precio de " + this.valor +"\n";
        }
        return "Esta casilla no está en venta";
    }

    public boolean esDuenho(Jugador jugador){
        return this.duenho == jugador;
    }

    
    //SECCIÓN QUE DEVUELVE INFORMACIÓN DE CASILLA

    /**Método que devuelve una cadena con información específica de cada tipo de casilla.
     * La usa descCasilla excepto cuando es una casilla que no se puede describir o para Parking y Carcel.
     * Se da por hecho que es una casilla que se puede describir (excepto Parking y Carcel).
     * Nota: los imprime con salto de línea al final.
     * Nota 2: por cómo está implementado descCasilla, el tipo Especial devuelve info para Salida.
     */
    public String infoCasilla() {

        // Cadena con la información que queremos devolver
        String info = "{\n";

        // La completamos con la información correspondiente en función del tipo de casilla
        switch(this.tipo) {
            case "Solar":
                info += "\tTipo: " + this.tipo + "\n";
                info += "\tColor del grupo: " + this.grupo.getColorGrupo() + "\n";
                info += "\tDueño: " + this.duenho.getNombre() + "\n";
                info += String.format("\tPrecio: %,.0f€\n", this.valor);
                info += String.format("\tAlquiler: %,.0f€\n", this.impuesto);
                info += String.format("\tHipoteca: %,.0f€\n", this.hipoteca);
                info += String.format("\tPrecio casa: %,.0f€\n", this.valor * 0.60f);
                info += String.format("\tPrecio hotel: %,.0f€\n", this.valor * 0.60f);
                info += String.format("\tPrecio piscina: %,.0f€\n", this.valor * 0.40f);
                info += String.format("\tPrecio pista de deporte: %,.0f€\n", this.valor * 1.25f);
                info += String.format("\tAlquiler de 1 casa: %,.0f€\n", this.impuesto * 5f);
                info += String.format("\tAlquiler de 2 casas: %,.0f€\n", this.impuesto * 15f);
                info += String.format("\tAlquiler de 3 casas: %,.0f€\n", this.impuesto * 35f);
                info += String.format("\tAlquiler de 4 casas: %,.0f€\n", this.impuesto * 50f);
                info += String.format("\tAlquiler hotel: %,.0f€\n", this.impuesto * 70f);
                info += String.format("\tAlquiler piscina: %,.0f€\n", this.impuesto * 25f);
                info += String.format("\tAlquiler pista de deporte: %,.0f€\n", this.impuesto * 25f);
                break;

            case "Transporte":
                info += "\tTipo: " + this.tipo + "\n";
                info += "\tDueño: " + this.duenho.getNombre() + "\n";
                info += String.format("\tPrecio: %,.0f€\n", this.valor);
                info += String.format("\tPago por caer: %,.0f€\n",
                        this.impuesto * 0.25f * this.duenho.numeroCasillasTipo("Transporte"));
                info += String.format("\t\t(cada casilla de este tipo que tengas suma 1/4 de %,.0f€ al alquiler)\n",
                        this.impuesto);
                info += String.format("\tHipoteca: %,.0f€\n", this.hipoteca);
                break;

            case "Servicio":
                info += "\tTipo: " + this.tipo + "\n";
                info += "\tDueño: " + this.duenho.getNombre() + "\n";
                info += String.format("\tPrecio: %,.0f€\n", this.valor);
                info += String.format("\tPago por caer: dados * x * %,.0f€\n", this.impuesto);
                info += "\t\t(x=4 si se posee una casilla de este tipo, x=10 si se poseen 2)\n";
                info += String.format("\tHipoteca: %,.0f€\n", this.hipoteca);
                break;

            case "Impuestos":
                info += "\tTipo: Impuestos\n";
                info += String.format("\tA pagar: %,.0f€\n", this.impuesto);
                break;
            case "Especial":
                // La única posibilidad de llegar aquí es que sea la casilla Salida (ver descCasilla en Menú)
                info += String.format("\tPago por vuelta: %,.0f€\n", Valor.SUMA_VUELTA);
                break;
            default:
                System.out.println("Error en la función infoCasilla.");
        }

        // Añadimos los jugadores en la casilla si los hay y cerramos
        info += jugadoresEnCasilla();
        info += "}\n";

        return info;
    }

    /**Método auxiliar que devuelve la lista de jugadores en una casilla.
     * Si no hay ningún jugador en la casilla devuelve una cadena vacía.
     * Nota: incluye el salto de línea al final.
     */
    public String jugadoresEnCasilla() {
        // Obtenemos la lista de avatares que hay en la casilla
        ArrayList<Avatar> avataresEnCasilla = this.getAvatares();

        // Creamos la cadena que se inicializa a vacía por si no hay avatares en la casilla
        String jugadores = "";

        // Si hubiera avatares se añaden al String
        if(!avataresEnCasilla.isEmpty()) {

            // Recorremos la lista de avatares y mostramos todos los jugadores en la misma línea
            jugadores += "\tJugadores: ";
            for (Avatar avatar : avataresEnCasilla) {
                jugadores += "[" + avatar.getJugador().getNombre() + "]  ";
            }
            jugadores += "\n";
        }

        return jugadores;
    }


    //SECCIÓN DE GETTERS Y SETTERS DE CASILLA

    public String getNombre(){
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }
  
    public void setTipo(String tipo_casilla) {
        switch(tipo_casilla) {
            case "Especial": case "Impuesto": case "Servicio": case "Transporte":
            case "Caja de comunidad": case "Suerte": case "Solar":
                this.tipo=tipo_casilla;
                break;
            default:
                System.out.println(tipo_casilla + " no es un tipo de casilla válido.\n");
        }
    }
    
    public float getValor() {
        return valor;
    }
    
    public void setValor(float valor_casilla) {
        if (valor_casilla > 0) {
            this.valor = valor_casilla;
        }
        else {
            System.out.println("El valor de la casilla debe ser positivo.\n");
        }
    }

    public int getPosicion(){
        return posicion;
    }
    
    public void setPosicion(int posicion_casilla) {
        if(posicion_casilla<40 && posicion_casilla>-1) {
            this.posicion = posicion_casilla;
        }
        else {
            System.out.println(posicion + " no es una casilla válida.\n");
        }
    }
    
    public Jugador getDuenho(){
        return duenho;
    }
   
    public void setDuenho(Jugador duenho_casilla) {
        if (duenho_casilla != null) {
            for (Avatar avatar : this.avatares) {
                // Si el jugador es el dueño de algún avatar en la casilla
                if (avatar.getJugador()==duenho_casilla)  {
                    break;
                }
                else{
                    this.duenho = duenho_casilla; // Asigna el nuevo dueño
                }
            }
        }
        else {
            System.out.println("El jugador proporcionado no es un dueño válido.\n");
        }
    }

    public Grupo getGrupo(){
        return grupo;
    }
    
    public void setGrupo(Grupo grupo_casilla) {
        if (grupo_casilla != null) {
            this.grupo = grupo_casilla;
        }
        else {
            System.out.println("El grupo no puede ser nulo.\n");
        }
    }

    public float getImpuesto(){
        return impuesto;
    }

    public void setImpuesto(float impuesto_casilla) {
        if (impuesto_casilla > 0) {
            this.impuesto = impuesto_casilla;
        }
        else {
            System.out.println("El impuesto debe ser un valor positivo.\n");
        }
    }

    public float getHipoteca(){
        return hipoteca;
    }
    
    public void setHipoteca(float hipoteca_casilla) {
        if (hipoteca_casilla > 0) {
            this.hipoteca = hipoteca_casilla;
        }
        else {
            System.out.println("La hipoteca debe ser un valor positivo.\n");
        }
    }
    
    public ArrayList<Avatar> getAvatares(){
        return avatares;
    }
}
