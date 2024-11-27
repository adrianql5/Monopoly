package monopoly.casillas;

public class Propiedad extends Casilla{
    private boolean estaHipotecada;
    private float valor;
    private float hipoteca;
    public float dinero_recaudado;
    private int veces_visitada_por_duenho;

       //SECCIÓN DE CONSTRUCTORES DE CASILLA
    /**Constructor para casillas tipo Solar, Servicio y Transporte.
     * @param nombre Nombre de la casilla
     * @param tipo Debe ser Solar, Servicio o Transporte
     * @param posicion Posición en el tablero
     * @param valor Valor de la casilla
     * @param duenho Dueño de la casilla
     */
    public Propiedad(String nombre,int posicion, float valor, Jugador duenho) {
        super(nombre,posicion,duenho);
        this.valor = valor;
        this.hipoteca = valor/2f;
        this.dinero_recaudado = 0;
        this.veces_visitada_por_duenho = 0;
    }


    //SECCIÓN DE MÉTODOS ÚTILES DE CASILLA
    public int getVecesVisitadaPorDuenho(){
        return veces_visitada_por_duenho;
    }
    
    public void sumarVecesVisitadaPorDuenho(int valor){
        veces_visitada_por_duenho+=valor;
    }



    //SECCION DE HIPOTECAR

    public boolean estaHipotecada() {
        return estaHipotecada;
    }

    public void setDeshipotecada(){
        estaHipotecada=false;
    }


    public boolean esHipotecable() {
        if (!estaHipotecada) { // Simplificación de la condición
            boolean sinEdificios = true;

            if(!this.esTipoComprable()){
                System.out.println("Este tipo de propiedades no es hipotecable.");;
                return false;
            }
                

            if(this instanceof Solar){
                for (ArrayList<Edificio> tipoEdificio : this.edificios) {
                    if (!tipoEdificio.isEmpty()) {
                        sinEdificios = false;
                        break;
                    }
                }
            }
            
            if (!sinEdificios) {
                System.out.println("No puedes hipotecar la casilla " + this.getNombre() + " porque tienes que vender todas tus edificaciones.");
                return false; // Corregido: aquí debe devolver `false` para indicar que no se puede hipotecar
            } else {
                estaHipotecada = true;
                return true; // Retorna `true` si la propiedad fue hipotecada exitosamente
            }
        } else {
            System.out.println("No puedes hipotecar esta propiedad porque ya está hipotecada.");
            return false;
        }
    }

    public boolean esDesHipotecable() {
        if(!this.esTipoComprable()){
            System.out.println("Este tipo de propiedad no es deshipotecable");
            return false;
        }

        if(estaHipotecada){
            this.estaHipotecada = false;
            return true;
        }
        else{
            System.out.println("No puedes deshipotecar esta propiedad porque no está hipotecada.");
            return false;
        }
    }



}
