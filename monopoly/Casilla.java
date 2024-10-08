package monopoly;

import java.util.ArrayList;
import java.util.Scanner;

import partida.*;



public class Casilla {

    //ATRIBUTOS
    private String nombre;//Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad).
    private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float hipoteca; //Valor otorgado por hipotecar una casilla
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.

    //SECCION DE CONSTRUCTORES
    
    public Casilla() {
    }//Parámetros vacíos

    /**Constructor para casillas tipo Solar, Servicios y Transporte.
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
        this.tipo="Impuesto";
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

    
    //SECCIÓN DE MÉTODOS ÚTILES
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
     * @param tirada Valor de la tirada (para determinar impuesto a pagar en casillas de servicios)
     * @return TRUE en caso de ser solvente (es decir, de cumplir las deudas), y FALSE en caso de no serlo
     */
public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
    Scanner scan = new Scanner(System.in);
    String opcion;

    switch (this.tipo) {
        case "Solar":
            if (this.duenho == banca && actual.getFortuna() >= this.valor) {
                System.out.println("Puedes comprar la casilla " + this.nombre + " por el módico precio de " + this.valor);
                actual.sumarGastos(this.valor);
                banca.sumarFortuna(this.valor);
                this.duenho = actual;
                return true;
            } else if (this.duenho != banca && !this.duenho.estaHipotecado() && !actual.estaEnBancarrota() && actual.getFortuna() >= this.impuesto) {
                System.out.println(actual.getNombre() + " debe pagarle el alquiler a " + this.duenho.getNombre());
                actual.sumarGastos(this.impuesto);
                this.duenho.sumarFortuna(this.impuesto);
                return true;
            } else {
                return false;
            }

        case "Especial":
            if (this.nombre.equals("Carcel")) {
                System.out.println("Tienes 3 opciones para salir: Pagar, Usar Carta de Suerte o Sacar Dados Dobles.\n");
                opcion = scan.nextLine();
                switch (opcion) {
                    case "Pagar":
                        if (actual.getFortuna() >= Valor.DINERO_SALIR_CARCEL) {
                            actual.sumarGastos(Valor.DINERO_SALIR_CARCEL);
                            banca.sumarFortuna(Valor.DINERO_SALIR_CARCEL);
                            return true;
                        } else {
                            System.out.println("No tiene suficiente dinero para realizar esta acción.\n");
                            return false;
                        }

                    case "Usar Carta de Suerte":
                        // Implementar lógica para usar carta de suerte
                        return true;

                    case "Sacar Dados Dobles":
                        // Implementar lógica para sacar dados dobles
                        return true;

                    default:
                        System.out.println("Opción inválida.");
                        return false;
                }

            } else if (this.nombre.equals("Parking")) {
                System.out.println("Enhorabuena, has ganado el bote almacenado por impuestos, tasas y multas.\n");
                actual.sumarFortuna(this.valor);
                this.valor = 0;
                return true;

            } else if (this.nombre.equals("IrCarcel")) {
                System.out.println("Mala suerte, te vas a la cárcel.\n");
                return true;

            } else if (this.nombre.equals("Salida")) {
                System.out.println("Has pasado por la salida. Has cobrado " + Valor.SUMA_VUELTA);
                actual.sumarFortuna(Valor.SUMA_VUELTA);
                return true;
            }

            break;

        case "Transporte":
            float multiplicador = 0; // Inicialización de multiplicador
            if (this.duenho == banca && actual.getFortuna() >= this.valor) {
                System.out.println("Puedes comprar la casilla " + this.nombre + " por el módico precio de " + this.valor);
                actual.sumarGastos(this.valor);
                banca.sumarFortuna(this.valor);
                this.duenho = actual;
                return true;

            } else if (this.duenho != banca && !this.duenho.estaHipotecado() && !actual.estaEnBancarrota()) {
               /* 
                if (this.duenho.esDuenhoCasilla(actual,encontrar_casilla("Trans1"))) multiplicador += 0.25;
                if (this.duenho.esDuenhoCasilla(actual,encontrar_casilla("Trans2"))) multiplicador += 0.25;
                if (this.duenho.esDuenhoCasilla(actual,encontrar_casilla("Trans3"))) multiplicador += 0.25;
                if (this.duenho.esDuenhoCasilla(actual, encontrar_casilla("Trans4"))) multiplicador += 0.25;
                */ //ni puta idea d por q no funciona
                float total = multiplicador * this.valor;
                actual.sumarGastos(total);
                this.duenho.sumarFortuna(total);

                System.out.println(actual.getNombre() + " debe pagarle el servicio de transporte a " + this.duenho.getNombre());
                return true;
            }

            break;

        case "Impuestos":
            System.out.println("Vaya! Debes pagar tus impuestos a la banca.\n");
            if (!actual.estaEnBancarrota() && actual.getFortuna() >= this.valor) {
                actual.sumarGastos(this.valor);
                banca.sumarFortuna(this.valor);
                return true;
            } else {
                return false;
            }

        case "Servicios":
            if (this.duenho == banca && actual.getFortuna() >= this.valor) {
                System.out.println("Puedes comprar la casilla " + this.nombre + " por el módico precio de " + this.valor);
                actual.sumarGastos(this.valor);
                banca.sumarFortuna(this.valor);
                this.duenho = actual;
                return true;

            } else if (this.duenho != banca && !this.duenho.estaHipotecado() && !actual.estaEnBancarrota() && actual.getFortuna() >= (4 * tirada * this.valor)) {
                System.out.println(actual.getNombre() + " debe pagarle el servicio a " + this.duenho.getNombre());
                actual.sumarGastos(4 * tirada * this.valor);
                this.duenho.sumarFortuna(4 * tirada * this.valor);
                return true;
            }

            break;

        default:
            System.out.println("Error en evaluarCasilla.\n");
            return false;
    }

