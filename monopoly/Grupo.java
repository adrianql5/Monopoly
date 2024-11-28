package monopoly;

import java.util.ArrayList;
import monopoly.casillas.propiedades.Solar;
import partida.Jugador;

public class Grupo {

    // Atributos
    private ArrayList<Solar> miembros; // Propiedades miembros del grupo
    private String colorGrupo; // Color del grupo
    private int numCasillas; // Número de casillas del grupo
    private float recaudacionGrupo; // Recaudación total del grupo

    // =======================
    // SECCIÓN: CONSTRUCTORES
    // =======================

    /**
     * Constructor para un grupo con dos propiedades.
     * 
     * @param prop1      Solar miembro 1
     * @param prop2      Solar miembro 2
     * @param colorGrupo Color del grupo
     */
    public Grupo(Solar prop1, Solar prop2, String colorGrupo) {
        this.miembros = new ArrayList<>();
        this.colorGrupo = colorGrupo;
        this.miembros.add(prop1);
        this.miembros.add(prop2);
        this.numCasillas = 2;
    }

    /**
     * Constructor para un grupo con tres propiedades.
     * 
     * @param prop1      Solar miembro 1
     * @param prop2      Solar miembro 2
     * @param prop3      Solar miembro 3
     * @param colorGrupo Color del grupo
     */
    public Grupo(Solar prop1, Solar prop2, Solar prop3, String colorGrupo) {
        this.miembros = new ArrayList<>();
        this.colorGrupo = colorGrupo;
        this.miembros.add(prop1);
        this.miembros.add(prop2);
        this.miembros.add(prop3);
        this.numCasillas = 3;
    }

    // ====================
    // SECCIÓN: MÉTODOS
    // ====================

    /**
     * Verifica si alguna propiedad del grupo está hipotecada.
     * 
     * @return {@code true} si alguna solar está hipotecada; {@code false} en caso
     *         contrario.
     */
    public boolean estaHipotecadoGrupo() {
        for (Solar solar : this.miembros) {
            if (solar.estaHipotecada()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Añade una propiedad al grupo.
     * 
     * @param solar Solar que se quiere añadir al grupo.
     */
    public void anhadirPropiedad(Solar solar) {
        this.miembros.add(solar);
        this.numCasillas++;
    }

    /**
     * Comprueba si el jugador posee todas las propiedades del grupo.
     * 
     * @param jugador Jugador que se quiere evaluar.
     * @return {@code true} si el jugador es dueño de todas las propiedades;
     *         {@code false} en caso contrario.
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        for (Solar solar : miembros) {
            if (solar.getDuenho() != jugador) {
                return false;
            }
        }
        return true;
    }

    // ==========================
    // SECCIÓN: GETTERS/SETTERS
    // ==========================

    public ArrayList<Solar> getMiembrosGrupo() {
        return this.miembros;
    }

    public void setMiembrosGrupo(ArrayList<Solar> miembros) {
        this.miembros = miembros;
    }

    public String getColorGrupo() {
        return this.colorGrupo;
    }

    public void setColorGrupo(String colorGrupo) {
        this.colorGrupo = colorGrupo;
    }

    public int getNumCasillasGrupo() {
        return this.numCasillas;
    }

    public void setNumCasillasGrupo(int numCasillas) {
        this.numCasillas = numCasillas;
    }

    public float getRecaudacionGrupo() {
        return this.recaudacionGrupo;
    }

    /**
     * Suma un monto a la recaudación total del grupo.
     * 
     * @param recaudacionGrupo Cantidad a sumar.
     */
    public void sumarRecaudacionGrupo(float recaudacionGrupo) {
        this.recaudacionGrupo += recaudacionGrupo;
    }
}
