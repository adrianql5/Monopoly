package monopoly.casillas;

import java.util.ArrayList;

import monopoly.Grupo;
import monopoly.Valor;
import monopoly.edificios.Edificio;
import partida.*;
import partida.avatares.Avatar;



public class Casilla {

    //ATRIBUTOS
    private String nombre;//Nombre de la casilla
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.

    
    public Casilla(String nombre, int posicion, Jugador duenho) {
        this.nombre=nombre;
        this.posicion= posicion;
        this.duenho= duenho;
        this.avatares= new ArrayList<Avatar>();
    }


    /**Método utilizado para añadir un avatar al array de avatares en casilla.*/
    public void anhadirAvatar(Avatar av) {
        this.avatares.add(av);
    }

    /**Método utilizado para eliminar un avatar del array de avatares en casilla.*/
    public void eliminarAvatar(Avatar av) {
        this.avatares.remove(av);
    }

 


    //SECCION DE BUILDEAR Y COMPRAR EDIFICIOS---------------------------------------------------------------------------
    /**Método para evaluar lo que hay que pagar en una casilla de tipo Servicio.
     * Sobrecarga del método para las casillas de este tipo ya que necesitan el valor de la tirada.
     * @param tirada Valor de la tirada
     */
    public float evaluarAlquiler(int tirada) {
        // Precio base del alquiler de cualquier casilla
        float totalAlquiler = this.impuesto;

        if(this.tipo.equals("Servicio")) {
            totalAlquiler *= tirada * (this.duenho.numeroCasillasTipo("Servicio") == 1 ? 4 : 10);
        }
        else {
            System.out.println("Error en evaluarAlquiler(tirada): se intentó usar en una casilla que no es Servicio.");
        }

        return totalAlquiler;
    }

    /**Método para evaluar lo que hay que pagar en una casilla de tipo Transporte o Solar.*/
    public float evaluarAlquiler() {
        // Precio base del alquiler de cualquier casilla
        float totalAlquiler = this.impuesto;

        if(this.tipo.equals("Transporte")) {
            // Se paga 25% más por cada casilla de Transporte que posee el jugador
            totalAlquiler *= 0.25f * this.duenho.numeroCasillasTipo("Transporte");;
        }
        else if(this.tipo.equals("Solar")) {
            // Si es dueño del grupo se duplica el precio base del alquiler del solar
            if (this.grupo.esDuenhoGrupo(this.duenho)){
                totalAlquiler *= 2f;
            }

            // Sumamos al total el precio de las casas si las hubiera
            // En el ArrayList de edificios la posición 0 es el ArrayList de las casas :)
            switch(this.edificios.get(0).size()) {
                case 0: break;
                case 1: totalAlquiler += this.impuesto * 5f; break;
                case 2: totalAlquiler += this.impuesto * 15f; break;
                case 3: totalAlquiler += this.impuesto * 35f; break;
                case 4: totalAlquiler += this.impuesto * 50f; break;
                default:
                    System.out.println("Cómo pudiste edificar más de 4 casas en un Solar meu...");
            }

            // Sumamos al total el precio de los hoteles, piscinas y pistas de deporte
            // (posiciones 1,2,3 en el ArrayList de edificios respectivamente)
            totalAlquiler += this.impuesto * 70f * this.edificios.get(1).size();
            totalAlquiler += this.impuesto * 25f * ( this.edificios.get(2).size() + this.edificios.get(3).size() );

        }
        else {
            System.out.println("Error en evaluarAlquiler: tipo de casilla inválido.");
        }

        return totalAlquiler;
    }

   // Devuelve el índice correspondiente al tipo de edificación
    public int getTipoIndex(String tipo) {
        switch (tipo) {
            case "casa":
                return 0;
            case "hotel":
                return 1;
            case "piscina":
                return 2;
            case "pista de deporte":
                return 3;
            default:
                return -1;
        }
    }

    // Añadir una edificación del tipo especificado
    public void anhadirEdificio(Edificio ed) {
        int index = getTipoIndex(ed.getTipo());
        if (index != -1) {
            this.edificios.get(index).add(ed);
        } 

    }



    public void eliminarCasasDeCasilla() {
        this.getCasas().clear();
    }


    public void eliminarTodosLosEds() {
        for (ArrayList<Edificio> lista : this.edificios) {
            lista.clear(); 
        }
    }

