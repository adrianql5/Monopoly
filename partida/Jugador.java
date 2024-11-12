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
    private int bloqueado;
    private Estadisticas estadisticas;
   
    private Jugador jugadorConElQueEstanEnDeuda;//si es true está en deuda con la banca, si es false con un jugador


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
        this.bloqueado=0;
    }
    
    /**Constructor principal. Desde este constructor también se crea el avatar.
     * @param nombre Nombre del jugador
     * @param tipoAvatar Tipo del avatar que tendrá
     * @param inicio Casilla en la que empezará
     * @param avCreados ArrayList de avatares creados (usado para dos propósitos:
     *                  evitar que dos jugadores tengan el mismo nombre y que dos avatares tengan mismo ID).
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
        this.propiedades = new ArrayList<Casilla>();
        this.vueltas_sin_comprar=0;
        this.bloqueado=0;
        this.jugadorConElQueEstanEnDeuda=null;
        this.estadisticas = new Estadisticas();
        

    }

    //FUNCIONES CLAVE DE LA CLASE JUGADOR-------------------------------------------------------------------------------

    //OG
    /**Método para pagar dinero a otro jugador.
     * Sólo quita el dinero de un jugador y se lo ingresa a otro (actualizando los atributos correspondientes).
     * @param cobrador Jugador que recibe la cantidad que paga este jugador
     * @param cantidad Cantidad que tiene que pagar el jugador
     */
    public void pagar(Jugador cobrador, float cantidad) {
        // Modificamos los atributos del pagador
        this.restarFortuna(cantidad);
        this.sumarGastos(cantidad);
        this.estadisticas.sumarPagoDeAlquileres(cantidad);

        // Modificamos los atributos del cobrador
        cobrador.sumarFortuna(cantidad);
        cobrador.getEstadisticas().sumarCobroDeAlquileres(cantidad);

        // Modificamos los atributos de la casilla
        this.getAvatar().getLugar().sumarDinero_recaudado(cantidad);

    }


    // SOBRECARGA DEL MÉTODO "pagar" para casillas tipo Impuesto y otras tasas
    /**Método para pagar casillas que no pueden ser compradas
     * @param nombre_casilla Nombre de la casilla que hay que pagar

    public void pagar(String nombre_casilla) {
        if() {

        }
    }*/

    // SOBRECARGA DEL MÉTODO "pagar"
    /**Método para pagar una casilla.
     * @param banca Es necesaria para varias comprobaciones
     * @param casilla Casilla cuyo alquiler hay que cobrar

    public void pagar(Jugador banca, Casilla casilla) {
        // Valores que nos van a hacer falta varias veces
        Jugador duenhoCasilla = casilla.getDuenho();

        // Ligera comprobación
        if(!duenhoCasilla.equals(banca)) {
            System.out.println("Llamada errónea a la función PAGAR(dueño, casilla): dueño incorrecto.");
            return;
        }

        //
    }*/

    // SOBRECARGA DEL MÉTODO "pagar"

    /**Método para pagar el alquiler de una casilla a su dueño.
     * Esta manera de llamarlo PRESUPONE QUE EL DUEÑO NO ES LA BANCA.
     * //@param casilla Casilla cuyo alquiler hay que pagar

    public void pagar(Casilla casilla) {
        // Valores que nos van a hacer falta varias veces
        Jugador duenhoCasilla = casilla.getDuenho();
        float alquiler = casilla.evaluarAlquiler();

        // Realizamos el pago
        pagar(duenhoCasilla, alquiler);
    }*/


    // GETTERS Y SETTERS------------------------------------------------------------------------------------------------

    public void setBloqueado(int turnos){
        this.bloqueado=turnos;
    }
    public int restarBloqueado(int turnos){
        return this.bloqueado = turnos -1;
    }
    public void setDeudaConJugador(Jugador jugador){
        this.jugadorConElQueEstanEnDeuda=jugador;
    }
    
    public Jugador getDeudaConJugador(){
        return this.jugadorConElQueEstanEnDeuda;
    }
    public Estadisticas getEstadisticas() {
        return this.estadisticas;
    }


    //SECCIÓN DE MÉTODOS ÚTILES DE JUGADOR------------------------------------------------------------------------------

    /**Método para añadir una propiedad al jugador.
     * @param casilla Casilla a añadir
     */
    public void anhadirPropiedad(Casilla casilla) {
        if (!propiedades.contains(casilla)) {
            this.propiedades.add(casilla);
        }
    }
    
    /**Método para eliminar una propiedad del arraylist de propiedades de jugador.
     * @param casilla Casilla a añadir
     */
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
        this.estadisticas.sumarVecesEnLaCarcel(1);
        this.enCarcel = true;
        this.avatar.getLugar().anhadirAvatar(this.avatar);
    }

    /**Método para contar cuántas casillas posee un jugador de un tipo determinado
     * Solo se usa para las propiedades de tipo Transportes de momento
     * @param tipo Tipo de propiedad
     */
    public int numeroCasillasTipo(String tipo){
        int contador=0;
        for(Casilla c: propiedades){
            if(c.getTipo().equals(tipo)){
                contador++;
            }
        }
        return contador;
    }
    
    //SECCIÓN DE MÉTODOS QUE GESTIONAN LA FORTUNA DEL JUGADOR-----------------------------------------------------------

    /**
     * Método para añadir fortuna a un jugador.
     * Si hay que restar fortuna, se pasaría un valor negativo (o también puedes usar restarFortuna si eres Adrián).
     * @param valor Valor a añadir
     */
    public void sumarFortuna(float valor) {
        this.fortuna += valor;
    }

    /**
     * Método para restar fortuna a un jugador.
     * @param valor Valor a añadir
     */
    public void restarFortuna(float valor){
        this.fortuna -=valor;
    }

    /**Método para sumar gastos a un jugador.
     * @param valor Cantidad que sumar a los gastos del jugador (será el precio de un solar, impuestos pagados...).
     */
    public void sumarGastos(float valor) {
        this.gastos += valor;
    }

    public float getGastos() {
        return this.gastos;
    }

    //SECCÓN DE MÉTODOS BOOLEANOS DE JUGADOR----------------------------------------------------------------------------
    public boolean isEnCarcel() {
        return enCarcel;
    }

    
    public boolean tieneDinero(){
        if(fortuna<=0){
            return false;
        }
        return true;
    }

    public boolean tienePropiedadesHipotecables(){
        for( Casilla c: propiedades){
            if(!c.estaHipotecada()){
                return false;
            }
        }
        return true;
    }


    //SECCIÓN DE GETTERS Y SETTERS DE JUGADOR
    public String getNombre(){
        return this.nombre;
    }

    public float getFortuna(){
        return this.fortuna;
    }

    public void setFortuna(float nuevaFortuna) {
        this.fortuna = nuevaFortuna;
    }

    public Avatar getAvatar() {
        return avatar;
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

    public int getVueltas(){return this.vueltas;}

    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
    }


    //SECCIÓN DE MÉTODOS QUE TIENE QUE VER CON VUELTAS EN EL TABLERO
    public void sumarVuelta(){
        vueltas++;
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


    //SECCIÓN QUE DEVUELVE INFORMACIÓN DE JUGADOR
    /**Método que devuelve la información de un jugador*/
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
                    System.out.print(", "); // Añade coma a todo menos a la última
                }
            }
            System.out.println("]");

            // Imprimir edificios
            System.out.println("\tEdificios: {");
            String[] tipos = {"casa", "hotel", "piscina", "pista de deporte"};
            for (int i = 0; i < this.getPropiedades().size(); i++) {
                if( this.getPropiedades().get(i).getTipo().equals("Solar")){
                    if (0 != this.getPropiedades().get(i).getNumeroEdificios()) {
                        System.out.println("\t\t" + this.getPropiedades().get(i).getNombre() + ": {");

                        for (String tipo : tipos) {
                            ArrayList<Edificio> edificios = this.getPropiedades().get(i).getEdificiosPorTipo(tipo);

                            if (!edificios.isEmpty()) {
                                System.out.print("\t\t\t" + tipo + ": [");
                                for (int j = 0; j < edificios.size(); j++) {
                                    System.out.print(edificios.get(j).getId());
                                    if (j < edificios.size() - 1) {
                                        System.out.print(", "); // Añade coma a todo menos a la última
                                    }
                                }
                                System.out.println("]");
                            }
                        }
                        System.out.println("\t\t}");
                    }
                    System.out.println("\t}");
                }
            }
        }
        System.out.println("}");
    }

    /**Método para mostrar las estadísticas de un jugador*/
    public void infoEstadisticas() {
        System.out.println("{");
        System.out.printf("\tdineroInvertido: %,.2f€,\n",this.getGastos());
        System.out.printf("\tpagoTasasEImpuestos: %,.2f€,\n", this.estadisticas.getImpuestosPagados());
        System.out.printf("\tpagoDeAlquileres: %,.2f€,\n", this.estadisticas.getPagoDeAlquileres());
        System.out.printf("\tcobroDeAlquileres: %,.2f€,\n", this.estadisticas.getCobroDeAlquileres());
        System.out.printf("\tpasarPorCasillaDeSalida: %,.2f€,\n", this.estadisticas.getDineroSalidaRecaudado());
        System.out.printf("\tpremiosInversionesOBote: %,.2f€,\n", this.estadisticas.getDineroRecaudadoBote());
        System.out.printf("\tvecesEnLaCarcel: %d\n", this.estadisticas.getVecesEnLaCarcel());
        System.out.println("}");
    }
    
}

