package partida;

import java.util.ArrayList;

import monopoly.*;
import monopoly.casillas.Casilla;
import monopoly.edificios.Edificio;
import partida.avatares.Avatar;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuenta las tiradas sin éxito que ha hecho el jugador para intentar salir.
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.
    private int vueltas_sin_comprar;
    private int bloqueado;
    private ArrayList<Trato> tratosPendientes; // Lista de tratos pendientes propuestos a este jugador

    private Estadisticas estadisticas;
    /**Atributo para movimiento avanzado. Lo usa buclePartida() para comprobaciones antes de llamar a analizarComando().
     * Pelota: se almacenan los siguientes movimientos que tiene que hacer el avatar si hubiese (ej: [2, 2, 1])
     * Coche: si en los 2 próximos turnos no puede mover se almacena [0, 0].
     */
    private ArrayList<Integer> movimientos_pendientes;

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
        // No inicializamos ni las estadísticas ni los movimientos pendientes pa la banca!
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
        this.movimientos_pendientes = new ArrayList<Integer>();
        this.tratosPendientes = new ArrayList<>(); // Inicializar la lista de tratos pendientes

    }

    //FUNCIONES CLAVE DE LA CLASE JUGADOR-------------------------------------------------------------------------------

    //OG
    /**Método para pagar dinero a otro jugador.
     * ESTA MANERA DE LLAMARLO SE USA PARA PAGAR ALQUILERES.
     * Sólo quita el dinero de un jugador y se lo ingresa a otro (actualizando los atributos correspondientes).
     * Se presupone que no se pasa la banca como cobrador (se actualizarían estadísticas que no tocan!!).
     * @param cobrador Jugador que recibe la cantidad que paga este jugador
     * @param alquiler Cantidad que tiene que pagar el jugador
     */
    public void pagar(Jugador cobrador, float alquiler) {
        // Modificamos los atributos del pagador
        this.restarFortuna(alquiler);
        this.sumarGastos(alquiler);
        this.estadisticas.sumarPagoDeAlquileres(alquiler);

        // Modificamos los atributos del cobrador
        cobrador.sumarFortuna(alquiler);
        cobrador.getEstadisticas().sumarCobroDeAlquileres(alquiler);

        // Modificamos los atributos de la casilla
        this.getAvatar().getLugar().sumarDinero_recaudado(alquiler);

    }


    // SOBRECARGA DEL MÉTODO "pagar" para pagar a la banca
    /**Método para pagar dinero a otro jugador.
     * ESTA MANERA DE LLAMARLO SE USA PARA PAGAR A LA BANCA (IMPUESTOS O TASAS).
     * CUANDO SE PAGAN IMPUESTOS O TASAS RECORDAR AÑADIR EL DINERO AL BOTE DEL PARKING EN EL MENÚ.
     * @param cantidad Dinero que hay que pagar a la banca
     * @param banca Estamos obligados a pasar la banca para actualizar sus atributos desde aquí
     */
    public void pagar(float cantidad, Jugador banca) {
        // Modificamos los atributos del pagador
        this.restarFortuna(cantidad);
        this.sumarGastos(cantidad);
        this.estadisticas.sumarImpuestosYTasasPagados(cantidad);

        // Modificamos los atributos del cobrador
        banca.sumarFortuna(cantidad);
    }

    /**Método que elimina el primer elemento del ArrayList movimientos_pendientes.
     * Si el ArrayList está vacío no hace nada.
     */
    public void eliminarMovimientoPendiente() {
        if(!this.movimientos_pendientes.isEmpty()) {
            this.movimientos_pendientes.remove(0);
        }
    }

    /**Método que deja al jugador sin poder tirar los 2 siguientes turnos.
     * Añadimos 3 ceros al ArrayList de movimientos pendientes.
     * Cada vez que pase turno se borra uno (por eso son 3, al pasar el primer turno ya quedan 2).
     * Cuando le llega el turno un 0 en este ArrayList bloquea la función lanzarDados().
     */
    public void dosTurnosSinTirar() {
        for (int i=0; i<3; i++) {
            this.movimientos_pendientes.add(0);
        }
    }

    /**Método que calcula los movimientos pendientes (sólo se usa para la pelota)
     * Si TIRADA>4 avanza. Si TIRADA<=4 retrocede.
     * Va parando en las casillas impares (a partir del 4 en el caso de avanzar)
     * @param tirada SE PRESUPONE QUE EL VALOR DE TIRADA TIENE SENTIDO (número entre 2 y 12)
     */
    public void calcularMovimientosPendientes(int tirada) {
        // Checkeo por si se usa mal la función (si se quieren calcular los movimientos restantes cuando ya hay)
        if(!this.movimientos_pendientes.isEmpty()) {
            System.out.println("Error en la función calcularMovimientosPendientes: ya hay movimientos pendientes.");
            return;
        }

        if(tirada>4) {
            // Si la tirada es mayor que 4 siempre va a haber que parar en la quinta casilla al avanzar
            this.movimientos_pendientes.add(5);
            // Cuando la tirada vale más de 5: por cada par (6y7, 8y9, etc.)
            for(int i=5; i<tirada; i+=2) {
                if (i+1==tirada) this.movimientos_pendientes.add(1);
                else this.movimientos_pendientes.add(2);
            }
            return;
        }

        // Si la tirada es menor o igual a 4 siempre va a haber que parar en la primera casilla al retroceder
        this.movimientos_pendientes.add(-1);
        if(tirada==2) {
            this.movimientos_pendientes.add(-1);
            return;
        }
        // tirada >=3
        this.movimientos_pendientes.add(-2);
        if(tirada==4) {
            this.movimientos_pendientes.add(-1);
        }
    }


    // GETTERS Y SETTERS------------------------------------------------------------------------------------------------

    public void setDeudaConJugador(Jugador jugador){
        this.jugadorConElQueEstanEnDeuda=jugador;
    }
    
    public Jugador getDeudaConJugador(){
        return this.jugadorConElQueEstanEnDeuda;
    }
    public Estadisticas getEstadisticas() {
        return this.estadisticas;
    }

    public ArrayList<Integer> getMovimientos_pendientes() {
        return this.movimientos_pendientes;
    }

    public float getGastos() {
        return this.gastos;
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


        // Si el avatar tiene movimientos pendientes (DISTINTOS DE 0) se eliminan
        // Nótese que con el caso actual del coche si el primer elemento es 0 el resto (si los hay) también lo son
        if(!this.movimientos_pendientes.isEmpty() && this.movimientos_pendientes.get(0)!=0) {
            int j = movimientos_pendientes.size();
            for(int i=0; i<j; i++) {
                this.movimientos_pendientes.remove(0);
            }
        }
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

    //SECCÓN DE MÉTODOS BOOLEANOS DE JUGADOR----------------------------------------------------------------------------
    public boolean isEnCarcel() {
        return this.enCarcel;
    }
    
    public boolean tieneDinero(){
        return this.fortuna > 0;
    }

    public boolean tienePropiedadesHipotecables(){
        for(Casilla c: this.propiedades){
            if(!c.estaHipotecada()){
                return true;
            }
        }
        return false;
    }

    public boolean esCocheAvanzado() {
        return this.avatar.getTipo().equals("coche") && this.avatar.getMovimientoAvanzado();
    }

    public boolean esPelotaAvanzado() {
        return this.avatar.getTipo().equals("pelota") && this.avatar.getMovimientoAvanzado();
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
    public void restarVuelta(){
        vueltas--;
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
        System.out.printf("\tpagoTasasEImpuestos: %,.2f€,\n", this.estadisticas.getImpuestosYTasasPagados());
        System.out.printf("\tpagoDeAlquileres: %,.2f€,\n", this.estadisticas.getPagoDeAlquileres());
        System.out.printf("\tcobroDeAlquileres: %,.2f€,\n", this.estadisticas.getCobroDeAlquileres());
        System.out.printf("\tpasarPorCasillaDeSalida: %,.2f€,\n", this.estadisticas.getDineroSalidaRecaudado());
        System.out.printf("\tpremiosInversionesOBote: %,.2f€,\n", this.estadisticas.getDineroRecaudadoBote());
        System.out.printf("\tvecesEnLaCarcel: %d\n", this.estadisticas.getVecesEnLaCarcel());
        System.out.println("}");
    }
    public ArrayList<Trato> getTratosPendientes() {
        return this.tratosPendientes;
    }
    public void agregarTrato(Trato trato) {
        this.tratosPendientes.add(trato);
        System.out.printf("Se ha añadido un nuevo trato pendiente: %s\n", trato.getId());
    }
    public void eliminarTrato(Trato trato) {
        if (this.tratosPendientes.remove(trato)) {
            System.out.printf("El trato %s ha sido eliminado de los pendientes.\n", trato.getId());
        } else {
            System.out.printf("No se encontró el trato %s en los pendientes.\n", trato.getId());
        }
    }
    public Trato buscarTratoPorId(String id) {
        for (Trato trato : tratosPendientes) {
            if (trato.getId().equals(id)) {
                return trato;
            }
        }
        return null; // Devuelve null si no encuentra el trato
    }
    public void listarTratosPendientes() {
        if (this.tratosPendientes.isEmpty()) {
            System.out.println("No tienes tratos pendientes.");
        } else {
            System.out.println("Tus tratos pendientes son:");
            for (Trato trato : tratosPendientes) {
                System.out.println(trato);
            }
        }
    }


}
