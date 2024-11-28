package monopoly.casillas.propiedades;

import java.util.*;

import monopoly.*;
import partida.*;
import monopoly.edificios.*;
import monopoly.edificios.*;

public class Solar extends Propiedad{
    private ArrayList<ArrayList<Edificio>> edificios;

    protected Grupo grupo;

    public Solar(String nombre,int posicion, float valor){
        super(nombre,posicion,valor);
        this.grupo=null;
        this.edificios = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            this.edificios.add(new ArrayList<Edificio>()); // Array de casas, hoteles, piscinas, pistas
        }
    }


    public int contarTipoPropiedadesGrupos(String tipo) {
        int index = getTipoIndex(tipo); // Obtener el índice del tipo de edificación
        if (index == -1) {
            return 0; // Retornar 0 si el tipo es inválido
        }
        int contador = 0;
        // Contar todas las edificaciones del tipo especificado en cada casilla del grupo
        for (Propiedad c : grupo.getMiembrosGrupo()) {
            contador += c.contarTipoPropiedadesCasilla(tipo); // Usar la función para contar en cada casilla
        }
        return contador;
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


    public float calcularAlquiler(){
        return valor * Valor.FACTOR_ALQUILER_SOLAR;
    }

    /**Método para evaluar lo que hay que pagar en una casilla de tipo Transporte o Solar.*/
    public float evaluarAlquiler() {
        // Precio base del alquiler de cualquier casilla
        float totalAlquiler = alquiler;
        // Si es dueño del grupo se duplica el precio base del alquiler del solar
        if (this.grupo.esDuenhoGrupo(this.duenho)){
            totalAlquiler *= 2f;
        }
        // Sumamos al total el precio de las casas si las hubiera
        // En el ArrayList de edificios la posición 0 es el ArrayList de las casas :)
        switch(this.edificios.get(0).size()) {
            case 0: break;
            case 1: totalAlquiler += alquiler * 5f; break;
            case 2: totalAlquiler += alquiler * 15f; break;
            case 3: totalAlquiler += alquiler * 35f; break;
            case 4: totalAlquiler += alquiler * 50f; break;
            default:
                System.out.println("Cómo pudiste edificar más de 4 casas en un Solar meu...");
        }

        // Sumamos al total el precio de los hoteles, piscinas y pistas de deporte
        // (posiciones 1,2,3 en el ArrayList de edificios respectivamente)
        totalAlquiler += alquiler * 70f * this.edificios.get(1).size();
        totalAlquiler += alquiler * 25f * ( this.edificios.get(2).size() + this.edificios.get(3).size() );

        return totalAlquiler;
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

}