    return false;
}



    /** Método usado para comprar una casilla determinada.
     * @param solicitante Jugador que solicita la compra de la casilla
     * @param banca La banca es el dueño de las casillas no compradas aún
     */
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if(solicitante.getFortuna()>=this.valor && this.duenho==banca){
            solicitante.sumarGastos(this.valor);
            banca.sumarFortuna(this.valor);
            banca.eliminarPropiedad(this);
            solicitante.anhadirPropiedad(this);
            this.duenho=solicitante;

            System.out.println(solicitante.getNombre()+" ha comprado la propiedad "+this.getNombre()+" por el precio de "+this.valor);
        
        }
        else if(this.duenho!=banca){
            System.out.println("Esta casilla no está en venta");
    
        }

        else{
            System.out.println("No tienes dinero para comprar esta casilla");
        }
 
    }

    /**Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.
     */
    public String infoCasilla() {
        String info= new String();
        info= "Nombre casilla:"+this.nombre+"\n"
                + "Tipo Casilla: " +this.tipo + "\n"
                + "Posicion: "+this.posicion+"\n"
                + "Valor: "+this.valor+"\n"
                + "Dueño: "+(this.duenho != null ? this.duenho.getNombre() : "Casilla sin Dueño" )+"\n"
                + "Color del grupo"+this.grupo.getColorGrupo()
                + "Valor hipoteca:"+this.hipoteca+"\n";

        return info;
    }

    /** Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casaEnVenta() {
        if(this.duenho==null || this.duenho.getNombre()=="Banca"){
            return "La casilla" + this.nombre + "está en venta por un precio de " + this.valor +"\n";
        }
        return "Esta casilla no está en venta";
    }

    public boolean esDuenhoCasilla(Jugador jugador, Casilla casilla){
        if (casilla.duenho==jugador) return true;
        else return false;
    }

    //SECCIÓN DE GETTERS Y SETTERS

    public String getNombre(){
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public float getValor() {
        return valor;
    }

    public int getPosicion(){
        return posicion;
    }

    public Jugador getDuenho(){
        return duenho;
    }

    public Grupo getGrupo(){
        return grupo;
    }

    public float getImpuesto(){
        return impuesto;
    }

    public float getHipoteca(){
        return hipoteca;
    }

    public ArrayList<Avatar> getAvatares(){
        return avatares;
    }

    //No hace falta setter de avatares
    public void setNombre(String nombre_casilla) {
        switch(nombre_casilla) {
            case "Salida": case "Carcel": case "Parking": case "IrCarcel":
            case "Imp 1": case "Imp 2": case "Serv 1": case "Serv 2":
            case "Trans 1": case "Trans 2": case "Trans 3": case "Trans 4":
            case "Caja": case "Suerte": case "Solar 1": case "Solar 2":
            case "Solar 3": case "Solar 4": case "Solar 5": case "Solar 6":
            case "Solar 7": case "Solar 8": case "Solar 9": case "Solar 10":
            case "Solar 11": case "Solar 12": case "Solar 13": case "Solar 14":
            case "Solar 15": case "Solar 16": case "Solar 17": case "Solar 18":
            case "Solar 19": case "Solar 20": case "Solar 21": case "Solar 22":
                this.nombre=nombre_casilla;
                break;
            default:
                System.out.println(nombre_casilla + " no es un nombre de casilla válido.\n");
        }
    }

    public void setTipo(String tipo_casilla) {
        switch(tipo_casilla) {
            case "Especial": case "Impuesto": case "Servicios": case "Transporte":
            case "Caja de comunidad": case "Suerte": case "Solar":
                this.tipo=tipo_casilla;
                break;
            default:
                System.out.println(tipo_casilla + " no es un tipo de casilla válido.\n");
        }
    }

    public void setValor(float valor_casilla) {
        if (valor_casilla > 0) {
            this.valor = valor_casilla;
        }
        else {
            System.out.println("El valor de la casilla debe ser positivo.\n");
        }
    }

    public void setPosicion(int posicion_casilla) {
        if(posicion_casilla<40 && posicion_casilla>-1) {
            this.posicion = posicion_casilla;
        }
        else {
            System.out.println(posicion + " no es una casilla válida.\n");
        }
    }

    public void setDuenho(Jugador duenho_casilla) {
        if (duenho_casilla != null) {
            for (Avatar avatar : this.avatares) {
                if (avatar.getJugador().getNombre().equals(duenho_casilla.getNombre()))  { // Si el jugador es el dueño de algún avatar en la casilla
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

    public void setGrupo(Grupo grupo_casilla) {
        if (grupo_casilla != null) {
            this.grupo = grupo_casilla;
        }
        else {
            System.out.println("El grupo no puede ser nulo.\n");
        }
    }

    public void setImpuesto(float impuesto_casilla) {
        if (impuesto_casilla > 0) {
            this.impuesto = impuesto_casilla;
        }
        else {
            System.out.println("El impuesto debe ser un valor positivo.\n");
        }
    }

    public void setHipoteca(float hipoteca_casilla) {
        if (hipoteca_casilla > 0) {
            this.hipoteca = hipoteca_casilla;
        }
        else {
            System.out.println("La hipoteca debe ser un valor positivo.\n");
        }
    }
    public boolean estaHipotecada() {
        return true;
    }

    public boolean posiblecompra(Jugador comprador, Jugador banca) {
        boolean esTipoComprable = "solar".equals(tipo) || "transporte".equals(tipo) || "servicio".equals(tipo);
        boolean esDueñoBanca = (duenho == banca);
        boolean tieneSuficienteFortuna = valor <= comprador.getFortuna();

        return esTipoComprable && esDueñoBanca && tieneSuficienteFortuna;
    }


}