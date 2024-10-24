package monopoly;

import java.util.ArrayList;

import partida.Jugador;



public class Edificio {
    private String id;
    private String tipo;
    private Jugador duenho;
    private Casilla casilla;
    private float coste;
    private Grupo grupo;
    

    public Edificio(String tipo, Casilla lugar) {
        this.id = lugar.generarID(tipo); 
        this.tipo = tipo;
        this.casilla = lugar;
        this.duenho=casilla.getDuenho();
        this.grupo = casilla.getGrupo();
        
     

        if(this.tipo.equals("Casa") || this.tipo.equals("Hotel"))
            this.coste = casilla.getValor() * 0.60f;


        
        if(this.tipo.equals("Piscina"))
            this.coste = casilla.getValor() * 0.40f;
            
        if(this.tipo.equals("Pista de Deportes"))
            this.coste = casilla.getValor() * 1.25f;

    }

    public boolean esTipoComprable(String tipo) {
        return tipo.equals("Casa") || tipo.equals("Hotel") || tipo.equals("Piscina") || tipo.equals("Pistas de Deportes");
    }

    

    // Lista todas las edificaciones en su respectivo array
    public String infoEdificio() {
        String cadena = "";

        
        cadena += "{\n";
        cadena += "\tid: " + this.id + ",\n";
        cadena += "\tpropietario: " + this.duenho.getNombre() + ",\n";
        cadena += "\tcasilla: " + this.casilla.getNombre() + ",\n";
        cadena += "\tgrupo: " + this.grupo.getColorGrupo() + ",\n";
        cadena += "\tcoste: " + this.coste + "\n";
        cadena += "},\n";
            
        
        return cadena;
    }



    // Getters y Setters
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
        this.casilla = casilla;
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

}
