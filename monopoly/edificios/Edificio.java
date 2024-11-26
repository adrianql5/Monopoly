package monopoly.edificios;

import partida.Jugador;

import java.util.Map;

import monopoly.Grupo;
import monopoly.Valor;
import monopoly.casillas.Casilla;


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
        this.duenho = casilla.getDuenho();
        this.grupo = casilla.getGrupo();

        asignarValores();
    }


    public void asignarValores() {
        String grupo = this.casilla.getGrupo().getColorGrupo();
        int n=this.casilla.getGrupo().getNumCasillasGrupo();
        
        Map<String, Float> grupoValores = Map.of(
            "WHITE", Valor.GRUPO1/n,
            "CYAN", Valor.GRUPO2/n,
            "BLUE", Valor.GRUPO3/n,
            "YELLOW", Valor.GRUPO4/n,
            "BLACK", Valor.GRUPO5/n,
            "GREEN", Valor.GRUPO6/n,
            "RED", Valor.GRUPO7/n,
            "PURPLE", Valor.GRUPO8/n
        );
        
        Float valorInicialSolar = grupoValores.get(grupo);

        if (valorInicialSolar != null) {
            this.coste = calcularCoste(valorInicialSolar, tipo);
        } else {
            this.coste = 0; 
        }
    }

    private float calcularCoste(float valorGrupo, String tipo) {
        return switch (tipo) {
            case "casa", "hotel" -> valorGrupo * 0.60f;
            case "pista de deporte" -> valorGrupo * 1.25f;
            case "piscina" -> valorGrupo * 0.40f;
            default -> 0;
        };
    }


    public boolean esTipoComprable(String tipo) {
        return tipo.equals("casa") || tipo.equals("hotel") || tipo.equals("piscina") || tipo.equals("pista de deporte");
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
