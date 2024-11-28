package monopoly.edificios;

import java.util.ArrayList;
import monopoly.casillas.propiedades.Solar;

public class PistaDeporte extends Edificio {
    public PistaDeporte(Solar solar) {
        super(solar);
    }

    // =========================================
    // MÉTODOS ESPECÍFICOS
    // =========================================
    public boolean esEdificablePista() {
        int maxPistasG = solar.getGrupo().contarPistasGrupo();
        int maxHotelesC = solar.contarHotelesSolar();
    
        int max = solar.getGrupo().getNumCasillasGrupo();
    
        if (maxPistasG >= max) {
            System.out.println("Has llegado al máximo de pistas de deporte de este grupo.");
            return false;
        }
    
        if (maxHotelesC >= 2) {
            return true;
        } else {
            System.out.println("Para construir una pista de deportes en esta casilla necesitas mínimo dos hoteles.");
            return false;
        }
    }
    

    /**
     * Genera un ID único para la casa en el solar.
     *
     * @return Un ID único para la casa.
     */
    public String generarID() {
        ArrayList<Edificio> edificios = solar.getPistasDeDeporte();
        int maxId = 0;
        
        for (Edificio edificio : edificios) {
            String id = edificio.getId();
            String[] partes = id.split("-");

            // Validar formato del ID y extraer el número
            if (partes.length == 2 && partes[0].equals("pista de deporte")) {
                try {
                    int numero = Integer.parseInt(partes[1]);
                    maxId = Math.max(maxId, numero);
                } catch (NumberFormatException e) {
                    // Ignorar IDs que no sean números válidos
                }
            }
        }

        // Generar un nuevo ID incrementado
        return "pista de deporte-" + (maxId + 1);
    }


    @Override
    protected float calcularCoste(float valorGrupo) {
        return valorGrupo * 1.25f;
    }
}
