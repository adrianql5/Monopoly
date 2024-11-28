package monopoly.casillas.propiedades;

import static java.lang.Math.*;

import java.util.*;

import monopoly.*;
import partida.*;
import monopoly.edificios.*;
import monopoly.edificios.*;

public class Solar extends Propiedad{
    private ArrayList<ArrayList<Edificio>> edificios;

    protected Grupo grupo;
    
    public Solar(String nombre,int posicion){
        super(nombre,posicion);
        this.grupo=null;
        this.edificios = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            this.edificios.add(new ArrayList<Edificio>()); // Array de casas, hoteles, piscinas, pistas
        }
    }
    

    public float calcularValor(){
        switch (posicion) {
            case 1 ,3:
                return Valor.GRUPO1/2;
            case 6,8,9:
                return Valor.GRUPO2/3;
            case 11,13,14:
                return Valor.GRUPO3/3;
            case 16, 18, 19:
                return Valor.GRUPO4/3;
            case 21 ,23,24:
                return Valor.GRUPO5/3;
            case 26, 27,29:
                return Valor.GRUPO6/3;
            case 31, 32, 34:
                return Valor.GRUPO7/3;
            case 37,39:
                return Valor.GRUPO8/2;
            default:
                return 0.0f;
        }
    }

    public float calcularAlquiler(){
        return valor * Valor.FACTOR_ALQUILER_SOLAR;
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


    // Añadir una edificación del tipo especificado
    public void anhadirEdificio(Edificio ed) {
        
        if(ed instanceof Casa){
            this.edificios.get(0).add(ed);
        }

        else if (ed instanceof Piscina){
            this.edificios.get(2).add(ed);
        }

        else if (ed instanceof Hotel){
            this.edificios.get(1).add(ed);
        }
        else if(ed instanceof PistaDeporte){
            this.edificios.get(4).add(ed);
        }
    }


    /**Método para evaluar lo que hay que pagar en una casilla de tipo Transporte o Solar.*/
    public float evaluarAlquiler() {
        // Precio base del alquiler de cualquier casilla
        float totalAlquiler = alquiler;
        // Si es dueño del grupo se duplica el precio base del alquiler del solar
        if (this.grupo.esDuenhoGrupo(this.duenho)){
            totalAlquiler *= 2f;
        }
        // Sumamos al total el precio de las casas si las hubiera
        // En el ArrayList de edificios la posición 0 es el ArrayList de las casas :)
        switch(this.edificios.get(0).size()) {
            case 0: break;
            case 1: totalAlquiler += alquiler * 5f; break;
            case 2: totalAlquiler += alquiler * 15f; break;
            case 3: totalAlquiler += alquiler * 35f; break;
            case 4: totalAlquiler += alquiler * 50f; break;
            default:
                System.out.println("Cómo pudiste edificar más de 4 casas en un Solar meu...");
        }

        // Sumamos al total el precio de los hoteles, piscinas y pistas de deporte
        // (posiciones 1,2,3 en el ArrayList de edificios respectivamente)
        totalAlquiler += alquiler * 70f * this.edificios.get(1).size();
        totalAlquiler += alquiler * 25f * ( this.edificios.get(2).size() + this.edificios.get(3).size() );

        return totalAlquiler;
    }


    public int getNumeroEdificios() {
        int totalEdificios = 0;
        if(this.edificios==null){
            return 0;
        }
        for (ArrayList<Edificio> tipoEdificio : this.edificios) {
            totalEdificios += tipoEdificio.size();
        }
        return totalEdificios;
    }


    public void eliminarCasasDeCasilla() {
        this.getCasas().clear();
    }


    public void eliminarTodosLosEds() {
        for (ArrayList<Edificio> lista : this.edificios) {
            lista.clear();
        }
    }

    public int contarCasasSolar(){
        return getCasas().size();
    }

    public int contarHotelesSolar(){
        return getHoteles().size();
    }

    public int contarPiscinasSolar(){
        return getPiscinas().size();
    }

    public int contarPistasSolar(){
        return getPistasDeDeporte().size();
    }

    public ArrayList<Edificio> getCasas() {
        return this.edificios.get(0); // Retorna la lista de casas
    }

    public ArrayList<Edificio> getHoteles() {
        return this.edificios.get(1); // Retorna la lista de hoteles
    }

    public ArrayList<Edificio> getPiscinas() {
        return this.edificios.get(2); // Retorna la lista de piscinas
    }

    public ArrayList<Edificio> getPistasDeDeporte() {
        return this.edificios.get(3); // Retorna la lista de pistas de deporte
    }

    //hacer bucle O(n²)
    public String listarEdificaciones() {
        String str = ""; // Inicializar la cadena vacía

        // Recorrer cada tipo de edificación y listarlas
        String[] tipos = {"casa", "hotel", "piscina", "pista de deporte"};
        for (String tipo : tipos) {
            ArrayList<Edificio> edificios = getEdificiosPorTipo(tipo);
            if (!edificios.isEmpty()) {
                for (Edificio edificio : edificios) {
                    str += edificio.infoEdificio() + "\n"; // Agregar la información del edificio
                }
            }
        }

        return str; // Retornar la cadena resultante
    }

}
