package monopoly.casillas;

public class Especial {
        //ATRIBUTOS
        private String nombre;//Nombre de la casilla
        private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicio, Comunidad).
        private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
        private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
        private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
        private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
        private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicio/transportes o impuestos.
        private float hipoteca; //Valor otorgado por hipotecar una casilla
        private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.
        private ArrayList<ArrayList<Edificio>> edificios;
        private boolean estaHipotecada;
        private boolean ya_se_duplico;
        private int veces_visitada;
        private float dinero_recaudado;





        /**Constructor para casillas de tipo Impuestos.
     * @param nombre Nombre de la casilla
     * @param posicion Posición en el tablero
     * @param impuesto Impuesto establecido
     * @param duenho Dueño de la casilla
     */
    public Especial(String nombre, int posicion, float impuesto, Jugador duenho) {
        this.nombre=nombre;
        this.posicion= posicion;
        this.impuesto=impuesto;
        this.duenho= duenho;
        this.avatares=new ArrayList<Avatar>();
        
    }
}
