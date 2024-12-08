package monopoly.casillas.propiedades;

import java.util.*;
import monopoly.*;
import monopoly.edificios.*;
import partida.*;


public class Solar extends Propiedad {
    private ArrayList<ArrayList<Edificio>> edificios;
    private Grupo grupo;
    private int vecesVisitadaPorDuenho;

    // Constructor
    public Solar(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion,duenho);
        this.grupo = null;
        this.edificios = new ArrayList<>(4);
        this.vecesVisitadaPorDuenho = 0;
        for (int i = 0; i < 4; i++) {
            this.edificios.add(new ArrayList<>()); // Array de casas, hoteles, piscinas, pistas
        }
    }

    @Override
    public boolean evaluarCasilla(Jugador jugadorActual, int tirada) {
        if (duenho != jugadorActual) {
            if (!duenho.getNombre().equals("banca")) {
                if (!estaHipotecada) {
                    float precioAlquiler = evaluarAlquiler();
                    String mensaje = String.format("%s debe pagarle el alquiler de %s a %s: %,.0f€\n",
                            jugadorActual.getNombre(), this.nombre, duenho.getNombre(), precioAlquiler);
                    Juego.consola.imprimir(mensaje);
                    if (precioAlquiler > jugadorActual.getFortuna()) {
                        jugadorActual.setDeudaConJugador(duenho);
                        jugadorActual.setDeuda(precioAlquiler);
                        return false;
                    }
                    jugadorActual.pagar(duenho, precioAlquiler);
                    return true;
                } else {
                    Juego.consola.imprimir("La casilla " + this.nombre + " está hipotecada. No hay que pagar.");
                }
            } else {
                Juego.consola.imprimir("La casilla " + this.nombre + " está a la venta.\n");
            }
        } else {
            Juego.consola.imprimir("Esta casilla te pertenece.");        
            this.vecesVisitadaPorDuenho++;
        }
        return true;
    }

    public void incrementarVecesVisitadaPorDueho() {
        this.vecesVisitadaPorDuenho ++;
    }

    public int getVecesVisitadaPorDuenho() {
        return vecesVisitadaPorDuenho;
    }

    public ArrayList<ArrayList<Edificio>> getEdificios(){
        return edificios;
    }

    public int getNumeroEdificios(){
        int contador=0;

        for(ArrayList<Edificio> eds: edificios){
            contador += eds.size();
        }
        return contador;
    }

    // Calcular el valor del solar según su posición
    @Override
    public float calcularValor() {
        switch (posicion) {
            case 1, 3 -> { return Valor.GRUPO1 / 2; }
            case 6, 8, 9 -> { return Valor.GRUPO2 / 3; }
            case 11, 13, 14 -> { return Valor.GRUPO3 / 3; }
            case 16, 18, 19 -> { return Valor.GRUPO4 / 3; }
            case 21, 23, 24 -> { return Valor.GRUPO5 / 3; }
            case 26, 27, 29 -> { return Valor.GRUPO6 / 3; }
            case 31, 32, 34 -> { return Valor.GRUPO7 / 3; }
            case 37, 39 -> { return Valor.GRUPO8 / 2; }
            default -> { return 0.0f; }
        }
    }

    // Calcular el alquiler del solar
    @Override
    public float calcularAlquiler() {
        return valor * Valor.FACTOR_ALQUILER_SOLAR;
    }

    // Obtener el grupo del solar
    public Grupo getGrupo() {
        return grupo;
    }

    // Asignar un grupo al solar
    public void setGrupo(Grupo grupoCasilla) {
        if (grupoCasilla != null) {
            this.grupo = grupoCasilla;
        } else {
            Juego.consola.imprimir("El grupo no puede ser nulo.\n");
        }
    }

    public void edificarCasa(Jugador jugador){
        Casa casa =new Casa(this);
        if(casa.esEdificableCasa()){
            if (jugador.getFortuna() >= casa.getCoste()) {
                this.anhadirEdificio(casa); // Añadir el edificio a la casilla
                jugador.sumarGastos(casa.getCoste()); // Restar el coste del edificio
                jugador.sumarFortuna(-casa.getCoste()); // Reducir la fortuna del jugador
        
                Juego.consola.imprimir("El jugador " + jugador.getNombre() + " ha comprado el edificio " +
                    casa.getId() + " por " + casa.getCoste());
                }
                else{
                    Juego.consola.imprimir("No tienes suficiente dinero para edificar.");
                }
        }
    }

    public void edificarHotel(Jugador jugador){
        Hotel hotel =new Hotel(this);
        if(hotel.esEdificableHotel()){
            if (jugador.getFortuna() >= hotel.getCoste()) {
                this.anhadirEdificio(hotel); // Añadir el edificio a la casilla
                jugador.sumarGastos(hotel.getCoste()); // Restar el coste del edificio
                jugador.sumarFortuna(-hotel.getCoste()); // Reducir la fortuna del jugador
        
                Juego.consola.imprimir("El jugador " + jugador.getNombre() + " ha comprado el edificio " +
                    hotel.getId() + " por " + hotel.getCoste());
                }
                else{
                    Juego.consola.imprimir("No tienes suficiente dinero para edificar.");
                }
        }
    }


    public void edificarPiscina(Jugador jugador){
        Piscina piscina =new Piscina(this);
        if(piscina.esEdificablePiscina()){
            if (jugador.getFortuna() >= piscina.getCoste()) {
                this.anhadirEdificio(piscina); // Añadir el edificio a la casilla
                jugador.sumarGastos(piscina.getCoste()); // Restar el coste del edificio
                jugador.sumarFortuna(-piscina.getCoste()); // Reducir la fortuna del jugador
        
                Juego.consola.imprimir("El jugador " + jugador.getNombre() + " ha comprado el edificio " +
                    piscina.getId() + " por " + piscina.getCoste());
                }
                else{
                    Juego.consola.imprimir("No tienes suficiente dinero para edificar.");
                }
        }
    }


    public void edificarPista(Jugador jugador){
        PistaDeporte pista =new PistaDeporte(this);
        if(pista.esEdificablePista()){
            if (jugador.getFortuna() >= pista.getCoste()) {
                this.anhadirEdificio(pista); // Añadir el edificio a la casilla
                jugador.sumarGastos(pista.getCoste()); // Restar el coste del edificio
                jugador.sumarFortuna(-pista.getCoste()); // Reducir la fortuna del jugador
        
                Juego.consola.imprimir("El jugador " + jugador.getNombre() + " ha comprado el edificio " +
                    pista.getId() + " por " + pista.getCoste());
                }
                else{
                    Juego.consola.imprimir("No tienes suficiente dinero para edificar.");
                }
        }
    }

    public void venderCasas(Jugador jugador, int n){
        ArrayList<Edificio> eds= getCasas();
        int tamanho = eds.size();
        if (tamanho >= n){
            float suma = 0.0f;
            Iterator<Edificio> iterator = eds.iterator();
            int count = 0;
                        // Usamos el iterador para eliminar los edificios, motivo nº 01974182347123 de porqé odio java
            while (iterator.hasNext() && count < n) {
                Edificio edificio = iterator.next();
                suma += (edificio.getCoste())/2;
                iterator.remove();
                count++;
            }
            jugador.sumarFortuna(suma);
            Juego.consola.imprimir("El jugador " + jugador.getNombre() + " han vendido " +
                n + " casas en " + this.nombre + ", recibiendo " + suma +
                "€. En la propiedad quedan " + eds.size() + " casas.");
        }
        else if(tamanho==0){
            Juego.consola.imprimir("No puedes vender ninguna propiedad del tipo casa porque no hay ninguna en la casilla "
            + this.nombre);
        }
        else {
            Juego.consola.imprimir("Solamente se pueden vender " + eds.size() + " casas, recibiendo " + (eds.size() > 0 ? (eds.get(0).getCoste() / 2) * eds.size() : 0) + "€.");
        }
    }

    public void venderHoteles(Jugador jugador, int n){
        ArrayList<Edificio> eds= getHoteles();
        int tamanho = eds.size();
        if (tamanho >= n){
            float suma = 0.0f;
            Iterator<Edificio> iterator = eds.iterator();
            int count = 0;
                        // Usamos el iterador para eliminar los edificios, motivo nº 01974182347123 de porqé odio java
            while (iterator.hasNext() && count < n) {
                Edificio edificio = iterator.next();
                suma += (edificio.getCoste())/2;
                iterator.remove();
                count++;
            }
            jugador.sumarFortuna(suma);
            Juego.consola.imprimir("El jugador " + jugador.getNombre() + " han vendido " +
                n + " hoteles en " + this.nombre + ", recibiendo " + suma +
                "€. En la propiedad quedan " + eds.size() + " hoteles.");
        }
        else if(tamanho==0){
            Juego.consola.imprimir("No puedes vender ninguna propiedad del tipo hoteles porque no hay ninguna en la casilla "
            + this.nombre);
        }
        else {
            Juego.consola.imprimir("Solamente se pueden vender " + eds.size() + " hoteles, recibiendo " + (eds.size() > 0 ? (eds.get(0).getCoste() / 2) * eds.size() : 0) + "€.");
        }
    }

    public void venderPiscinas(Jugador jugador, int n){
        ArrayList<Edificio> eds= getPiscinas();
        int tamanho = eds.size();
        if (tamanho >= n){
            float suma = 0.0f;
            Iterator<Edificio> iterator = eds.iterator();
            int count = 0;
                        // Usamos el iterador para eliminar los edificios, motivo nº 01974182347123 de porqé odio java
            while (iterator.hasNext() && count < n) {
                Edificio edificio = iterator.next();
                suma += (edificio.getCoste())/2;
                iterator.remove();
                count++;
            }
            jugador.sumarFortuna(suma);
            Juego.consola.imprimir("El jugador " + jugador.getNombre() + " han vendido " +
                n + " piscinas en " + this.nombre + ", recibiendo " + suma +
                "€. En la propiedad quedan " + eds.size() + " piscinas.");
        }
        else if(tamanho==0){
            Juego.consola.imprimir("No puedes vender ninguna propiedad del tipo casa porque no hay ninguna en la casilla "
            + this.nombre);
        }
        else {
            Juego.consola.imprimir("Solamente se pueden vender " + eds.size() + " piscinas, recibiendo " + (eds.size() > 0 ? (eds.get(0).getCoste() / 2) * eds.size() : 0) + "€.");
        }
    }

    public void venderPistas(Jugador jugador, int n){
        ArrayList<Edificio> eds= getPiscinas();
        int tamanho = eds.size();
        if (tamanho >= n){
            float suma = 0.0f;
            Iterator<Edificio> iterator = eds.iterator();
            int count = 0;
                        // Usamos el iterador para eliminar los edificios, motivo nº 01974182347123 de porqé odio java
            while (iterator.hasNext() && count < n) {
                Edificio edificio = iterator.next();
                suma += (edificio.getCoste())/2;
                iterator.remove();
                count++;
            }
            jugador.sumarFortuna(suma);
            Juego.consola.imprimir("El jugador " + jugador.getNombre() + " han vendido " +
                n + " pistas en " + this.nombre + ", recibiendo " + suma +
                "€. En la propiedad quedan " + eds.size() + " pistas.");
        }
        else if(tamanho==0){
            Juego.consola.imprimir("No puedes vender ninguna propiedad del tipo pista porque no hay ninguna en la casilla "
            + this.nombre);
        }
        else {
            Juego.consola.imprimir("Solamente se pueden vender " + eds.size() + " pistas, recibiendo " + (eds.size() > 0 ? (eds.get(0).getCoste() / 2) * eds.size() : 0) + "€.");
        }
    }


    // Añadir una edificación al solar
    public void anhadirEdificio(Edificio ed) {
        if (ed instanceof Casa) {
            this.edificios.get(0).add(ed);
        } else if (ed instanceof Hotel) {
            this.edificios.get(1).add(ed);
        } else if (ed instanceof Piscina) {
            this.edificios.get(2).add(ed);
        } else if (ed instanceof PistaDeporte) {
            this.edificios.get(3).add(ed);
        }
    }

    // Evaluar alquiler en función de las edificaciones
    public float evaluarAlquiler() {
        float totalAlquiler = alquiler;
        if (this.grupo.esDuenhoGrupo(this.duenho)) {
            totalAlquiler *= 2f;
        }

        // Incrementar el alquiler según las edificaciones
        switch (this.edificios.get(0).size()) {
            case 1 -> totalAlquiler += alquiler * 5f;
            case 2 -> totalAlquiler += alquiler * 15f;
            case 3 -> totalAlquiler += alquiler * 35f;
            case 4 -> totalAlquiler += alquiler * 50f;
            default -> {}
        }

        totalAlquiler += alquiler * 70f * this.edificios.get(1).size(); // Hoteles
        totalAlquiler += alquiler * 25f * (this.edificios.get(2).size() + this.edificios.get(3).size()); // Piscinas y pistas
        return totalAlquiler;
    }

    // Métodos para contar edificaciones específicas
    public int contarCasasSolar() { return getCasas().size(); }
    public int contarHotelesSolar() { return getHoteles().size(); }
    public int contarPiscinasSolar() { return getPiscinas().size(); }
    public int contarPistasSolar() { return getPistasDeDeporte().size(); }

    // Getters para las listas de edificaciones
    public ArrayList<Edificio> getCasas() { return this.edificios.get(0); }
    public ArrayList<Edificio> getHoteles() { return this.edificios.get(1); }
    public ArrayList<Edificio> getPiscinas() { return this.edificios.get(2); }
    public ArrayList<Edificio> getPistasDeDeporte() { return this.edificios.get(3); }


    public String listarEdificaciones() {
        StringBuilder str = new StringBuilder(); // Usar StringBuilder para mayor eficiencia al concatenar cadenas
    
        for (ArrayList<Edificio> tipoEdificio : this.edificios) { // Iterar sobre cada lista de edificios
            if (!tipoEdificio.isEmpty()) { // Verificar si la lista no está vacía
                for (Edificio edificio : tipoEdificio) {
                    str.append(edificio.infoEdificio()).append("\n"); // Añadir información del edificio
                }
            }
        }
    
        return str.toString(); // Convertir StringBuilder a String y retornarlo
    }
    
    public void eliminarTodosLosEds() {
        for (ArrayList<Edificio> lista : this.edificios) {
            lista.clear();
        }
    }

    public void eliminarCasasDeCasilla() {
        this.getCasas().clear();
    }

    @Override
    public String infoCasilla(){
        String info = "{\n";
        info += "\tTipo: Solar\n";
        info += "\tColor del grupo: " + this.grupo.getColorGrupo() + "\n";
        info += "\tDueño: " + this.duenho.getNombre() + "\n";
        info += String.format("\tPrecio: %,.0f€\n", this.valor);
        info += String.format("\tAlquiler: %,.0f€\n", this.alquiler);
        info += String.format("\tHipoteca: %,.0f€\n", this.hipoteca);
        info += String.format("\tPrecio casa: %,.0f€\n", this.valor * 0.60f);
        info += String.format("\tPrecio hotel: %,.0f€\n", this.valor * 0.60f);
        info += String.format("\tPrecio piscina: %,.0f€\n", this.valor * 0.40f);
        info += String.format("\tPrecio pista de deporte: %,.0f€\n", this.valor * 1.25f);
        info += String.format("\tAlquiler de 1 casa: %,.0f€\n", this.alquiler * 5f);
        info += String.format("\tAlquiler de 2 casas: %,.0f€\n", this.alquiler * 15f);
        info += String.format("\tAlquiler de 3 casas: %,.0f€\n", this.alquiler * 35f);
        info += String.format("\tAlquiler de 4 casas: %,.0f€\n", this.alquiler * 50f);
        info += String.format("\tAlquiler hotel: %,.0f€\n", this.alquiler * 70f);
        info += String.format("\tAlquiler piscina: %,.0f€\n", this.alquiler * 25f);
        info += String.format("\tAlquiler pista de deporte: %,.0f€\n", this.alquiler * 25f);
                
        info += "\tEdificios: [";
        for(ArrayList<Edificio> tipo: edificios){
            for(Edificio ed: tipo){
                info+= String.format(ed.getId())+" ";
            }
        }

        info+="]\n";
        info += jugadoresEnCasilla();
        info += "}\n";
        return info;
    }

}
