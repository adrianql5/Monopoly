package partida;

import java.util.ArrayList;

import monopoly.*;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.
    private int vueltas_sin_comprar;


    //SECCIÓN DE CONSTRUCTORES DE JUGADOR
    
    //Constructor vacío para la banca
    public Jugador() {
        this.nombre="banca";
        this.avatar=null;
        this.fortuna=0;
        this.gastos=0;
        this.enCarcel=false;
        this.tiradasCarcel=0;
        this.vueltas=0;
        this.propiedades=new ArrayList<Casilla>();
        this.vueltas_sin_comprar=0;
    }

    /*Constructor principal. Requiere parámetros:
     * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
     * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
     * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {

        this.nombre = nombre;
        this.avatar = new Avatar(tipoAvatar, this, inicio, avCreados);
        this.avatar.setTipo(tipoAvatar);
        this.avatar.setLugar(inicio);
        this.fortuna = Valor.FORTUNA_INICIAL;
        this.gastos = 0;
        this.enCarcel = false;
        this.tiradasCarcel = 0;
        this.vueltas = 0;
        this.avatar.setJugador(this);
        this.propiedades = new ArrayList<>();
        this.vueltas_sin_comprar=0;

    }



    //SECCIÓN DE MÉTODOS ÚTILES DE JUGADOR

    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.

    public void anhadirPropiedad(Casilla casilla) {
        if (!propiedades.contains(casilla)) {
            this.propiedades.add(casilla);
        }
    }
    
    
    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        if (propiedades.contains(casilla)) {
            this.propiedades.remove(casilla);
        }
    }
    
    /**Método para establecer al jugador en la cárcel.
     * @param pos Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).
     */
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        this.avatar.getLugar().eliminarAvatar(this.avatar);
        this.avatar.setLugar(pos.get(1).get(0));
        this.enCarcel = true;
        this.avatar.getLugar().anhadirAvatar(this.avatar);
    }

    
    public int numeroCasillasTipo(String tipo){
        int contador=0;
        for(Casilla c: propiedades){
            if(c.getTipo().equals(tipo)){
                contador++;
            }
        }
        return contador;
    }
    
    //SECCIÓN DE MÉTODOS QUE GESTIONAN LA FORTUNA DEL JUGADOR

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) {
        this.fortuna += valor;
    }
    public void restarFortuna(float valor){
        this.fortuna -=valor;
    }

    /**Método para sumar gastos a un jugador.
     * @param valor Cantidad que sumar a los gastos del jugador (será el precio de un solar, impuestos pagados...).
     */
    public void sumarGastos(float valor) {
        this.gastos += valor;
    }


    //SECCÓN DE MÉTODOS BOOLEANOS DE JUGADOR
    public boolean isEnCarcel() {
        return enCarcel;
    }
    
    public boolean estaHipotecado() {
        return false;
    }

    public boolean esBanca(){
        if(this.avatar==null) return true;
        return false;
    }


    public boolean estaEnBancarrota() {
        if (this.getFortuna() <= 0) {
            // Comprobar si tiene propiedades hipotecables
            //for (Casilla propiedad : this.getPropiedades()) {
            //    if (!propiedad.estaHipotecada()) {
            //        // Si el jugador tiene al menos una propiedad sin hipotecar, no está en bancarrota
            //        return false;
            //    }
            //}
            // Si llega aquí, significa que no tiene dinero ni propiedades útiles
            return true;
        }
        // Si tiene fortuna, no está en bancarrota
        return false;
    }


    //SeECCIÓN DE GETTERS Y SETTERS DE JUGADOR
    public String getNombre(){
        return this.nombre;
    }

    public float getFortuna(){
        return this.fortuna;
    }

    public void setFortuna(float nuevaFortuna) {
        this.fortuna = nuevaFortuna;
    }


    public void sumarVuelta(){
        vueltas++;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public float getGastos() {
        return gastos;
    }

    public void setGastos(float gastos) {
        this.gastos = gastos;
    }


    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
    }

    public void setTiradasCarcel(int tiradasCarcel) {
        this.tiradasCarcel = tiradasCarcel;
    }

    public int getTiradasCarcel(){
        return tiradasCarcel;
    }

    public int getVueltas_sin_comprar(){
        return vueltas_sin_comprar;
    }

    public void sumarVueltas_sin_comprar(){
        vueltas_sin_comprar++;
    }
    public void setVueltas_sin_comprar(int vueltas_sin_comprar){
        this.vueltas_sin_comprar = vueltas_sin_comprar;
    }

    public int getVueltas() {
        return vueltas;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
    }


    //SECCIÓN QUE DEVUELVE INFORMACIÓN DE JUGADOR
    public void infoJugador() {
        System.out.println("{");
        // Imprimir nombre, avatar y fortuna con separador de miles para la fortuna
        System.out.println("\tnombre: " + this.getNombre() + ",");
        System.out.println("\tavatar: " + this.getAvatar().getId() + ",");
        System.out.printf("\tfortuna: %,.0f,\n", this.getFortuna()); // Usar formato con separador de miles y sin decimales

        // Imprimir propiedades
        System.out.print("\tpropiedades: ");
        if (this.getPropiedades().isEmpty()) {
            System.out.println("Ninguna");
        } else {
            System.out.print("[");
            for (int i = 0; i < this.getPropiedades().size(); i++) {
                System.out.print(this.getPropiedades().get(i).getNombre());
                if (i < this.getPropiedades().size() - 1) {
                    System.out.print(", ");
                    // Añade coma a todo menos a la última por eso el menos 1
                }
            }
            System.out.println("]");
        }
        System.out.println("}");
    }

    
}

