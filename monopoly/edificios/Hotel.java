package monopoly.edificios;

import java.util.ArrayList;
import monopoly.casillas.propiedades.Solar;

public class Hotel extends Edificio {
    public Hotel(Solar solar) {
        super(solar);
    }


    // =========================================
    // MÉTODOS ESPECÍFICOS
    // =========================================

    public boolean esEdificableHotel() {
        int maxHotelesG = solar.getGrupo().contarHotelesGrupo();
        int maxCasasG = solar.getGrupo().contarCasasGrupo();
        int maxCasasC = solar.contarCasasSolar();
        int max = solar.getGrupo().getNumCasillasGrupo();
    
        if (solar.getGrupo().estaHipotecadoGrupo()) {
            System.out.println("No puedes edificar en un grupo hipotecado.");
            return false;
        }

    
        if (maxHotelesG >= max) {
            System.out.println("Has llegado al máximo de hoteles de este grupo.");
            return false;
        }
    
        if (maxHotelesG == max - 1 && maxCasasG - 4 > max) {
            System.out.println("No puedes edificar un hotel, debes vender alguna casa de tu grupo primero.");
            return false;
        }
    
        if (maxCasasC < 4) {
            System.out.println("Debes tener al menos 4 casas en este solar para poder edificar un hotel.");
            return false;
        }
    
        return true;
    }
    






    /**
     * Genera un ID único para la casa en el solar.
     *
     * @return Un ID único para la casa.
     */
    @Override
    public String generarID() {
        ArrayList<Edificio> edificios = solar.getHoteles();
        int maxId = 0;
        
        for (Edificio edificio : edificios) {
            String id = edificio.getId();
            String[] partes = id.split("-");

            // Validar formato del ID y extraer el número
            if (partes.length == 2 && partes[0].equals("hotel")) {
                try {
                    int numero = Integer.parseInt(partes[1]);
                    maxId = Math.max(maxId, numero);
                } catch (NumberFormatException e) {
                    // Ignorar IDs que no sean números válidos
                }
            }
        }

        // Generar un nuevo ID incrementado
        return "hotel-" + (maxId + 1);
    }

    @Override
    protected float calcularCoste(float valorGrupo) {
        return valorGrupo * 0.60f;
    }
}
