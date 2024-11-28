package monopoly.edificios;

import monopoly.Valor;
import monopoly.casillas.propiedades.Solar;

import java.util.Map;

/**
 * Clase abstracta que representa un edificio en el tablero de Monopoly.
 */
public abstract class Edificio {

    // =========================================
    // ATRIBUTOS
    // =========================================
    protected String id; // Identificador único del edificio
    protected Solar solar; // Solar asociado al edificio
    protected float coste; // Coste del edificio
    protected float valorVenta;
    // =========================================
    // CONSTRUCTOR
    // =========================================
    /**
     * Constructor para inicializar un edificio en un solar específico.
     *
     * @param solar Solar donde se construirá el edificio.
     */
    public Edificio(Solar solar) {
        this.solar = solar;
        this.id = generarID();
        asignarValores();
        this.valorVenta=this.coste * 0.5f;
    }
    
    //hacerlo abstracto
    // Genera un ID único basado en el tipo y los IDs ya presentes en el subarray de ese tipo
    protected abstract String generarID();


    // =========================================
    // MÉTODOS PRINCIPALES
    // =========================================
    /**
     * Asigna los valores iniciales al edificio según el grupo al que pertenece el solar.
     */
    public void asignarValores() {
        String grupo = this.solar.getGrupo().getColorGrupo();
        int numCasillasGrupo = this.solar.getGrupo().getNumCasillasGrupo();

        Map<String, Float> grupoValores = Map.of(
            "WHITE", Valor.GRUPO1 / numCasillasGrupo,
            "CYAN", Valor.GRUPO2 / numCasillasGrupo,
            "BLUE", Valor.GRUPO3 / numCasillasGrupo,
            "YELLOW", Valor.GRUPO4 / numCasillasGrupo,
            "BLACK", Valor.GRUPO5 / numCasillasGrupo,
            "GREEN", Valor.GRUPO6 / numCasillasGrupo,
            "RED", Valor.GRUPO7 / numCasillasGrupo,
            "PURPLE", Valor.GRUPO8 / numCasillasGrupo
        );

        Float valorInicialSolar = grupoValores.get(grupo);
        this.coste = (valorInicialSolar != null) ? calcularCoste(valorInicialSolar) : 0;
    }

    // =========================================
    // MÉTODOS ABSTRACTOS
    // =========================================
    protected abstract float calcularCoste(float valorGrupo);
        
        /**
     * Devuelve la información detallada del edificio en formato JSON.
     *
     * @return Cadena con la información del edificio.
     */
    public String infoEdificio() {
        return "{\n" +
                "\tid: \"" + this.id + "\",\n" +
                "\tpropietario: \"" + (this.solar.getDuenho() != null ? this.solar.getDuenho().getNombre() : "N/A") + "\",\n" +
                "\tsolar: \"" + this.solar.getNombre() + "\",\n" +
                "\tgrupo: \"" + this.solar.getGrupo().getColorGrupo() + "\",\n" +
                "\tcoste: " + this.coste + "\n" +
                "}";
    }

    // =========================================
    // GETTERS Y SETTERS
    // =========================================
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Solar getSolar() {
        return this.solar;
    }

    public void setSolar(Solar solar) {
        this.solar = solar;
    }

    public float getCoste() {
        return this.coste;
    }

    public void setCoste(float coste) {
        this.coste = coste;
    }
}