    public int contarTipoPropiedadesCasilla(String tipo) {
        int index = getTipoIndex(tipo);
        if (index != -1) {
            return this.edificios.get(index).size();
        }
        return 0; // Tipo inválido o no soportado
    }

    public int contarTipoPropiedadesGrupos(String tipo) {
        int index = getTipoIndex(tipo); // Obtener el índice del tipo de edificación
        if (index == -1) {
            return 0; // Retornar 0 si el tipo es inválido
        }

        int contador = 0;
        // Contar todas las edificaciones del tipo especificado en cada casilla del grupo
        for (Casilla c : grupo.getMiembrosGrupo()) {
            contador += c.contarTipoPropiedadesCasilla(tipo); // Usar la función para contar en cada casilla
        }

        return contador;
    }
    


    
// Verifica si es posible edificar una propiedad del tipo dado
    public boolean esEdificable(String tipo, Jugador solicitante) {
        int maxHotelesG=contarTipoPropiedadesGrupos("hotel");
        int maxPistasG=contarTipoPropiedadesGrupos("pista de deporte");
        int maxPiscinasG=contarTipoPropiedadesGrupos("piscina");
        int maxCasasG=contarTipoPropiedadesGrupos("casa");

        int maxHotelesC=contarTipoPropiedadesCasilla("hotel");
        int maxPistasC=contarTipoPropiedadesCasilla("pista de deporte");
        int maxPiscinasC=contarTipoPropiedadesCasilla("piscinas");
        int maxCasasC=contarTipoPropiedadesCasilla("casa");


        int max=this.getGrupo().getNumCasillasGrupo();
            if(!this.getGrupo().estaHipotecadoGrupo()){
                    switch (tipo) {
                        case "casa":
                            if(maxHotelesG>=max){
                                if(maxCasasG>=max){
                                    System.out.println("Llegaste al máximo de casa de este grupo");
                                    return false;
                                }
                                else{
                                    return true;
                                }
                            }
                            else{
                                if(maxCasasC>3){
                                    System.out.println("Debes edificar un hotel, tienes 4 casas.");
                                    return false;
                                }
                                return true;
                            }

                        case "hotel":
                            if(maxHotelesG>=max){
                                System.out.println("Has llegado al máximo de hoteles de este Grupo");
                                return false;
                            }
                            else{
                                if(maxHotelesG==(max-1)){
                                    if(maxCasasG-4>max){
                                        System.out.println("No puedes edificar un hotel, debes vender alguna casa de tu grupo primero");
                                        return false;
                                    }
                                    else{
                                        if(maxCasasC<4){
                                            System.out.println("Debes tener al menos 4 casas para poder edificar un hotel");
                                            return false;
                                        }
                                        else{
                                            return true;
                                        }   
                                    }
                                }
                                else{
                                    if(maxCasasC<4){
                                        System.out.println("Debes tener al menos 4 casas para poder edificar un hotel");
                                        return false;
                                    }
                                    else{
                                        return true;
                                    }

                                }
                            }

                        case "piscina":
                            if(maxPiscinasG>=max){
                                System.out.println("Has llegado al máximo de piscinas de este grupo");
                                return false;
                            }    
                            else{
                                if(maxHotelesC>=1 && maxCasasC>=2){
                                    return true;
                                }
                                else{
                                    System.out.println("Para construir una piscina en esta casilla nesitas mínimo un hotel y dos casas.");
                                    return false;
                                }
                            }
                        
                        
                        case "pista de deporte":
                            if(maxPistasG>=max){
                                System.out.println("Has llegado al máximo de pistasd de deporte de este grupo");
                                return false;
                            }    
                            else{
                                if(maxHotelesC>=2){
                                    return true;
                                }
                                else{
                                    System.out.println("Para construir una pista de deportes en esta casilla nesitas dos hoteles.");
                                    return false;
                                }
                            }

                        default:
                            System.out.println("Tipo de edificación inválido. Introduce casa, hotel, piscina o pista de deporte.");
                            return false;
                    }
                }
                else{
                    System.out.println("No puedes edificar porque alguna propiedad del grupo está hipotecada.");
                    return false;
                }
            
    }

 
    // Genera un ID único basado en el tipo y los IDs ya presentes en el subarray de ese tipo
    public String generarID(String tipo) {
        int tipoIndex = getTipoIndex(tipo);
        ArrayList<Edificio> listaEdificios = edificios.get(tipoIndex);

        // Encontrar el número más alto de ID ya asignado
        int maxId = 0;
        for (Edificio edificio : listaEdificios) {
            String id = edificio.getId(); // Obtener el ID del edificio
            if (id.startsWith(tipo + "-")) {
                // Extraer el número del ID y compararlo
                String[] partes = id.split("-");
                int numero = Integer.parseInt(partes[1]);
                if (numero > maxId) {
                    maxId = numero;
                }
            }
        }

        // Generar un nuevo ID incrementado
        return tipo + "-" + (maxId + 1);
    }



