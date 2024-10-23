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
    private ArrayList<ArrayList<Edificio>> edificios; // Lista de listas de edificaciones

    public Edificio(String tipo, Casilla lugar) {
        this.id = generarID(tipo); 
        this.tipo = tipo;
        this.casilla = lugar;
        this.duenho=casilla.getDuenho();
        this.grupo = casilla.getGrupo();
        
        this.edificios = new ArrayList<>(); 
        for (int i = 0; i < 4; i++) {
            this.edificios.add(new ArrayList<>());
        }


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

    

    // Genera un ID basado en el tipo y el número de edificaciones en ese tipo
    public String generarID(String tipo) {
        int tipoIndex = getTipoIndex(tipo);
        int contador = edificios.get(tipoIndex).size(); 
        return tipo + "-" + (contador + 1);
    }

    // Lista todas las edificaciones en su respectivo array
    public String listarEdificaciones() {
        String cadena = "";

        for (ArrayList<Edificio> tipoEdificaciones : edificios) {
            for (Edificio e : tipoEdificaciones) {
                cadena += "{\n";
                cadena += "\tid: " + e.id + ",\n";
                cadena += "\tpropietario: " + e.duenho.getNombre() + ",\n";
                cadena += "\tcasilla: " + e.casilla.getNombre() + ",\n";
                cadena += "\tgrupo: " + e.grupo.getColorGrupo() + ",\n";
                cadena += "\tcoste: " + e.coste + "\n";
                cadena += "},\n";
            }
        }
        return cadena;
    }

    // Devuelve el índice correspondiente al tipo de edificación
    private int getTipoIndex(String tipo) {
        switch (tipo) {
            case "Casa":
                return 0;
            case "Hotel":
                return 1;
            case "Piscina":
                return 2;
            case "Pistas de Deportes":
                return 3;
            default:
                System.out.println("Error: Tipo de edificación no válido");
                return -1;
        }
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

    public ArrayList<ArrayList<Edificio>> getEdificaciones() {
        return this.edificios;
    }

    public void setEdificaciones(ArrayList<ArrayList<Edificio>> edificaciones) {
        this.edificios = edificaciones;
    }
}
