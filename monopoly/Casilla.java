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
        this.hipoteca= valor/2f;
        this.duenho= duenho;
        this.avatares= new ArrayList<Avatar>();
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
                    if (this.duenho!=banca) {//si pertenece a otro jugador le debe pagar el alquiler
                        //Teoricamente si no tiene dinero para pagar se queda en negativo y se acaba la partida
                        Casilla solar = actual.getAvatar().getLugar();
                        Jugador propietario = solar.getDuenho();
                        float alquiler = solar.getValor() * 0.10f;
                        System.out.printf("El jugador %s le debe pagar %,.0f€ por el alquiler a %s.\n",
                                actual.getNombre(), alquiler, propietario.getNombre());
                        actual.sumarGastos(alquiler);
                        actual.restarFortuna(alquiler);

                        propietario.sumarFortuna(alquiler);
                        if (actual.estaEnBancarrota()) return false;

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

                    //esto no sé donde lo viste adri pero en el pdf pone q hay q pagar SUMA_VUELTA aka TRANSPORTE
                    //int multiplicador = this.duenho.numeroCasillasTipo("Transporte"); // Inicialización de multiplicador

                    if (this.duenho!=banca) {
                        float total = Valor.TRANSPORTE;//pongo f porque si no pone un double, cositas de Java
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
                        float pagar = tirada * Valor.SUMA_VUELTA / 200f;//pongo f porque si no pone un double, cositas de Java
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

    /**Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.
     */
    public String infoCasilla() {
        String info= "{\n\tNombre casilla: " + this.nombre + "\n"
                + "\tTipo Casilla: " + this.tipo + "\n"
                + "\tPosicion: " + this.posicion + "\n"
                + "\tValor: " + String.format("%,.0f", this.valor) + "\n"
                + "\tDueño: " + (this.duenho != null ? this.duenho.getNombre() : "-") +"\n"
                + "\tColor del grupo: " + (this.grupo != null ? this.grupo.getColorGrupo() : "-") + "\n"
                + "\tValor hipoteca: " + String.format("%,.0f", this.valor/2f)+"€\n}\n";

        return info;
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