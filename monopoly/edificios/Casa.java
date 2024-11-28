package monopoly.edificios;

import java.util.ArrayList;
import monopoly.casillas.propiedades.Solar;

/**
 * Clase que representa una Casa, un tipo de Edificio en un Solar.
 */
public class Casa extends Edificio {

    // =========================================
    // CONSTRUCTOR
    // =========================================
    /**
     * Constructor para crear una Casa en un Solar.
     *
     * @param solar El solar donde se construirá la casa.
     */
    public Casa(Solar solar) {
        super(solar);
    }

    // =========================================
    // MÉTODOS ESPECÍFICOS
    // =========================================

    public boolean esEdificableCasa() {
        // Obtiene el número máximo de casas y hoteles en el grupo y en la casilla actual
        int maxHotelesGrupo = solar.getGrupo().contarHotelesGrupo();
        int maxCasasGrupo = solar.getGrupo().contarCasasGrupo();
        int maxCasasSolar = solar.contarCasasSolar();
    
        // Verifica las condiciones del grupo
        int maxPorGrupo = this.getSolar().getGrupo().getNumCasillasGrupo();
    
        if (this.getSolar().getGrupo().estaHipotecadoGrupo()) {
            System.out.println("No puedes edificar porque alguna propiedad del grupo está hipotecada.");
            return false;
        }
    
        if (maxHotelesGrupo >= maxPorGrupo) {
            if (maxCasasGrupo >= maxPorGrupo) {
                System.out.println("Llegaste al máximo de casas en este grupo.");
                return false;
            }
            return true; // Puede edificar más casas en el grupo
        }
    
        if (maxCasasSolar >= 4) {
            System.out.println("Debes edificar un hotel, tienes 4 casas en esta casilla.");
            return false;
        }
    
        return true; // Puede edificar casas
    }




    /**
     * Genera un ID único para la casa en el solar.
     *
     * @return Un ID único para la casa.
     */
    public String generarID() {
        ArrayList<Edificio> edificios = solar.getCasas();
        int maxId = 0;

        for (Edificio edificio : edificios) {
            String id = edificio.getId();
            String[] partes = id.split("-");

            // Validar formato del ID y extraer el número
            if (partes.length == 2 && partes[0].equals("casa")) {
                try {
                    int numero = Integer.parseInt(partes[1]);
                    maxId = Math.max(maxId, numero);
                } catch (NumberFormatException e) {
                    // Ignorar IDs que no sean números válidos
                }
            }
        }

        // Generar un nuevo ID incrementado
        return "casa-" + (maxId + 1);
    }

    /**
     * Calcula el coste de construcción de una Casa basado en el valor del grupo.
     *
     * @param valorGrupo El valor del grupo al que pertenece el solar.
     * @return El coste calculado.
     */
    @Override
    protected float calcularCoste(float valorGrupo) {
        return valorGrupo * 0.60f; // Una casa cuesta el 60% del valor del grupo
    }
}
