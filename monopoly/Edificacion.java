package monopoly;

import partida.Jugador;


import java.util.ArrayList;


public class Edificacion{
    private String id;
    private String tipo;
    private Jugador duenho;
    private Casilla casilla;
    private float coste;
    private Grupo grupo;
    private ArrayList<Edificacion> edificaciones;


    public Edificacion(String id, String tipo, Jugador duenho, Casilla lugar, Grupo grupo, float coste){
        this.id=generarID(id);
        this.tipo=tipo;
        this.duenho=duenho;
        this.casilla=lugar;
        this.grupo=grupo;
        this.coste=coste;
        edificaciones.add(this);
        
    }


    public boolean esTipoComprable(String tipo){
        if(tipo.equals("Casa") || tipo.equals("Hotel") 
        || tipo.equals("Piscina") || tipo.equals("Pistas de Deporte")){
            return true;
        }
        return false;
    }

    public String generarID(String tipo){
        int contador=0;

        for(Edificacion e : edificaciones){
            if(e.getTipo().equals(tipo)){
                contador++;
            }
        }
        return tipo +"-"+(contador+1);
    }

    public void anhadirEdificacion(Edificacion ed){
        this.edificaciones.add(ed);
    }

    public void eliminarEdificacion(Edificacion ed){
        this.edificaciones.remove(ed);
    }
    

    public String listarEdificaciones(){
        String cadena = new String();

        for(Edificacion e : edificaciones){
            cadena = ("{\n");
            cadena += ("\tid: " + e.id + "," + "\n");
            cadena += ("\tpropietario: " + e.duenho.getNombre() + "," + "\n");
            cadena += ("\tcasilla: " + e.casilla.getNombre() + "," + "\n");
            cadena += ("\tgrupo: " + e.grupo.getColorGrupo() + "," + "\n");
            cadena += ("\tcoste: " + e.coste + "\n");
            cadena += ("},\n");
        }
        return cadena;

    }



    
    //Sección de Getters y Setters
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Jugador getDuenho() {
        return this.duenho;
    }
    
    public void setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }

    
    public Casilla getCasilla() {
        return this.casilla;
    }
    
    public void setCasilla(Casilla casilla) {
        this.casilla=casilla;
    }
    
    
    public Grupo getGrupo() {
        return this.grupo;
    }
    
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
    
    
    public float getCoste() {
        return this.coste;
    }
    
    public void setCoste(float coste) {
        this.coste = coste;
    }
    
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        if (esTipoComprable(tipo)) {
            this.tipo = tipo;
        }
    }


    public ArrayList<Edificacion> getEdificaciones() {
        return this.edificaciones;
    }

    public void setEdificios(ArrayList<Edificacion> edificaciones) {
        this.edificaciones = edificaciones;
    }



}