    public ArrayList<Edificio> getCasas() {
        return this.edificios.get(0); // Retorna la lista de casas
    }

    public ArrayList<Edificio> getHoteles() {
        return this.edificios.get(1); // Retorna la lista de hoteles
    }

    public ArrayList<Edificio> getPiscinas() {
        return this.edificios.get(2); // Retorna la lista de piscinas
    }

    public ArrayList<Edificio> getPistasDeDeporte() {
        return this.edificios.get(3); // Retorna la lista de pistas de deporte
    }

    // Método para obtener la lista de edificios según su tipo
    public ArrayList<Edificio> getEdificiosPorTipo(String tipo) {
        int index = getTipoIndex(tipo); // Obtener el índice del tipo
        if (index != -1) {
            return this.edificios.get(index); // Retornar la lista correspondiente al tipo
        }
        return new ArrayList<>(); // Retornar una lista vacía si el tipo no es válido
    }

    public String listarEdificaciones() {
        String str = ""; // Inicializar la cadena vacía

        // Recorrer cada tipo de edificación y listarlas
        String[] tipos = {"casa", "hotel", "piscina", "pista de deporte"};
        for (String tipo : tipos) {
            ArrayList<Edificio> edificios = getEdificiosPorTipo(tipo);
            if (!edificios.isEmpty()) {
                for (Edificio edificio : edificios) {
                    str += edificio.infoEdificio() + "\n"; // Agregar la información del edificio
                }
            }
        }

        return str; // Retornar la cadena resultante
    }



 

    /**Método para añadir valor a una casilla. Utilidad:
     * (1) Sumar valor a la casilla de parking.
     * (2) Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
     * @param suma Cantidad a añadir al valor de la casilla
     */
    public void sumarValor(float suma) {
        this.valor +=suma;
    }


