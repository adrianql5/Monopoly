package monopoly;

import java.util.ArrayList;
import monopoly.casillas.propiedades.Propiedad;
import partida.Jugador;

public class Grupo {

    // Atributos
    private ArrayList<Propiedad> miembros; // Propiedades miembros del grupo
    private String colorGrupo; // Color del grupo
    private int numCasillas; // Número de casillas del grupo
    private float recaudacionGrupo; // Lo que recauda el grupo

    // SECCIÓN DE CONSTRUCTORES DE GRUPOS

    /** Constructor para cuando el grupo está formado por dos propiedades.
     * @param prop1 Propiedad miembro 1
     * @param prop2 Propiedad miembro 2
     * @param colorGrupo Color del grupo
     */
    public Grupo(Propiedad prop1, Propiedad prop2, String colorGrupo) {
        this.miembros = new ArrayList<Propiedad>();
        this.colorGrupo = colorGrupo;
        miembros.add(prop1);
        miembros.add(prop2);
        this.numCasillas = 2;
    }

    /** Constructor para cuando el grupo está formado por tres propiedades.
     * @param prop1 Propiedad miembro 1
     * @param prop2 Propiedad miembro 2
     * @param prop3 Propiedad miembro 3
     * @param colorGrupo Color del grupo
     */
    public Grupo(Propiedad prop1, Propiedad prop2, Propiedad prop3, String colorGrupo) {
        this.miembros = new ArrayList<Propiedad>();
        this.colorGrupo = colorGrupo;
        miembros.add(prop1);
        miembros.add(prop2);
        miembros.add(prop3);
        this.numCasillas = 3;
    }

    // SECCIÓN DE MÉTODOS ÚTILES DE GRUPOS

    /** Verifica si alguna de las propiedades del grupo está hipotecada.
     * @return TRUE si alguna propiedad está hipotecada, FALSE en otro caso
     */
    public boolean estaHipotecadoGrupo() {
        for (Propiedad propiedad : this.miembros) {
            if (propiedad.estaHipotecada()) {
                return true;
            }
        }
        return false;
    }

    /** Método que añade una propiedad al array de propiedades del grupo.
     * @param propiedad Propiedad que se quiere añadir
     */
    public void anhadirPropiedad(Propiedad propiedad) {
        this.miembros.add(propiedad);
        this.numCasillas++;
    }

    /** Método que comprueba si el jugador tiene todas las propiedades del grupo.
     * @param jugador Jugador que se quiere evaluar
     * @return TRUE si es dueño de todas las propiedades, FALSE en otro caso
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        for (Propiedad propiedad : miembros) {
            if (propiedad.getDuenho() != jugador) {
                return false;
            }
        }
        return true;
    }

    // SECCIÓN DE GETTERS Y SETTERS DE GRUPOS

    public ArrayList<Propiedad> getMiembrosGrupo() {
        return this.miembros;
    }

    public void setMiembrosGrupo(ArrayList<Propiedad> miembros) {
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
        return recaudacionGrupo;
    }

    public void sumarRecaudacionGrupo(float recaudacionGrupo) {
        this.recaudacionGrupo += recaudacionGrupo;
    }
}
