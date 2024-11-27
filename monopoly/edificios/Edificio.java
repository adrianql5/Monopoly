package monopoly.edificios;

import monopoly.Valor;

import partida.Jugador;
import monopoly.Grupo;
import monopoly.casillas.Casilla;
import java.util.Map;

public abstract class Edificio {
    private String id;
    private String tipo;
    private Jugador duenho;
    private Casilla casilla;
    private float coste;
    private Grupo grupo;

    public Edificio(Casilla lugar) {
        this.id = lugar.generarID(tipo);
        this.casilla = lugar;
        this.duenho = casilla.getDuenho();
        this.grupo = casilla.getGrupo();
        asignarValores();
    }

    public void asignarValores() {
        String grupo = this.casilla.getGrupo().getColorGrupo();
        int n = this.casilla.getGrupo().getNumCasillasGrupo();

        Map<String, Float> grupoValores = Map.of(
            "WHITE", Valor.GRUPO1 / n,
            "CYAN", Valor.GRUPO2 / n,
            "BLUE", Valor.GRUPO3 / n,
            "YELLOW", Valor.GRUPO4 / n,
            "BLACK", Valor.GRUPO5 / n,
            "GREEN", Valor.GRUPO6 / n,
            "RED", Valor.GRUPO7 / n,
            "PURPLE", Valor.GRUPO8 / n
        );

        Float valorInicialSolar = grupoValores.get(grupo);
        if (valorInicialSolar != null) {
            this.coste = calcularCoste(valorInicialSolar);
        } else {
            this.coste = 0;
        }
    }

    // Método abstracto que cada subclase implementará de forma distinta
    protected abstract float calcularCoste(float valorGrupo);

    public String infoEdificio() {
        return "{\n" +
                "\tid: " + this.id + ",\n" +
                "\tpropietario: " + (this.duenho != null ? this.duenho.getNombre() : "N/A") + ",\n" +
                "\tcasilla: " + this.casilla.getNombre() + ",\n" +
                "\tgrupo: " + this.grupo.getColorGrupo() + ",\n" +
                "\tcoste: " + this.coste + "\n" +
                "},\n";
    }

    // Getters y Setters
    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    public Jugador getDuenho() { return this.duenho; }
    public void setDuenho(Jugador duenho) { this.duenho = duenho; }
    public Casilla getCasilla() { return this.casilla; }
    public void setCasilla(Casilla casilla) { this.casilla = casilla; }
    public Grupo getGrupo() { return this.grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public float getCoste() { return this.coste; }
    public void setCoste(float coste) { this.coste = coste; }
    public String getTipo() { return this.tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
