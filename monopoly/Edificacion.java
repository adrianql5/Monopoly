package monopoly;

import partida.Jugador;


import java.util.ArrayList;


public class Edificacion{
    private String id;
    private String tipo;
    private Jugador duenho;
    private Casilla lugar;
    private float valor;
    private Grupo grupo;
    private ArrayList<Edificacion> edificaciones;


    public Edificacion(String id, String tipo, Jugador duenho, Casilla lugar, Grupo grupo){
        this.id=id;
        this.tipo=tipo;
        this.duenho=duenho;
        this.lugar=lugar;
        this.grupo=grupo;
        
    }

    public String getTipo(){
        return tipo;
    }

    public boolean esTipoComprable(String tipo){
        if(tipo.equals("Casa") || tipo.equals("Hotel") 
        || tipo.equals("Piscina") || tipo.equals("Pistas de Deporte")){
            return true;
        }
        return false;
    }






    
}
