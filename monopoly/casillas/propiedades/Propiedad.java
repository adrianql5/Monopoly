package monopoly.casillas.propiedades;

import monopoly.Valor;
import monopoly.casillas.Casilla;
import partida.Jugador;

import java.util.*;
import monopoly.edificios.*;


public  abstract class Propiedad extends Casilla {
    // Atributos
    protected boolean estaHipotecada;
    protected float valor;
    protected float hipoteca;
    protected float deshipoteca;
    protected float alquiler;
    protected float valorDeshipoteca;
    
    protected Jugador duenho;
    
    // ==========================
    // SECCIÓN: CONSTRUCTORES
    // ==========================
    /**
     * Constructor para casillas tipo Solar, Servicio y Transporte.
     * 
     * @param nombre   Nombre de la casilla.
     * @param posicion Posición en el tablero.
     */
    public Propiedad(String nombre, int posicion) {
        super(nombre, posicion); // Llama al constructor de la superclase Casilla
        this.duenho = new Jugador();
        this.valor = calcularValor();
        
        this.hipoteca = valor * Valor.FACTOR_HIPOTECA;
        this.deshipoteca = valor * Valor.FACTOR_DESHIPOTECA;
        this.estaHipotecada = false;
        
        this.dineroRecaudado = 0;
        
        this.alquiler= calcularAlquiler();
    }
    
    
    // ===========================
    // SECCIÓN: MÉTODOS ABSTRACTOS
    // ===========================
    
    //el tio la llama alquiler, es abstracto porque cada subclase lo calcula de forma difetente
    public abstract float calcularValor();

    public abstract float calcularAlquiler();
    


    // ==========================
    // SECCIÓN: GETTERS Y SETTERS
    // ==========================

    public float getDeshipoteca(){
        return this.deshipoteca;
    }

    /**Método para añadir valor a una casilla. Utilidad:
     * (1) Sumar valor a la casilla de parking.
     * (2) Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
     * @param suma Cantidad a añadir al valor de la casilla
     */

    public void sumarValor(float suma) {
        this.valor +=suma;
    }

    public Jugador getDuenho() {
        return duenho;
    }

    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }

    public float getHipoteca() {
        return hipoteca;
    }

    public void setHipoteca(float hipotecaCasilla) {
        if (hipotecaCasilla > 0) {
            this.hipoteca = hipotecaCasilla;
        } else {
            System.out.println("La hipoteca debe ser un valor positivo.");
        }
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valorCasilla) {
        if (valorCasilla > 0) {
            this.valor = valorCasilla;
        } else {
            System.out.println("El valor de la casilla debe ser positivo.");
        }
    }

    public boolean estaHipotecada() {
        return estaHipotecada;
    }

    public void setDeshipotecada() {
        this.estaHipotecada = false;
    }

    //El tio la llama  boolean perteneceAjugador(Jugador jugador)
    public boolean esDuenho(Jugador jugador) {
        return this.duenho.equals(jugador);
    }

    // ============================
    // SECCIÓN: MÉTODOS PRINCIPALES
    // ============================
    /**
     * Método usado para comprar una casilla determinada.
     * 
     * @param solicitante Jugador que solicita la compra de la casilla.
     * @param banca       La banca es el dueño de las casillas no compradas aún.
     */
    //otra de las funciones que pide el pavo
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (solicitante.getAvatar().getLugar().equals(this)) {
            if(this.duenho.getNombre().equals("banca")) {
                if (solicitante.getFortuna() >= this.valor) {
                    // Realiza la compra
                    solicitante.restarFortuna(this.valor);
                    solicitante.sumarGastos(this.valor);
                    banca.eliminarPropiedad(this);
                    solicitante.anhadirPropiedad(this);
                    this.setDuenho(solicitante);

                    System.out.printf("%s ha comprado la propiedad '%s' por el precio de %,.0f€.%n",
                            solicitante.getNombre(), this.getNombre(), this.valor);
                } else {
                    System.out.println("No tienes suficiente dinero para comprar esta casilla.");
                }
            } else {
                System.out.println("Esta casilla no está en venta.");
            }
        } else {
            System.out.println("¡Debes estar en esta casilla para poder comprarla!");
        }
    }

    /**
     * Determina si la casilla puede ser hipotecada.
     * 
     * @return true si es hipotecable, false en caso contrario.
     */

    public boolean esHipotecable() {
        if (!estaHipotecada) { // Verifica si la propiedad no está hipotecada
            boolean sinEdificios = true; // Inicializa como que no hay edificaciones
            
            // Verifica si la propiedad es una instancia de Solar
            if (this instanceof Solar) {
                Solar solar = (Solar) this;  // Hacemos un cast a Solar para acceder a los atributos específicos de Solar
                for (ArrayList<Edificio> tipoEdificio : solar.getEdificios()) {  // Accede a la lista de edificios
                    if (!tipoEdificio.isEmpty()) {  // Si alguna lista de edificios no está vacía
                        sinEdificios = false;  // Marca que no está vacío, por lo tanto no puede hipotecarse
                        break;
                    }
                }
            }
            
            // Si hay edificaciones, no se puede hipotecar
            if (!sinEdificios) {
                System.out.println("No puedes hipotecar la casilla " + this.getNombre() + " porque tienes que vender todas tus edificaciones.");
                return false;  // Retorna false indicando que no puede hipotecarse
            } else {
                estaHipotecada = true;  // Marca como hipotecada
                return true;  // Retorna true indicando que sí se puede hipotecar
            }
        } else {
            System.out.println("No puedes hipotecar esta propiedad porque ya está hipotecada.");
            return false;  // Retorna false si ya está hipotecada
        }
    }
    


    /**
     * Determina si la casilla puede ser deshipotecada.
     * 
     * @return true si es deshipotecable, false en caso contrario.
     */
    public boolean esDesHipotecable() {
        if (estaHipotecada) {
            this.estaHipotecada = false;
            return true;
        } else {
            System.out.println("No puedes deshipotecar esta propiedad porque no está hipotecada.");
            return false;
        }
    }
}


