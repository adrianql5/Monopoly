package monopoly;

import java.util.ArrayList;

import partida.Jugador;


class Grupo {

    //Atributos
    private ArrayList<Casilla> miembros; //Casillas miembros del grupo.
    private String colorGrupo; //Color del grupo
    private int numCasillas; //Número de casillas del grupo.


    //SECCIÓN DE CONSTRUCTORES DE GRUPOS

    /**Constructor para cuando el grupo está formado por DOS CASILLAS.
     * @param cas1 Casilla miembro 1
     * @param cas2 Casilla miembro 2
     * @param colorGrupo Color del grupo
     */
    public Grupo(Casilla cas1, Casilla cas2, String colorGrupo) {
        this.miembros= new ArrayList<Casilla>();
        this.colorGrupo=colorGrupo;
        miembros.add(cas1);
        miembros.add(cas2);
        this.numCasillas=2;
    }

    /**Constructor para cuando el grupo está formado por TRES CASILLAS.
     * @param cas1 Casilla miembro 1
     * @param cas2 Casilla miembro 2
     * @param cas3 Casilla miembro 3
     * @param colorGrupo Color del grupo
     */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo) {
        this.miembros= new ArrayList<Casilla>();
        this.colorGrupo=colorGrupo;
        miembros.add(cas1);
        miembros.add(cas2);
        miembros.add(cas3);
        this.numCasillas=3;
    }


    //SECCIÓN DE MÉTODOS ÚTILES DE CRUPOS

    /** Método que añade una casilla al array de casillas miembro de un grupo.
     * @param miembro Casilla que se quiere añadir
     */
    public void anhadirCasilla(Casilla miembro) {
        this.miembros.add(miembro);
        this.numCasillas++;
    }


    /**Método que comprueba si el jugador pasado tiene en su haber todas las casillas del grupo.
     * @param jugador Jugador que se quiere evaluar
     * @return TRUE si es dueño de todas las casillas del grupo, FALSE en otro caso
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        for (Casilla c: miembros){
            if(c.getDuenho()!=jugador){
                return false;
            }
        }
        return true;
    }

    // SECCIÓN DE GETETRS Y SETTERS DE GRUPOS
    
    public ArrayList<Casilla> getMiembrosGrupo(){
        return this.miembros;
    }

    public void setMiembrosGrupos(ArrayList<Casilla> miembros){
        this.miembros=miembros;
    }

    public String getColorGrupo(){
        return this.colorGrupo;
    }

    public void setColorGrupo(String colorgrupo){
        this.colorGrupo=colorgrupo;
    }

    public int getNumCasillasGrupo(){
        return this.numCasillas;
    }

    public void setNumCasillasGrupo(int numCasillas){
        this.numCasillas=numCasillas;
    }
}