    /** Método usado para comprar una casilla determinada.
     * @param solicitante Jugador que solicita la compra de la casilla
     * @param banca La banca es el dueño de las casillas no compradas aún
     */
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        // ¿La casilla es de un tipo que se pueda comprar?
        if(this.esTipoComprable()) {
            // ¿El jugador que la quiere comprar está encima de la casilla?
            if(solicitante.getAvatar().getLugar()==this) {
                // ¿La casilla pertenece a la banca?
                if(this.duenho==banca) {
                    // ¿El jugador tiene suficiente dinero para poder pagarla?
                    if (solicitante.getFortuna()>=this.valor) {
                        solicitante.restarFortuna(this.valor);
                        solicitante.sumarGastos(this.valor);
                        banca.eliminarPropiedad(this);
                        solicitante.anhadirPropiedad(this);
                        this.duenho=solicitante;

                        System.out.printf("%s ha comprado la propiedad %s por el precio de %,.0f€\n",
                                solicitante.getNombre(), this.nombre, this.valor);
                    }
                    else {
                        System.out.println("No tienes dinero para comprar esta casilla.");
                    }
                }
                else {
                    System.out.println("Esta casilla no está en venta.");
                }
            }
            else {
                System.out.println("¡Tienes que caer en la casilla para poder comprarla!");
            }
        }
        else {
            System.out.println("¡¡Esta casilla no se puede comprar!! \uD83D\uDE21");
        }
    }


    /**Método para verificar si el tipo de una casilla la hace comprable*/
    public boolean esTipoComprable() {
        return (this.tipo.equals("Solar") || this.tipo.equals("Transporte") || this.tipo.equals("Servicio"));
    }

    // Nota: creo q esta no es la implementación que pedían pero de todos modos no lo usamos
    /** Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() {
        if(this.duenho==null || this.duenho.getNombre().equals("Banca")){
            return "La casilla" + this.nombre + "está en venta por un precio de " + this.valor +"\n";
        }
        return "Esta casilla no está en venta";
    }

    public boolean esDuenho(Jugador jugador){
        return this.duenho == jugador;
    }

    
    //SECCIÓN QUE DEVUELVE INFORMACIÓN DE CASILLA

    /**Método que devuelve una cadena con información específica de cada tipo de casilla.
     * La usa descCasilla excepto cuando es una casilla que no se puede describir o para Parking y Carcel.
     * Se da por hecho que es una casilla que se puede describir (excepto Parking y Carcel).
     * Nota: los imprime con salto de línea al final.
     * Nota 2: por cómo está implementado descCasilla, el tipo Especial devuelve info para Salida.
     */
    public String infoCasilla() {

        // Cadena con la información que queremos devolver
        String info = "{\n";

        // La completamos con la información correspondiente en función del tipo de casilla
        switch(this.tipo) {
            case "Solar":
                info += "\tTipo: " + this.tipo + "\n";
                info += "\tColor del grupo: " + this.grupo.getColorGrupo() + "\n";
                info += "\tDueño: " + this.duenho.getNombre() + "\n";
                info += String.format("\tPrecio: %,.0f€\n", this.valor);
                info += String.format("\tAlquiler: %,.0f€\n", this.impuesto);
                info += String.format("\tHipoteca: %,.0f€\n", this.hipoteca);
                info += String.format("\tPrecio casa: %,.0f€\n", this.valor * 0.60f);
                info += String.format("\tPrecio hotel: %,.0f€\n", this.valor * 0.60f);
                info += String.format("\tPrecio piscina: %,.0f€\n", this.valor * 0.40f);
                info += String.format("\tPrecio pista de deporte: %,.0f€\n", this.valor * 1.25f);
                info += String.format("\tAlquiler de 1 casa: %,.0f€\n", this.impuesto * 5f);
                info += String.format("\tAlquiler de 2 casas: %,.0f€\n", this.impuesto * 15f);
                info += String.format("\tAlquiler de 3 casas: %,.0f€\n", this.impuesto * 35f);
                info += String.format("\tAlquiler de 4 casas: %,.0f€\n", this.impuesto * 50f);
                info += String.format("\tAlquiler hotel: %,.0f€\n", this.impuesto * 70f);
                info += String.format("\tAlquiler piscina: %,.0f€\n", this.impuesto * 25f);
                info += String.format("\tAlquiler pista de deporte: %,.0f€\n", this.impuesto * 25f);
                
                info += "\tEdificios: [";
                for(ArrayList<Edificio> tipo: edificios){
                    for(Edificio ed: tipo){
                        info+= String.format(ed.getId())+" ";
                    }
                }

                info+="]\n";
                break;

            case "Transporte":
                info += "\tTipo: " + this.tipo + "\n";
                info += "\tDueño: " + this.duenho.getNombre() + "\n";
                info += String.format("\tPrecio: %,.0f€\n", this.valor);
                info += String.format("\tPago por caer: %,.0f€\n",
                        this.impuesto * 0.25f * this.duenho.numeroCasillasTipo("Transporte"));
                info += String.format("\t\t(cada casilla de este tipo que tengas suma 1/4 de %,.0f€ al alquiler)\n",
                        this.impuesto);
                info += String.format("\tHipoteca: %,.0f€\n", this.hipoteca);
                break;

            case "Servicio":
                info += "\tTipo: " + this.tipo + "\n";
                info += "\tDueño: " + this.duenho.getNombre() + "\n";
                info += String.format("\tPrecio: %,.0f€\n", this.valor);
                info += String.format("\tPago por caer: dados * x * %,.0f€\n", this.impuesto);
                info += "\t\t(x=4 si se posee una casilla de este tipo, x=10 si se poseen 2)\n";
                info += String.format("\tHipoteca: %,.0f€\n", this.hipoteca);
                break;

            case "Impuestos":
                info += "\tTipo: Impuestos\n";
                info += String.format("\tA pagar: %,.0f€\n", this.impuesto);
                break;
            case "Especial":
                // La única posibilidad de llegar aquí es que sea la casilla Salida (ver descCasilla en Menú)
                info += String.format("\tPago por vuelta: %,.0f€\n", Valor.SUMA_VUELTA);
                break;
            default:
                System.out.println("Error en la función infoCasilla.");
        }

        // Añadimos los jugadores en la casilla si los hay y cerramos
        info += jugadoresEnCasilla();
        info += "}\n";

        return info;
    }

    /**Método auxiliar que devuelve la lista de jugadores en una casilla.
     * Si no hay ningún jugador en la casilla devuelve una cadena vacía.
     * Nota: incluye el salto de línea al final.
     */
    public String jugadoresEnCasilla() {
        // Obtenemos la lista de avatares que hay en la casilla
        ArrayList<Avatar> avataresEnCasilla = this.getAvatares();

        // Creamos la cadena que se inicializa a vacía por si no hay avatares en la casilla
        String jugadores = "";

        // Si hubiera avatares se añaden al String
        if(!avataresEnCasilla.isEmpty()) {

            // Recorremos la lista de avatares y mostramos todos los jugadores en la misma línea
            jugadores += "\tJugadores: ";
            for (Avatar avatar : avataresEnCasilla) {
                jugadores += "[" + avatar.getJugador().getNombre() + "]  ";
            }
            jugadores += "\n";
        }

        return jugadores;
    }


    //SECCIÓN DE GETTERS Y SETTERS DE CASILLA

    public String getNombre(){
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }
  
    public void setTipo(String tipo_casilla) {
        switch(tipo_casilla) {
            case "Especial": case "Impuesto": case "Servicio": case "Transporte":
            case "Caja de comunidad": case "Suerte": case "Solar":
                this.tipo=tipo_casilla;
                break;
            default:
                System.out.println(tipo_casilla + " no es un tipo de casilla válido.\n");
        }
    }
    
    public float getValor() {
        return valor;
    }
    
    public void setValor(float valor_casilla) {
        if (valor_casilla > 0) {
            this.valor = valor_casilla;
        }
        else {
            System.out.println("El valor de la casilla debe ser positivo.\n");
        }
    }

    public int getPosicion(){
        return posicion;
    }
    
    public void setPosicion(int posicion_casilla) {
        if(posicion_casilla<40 && posicion_casilla>-1) {
            this.posicion = posicion_casilla;
        }
        else {
            System.out.println(posicion + " no es una casilla válida.\n");
        }
    }
    
    public Jugador getDuenho(){
        return duenho;
    }
   
    public void setDuenho(Jugador duenho_casilla) {
        if (duenho_casilla != null) {
            for (Avatar avatar : this.avatares) {
                // Si el jugador es el dueño de algún avatar en la casilla
                if (avatar.getJugador()==duenho_casilla)  {
                    break;
                }
                else{
                    this.duenho = duenho_casilla; // Asigna el nuevo dueño
                }
            }
        }
        else {
            System.out.println("El jugador proporcionado no es un dueño válido.\n");
        }
    }

    public Grupo getGrupo(){
        return grupo;
    }
    
    public void setGrupo(Grupo grupo_casilla) {
        if (grupo_casilla != null) {
            this.grupo = grupo_casilla;
        }
        else {
            System.out.println("El grupo no puede ser nulo.\n");
        }
    }

    public void sumarImpuesto(float impuesto){
        this.impuesto+=impuesto;
    }


    public float getImpuesto(){
        return impuesto;
    }

    public void setImpuesto(float impuesto_casilla) {
        if (impuesto_casilla > 0) {
            this.impuesto = impuesto_casilla;
        }
        else {
            System.out.println("El impuesto debe ser un valor positivo.\n");
        }
    }

    public float getHipoteca(){
        return hipoteca;
    }
    
    public void setHipoteca(float hipoteca_casilla) {
        if (hipoteca_casilla > 0) {
            this.hipoteca = hipoteca_casilla;
        }
        else {
            System.out.println("La hipoteca debe ser un valor positivo.\n");
        }
    }
    
    public ArrayList<Avatar> getAvatares(){
        return avatares;
    }
    public int getNumeroEdificios() {
        int totalEdificios = 0;
        if(this.edificios==null){
            return 0;
        }
        for (ArrayList<Edificio> tipoEdificio : this.edificios) {
            totalEdificios += tipoEdificio.size();
        }
        return totalEdificios;
    }

    // Getter para obtener el valor de veces_visitada
    public int getVecesVisitada() {
        return this.veces_visitada;
    }

    // Setter para establecer el valor de veces_visitada
    public void sumarVecesVisitada(int vecesVisitada) {
        this.veces_visitada += vecesVisitada;
    }

    public float getDinero_recaudado(){
        return this.dinero_recaudado;
    }
    public void sumarDinero_recaudado(float dinero_recaudado){
        if(this.getTipo().equals("Solar")){
            this.grupo.sumarRecaudacionGrupo(dinero_recaudado);
        }

        this.dinero_recaudado += dinero_recaudado;
    }
}

