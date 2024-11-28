package monopoly.casillas.propiedades;

import java.util.ArrayList;
import monopoly.*;
import monopoly.edificios.*;

public class Solar extends Propiedad {
    private ArrayList<ArrayList<Edificio>> edificios;
    protected Grupo grupo;
    protected int vecesVisitadaPorDuenho;

    // Constructor
    public Solar(String nombre, int posicion) {
        super(nombre, posicion);
        this.grupo = null;
        this.edificios = new ArrayList<>(4);
        this.vecesVisitadaPorDuenho = 0;
        for (int i = 0; i < 4; i++) {
            this.edificios.add(new ArrayList<>()); // Array de casas, hoteles, piscinas, pistas
        }
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
            System.out.println("El grupo no puede ser nulo.\n");
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
    

}
