package monopoly.edificios;

import java.util.ArrayList;
import monopoly.casillas.propiedades.Solar;

import monopoly.Juego;

public class Piscina extends Edificio {
    public Piscina(Solar solar) {
        super(solar);
    }

    // =========================================
    // MÉTODOS ESPECÍFICOS
    // =========================================



   

    public boolean esEdificablePiscina() {
        int maxPiscinasG = solar.getGrupo().contarPiscinasGrupo();
        int maxHotelesC = solar.contarHotelesSolar();
        int maxCasasC = solar.contarCasasSolar();
    
        int max = solar.getGrupo().getNumCasillasGrupo();
    
        if (maxPiscinasG >= max) {
            Juego.consola.imprimir("Has llegado al máximo de piscinas de este grupo.");
            return false;
        }
    
        if (maxHotelesC >= 1 && maxCasasC >= 2) {
            return true;
        } else {
            Juego.consola.imprimir("Para construir una piscina en esta casilla necesitas mínimo un hotel y dos casas.");
            return false;
        }
    }
    


    /**
     * Genera un ID único para la casa en el solar.
     *
     * @return Un ID único para la casa.
     */
    @Override
    public String generarID() {
        ArrayList<Edificio> edificios = solar.getPiscinas();
        int maxId = 0;
        
        for (Edificio edificio : edificios) {
            String id = edificio.getId();
            String[] partes = id.split("-");

            // Validar formato del ID y extraer el número
            if (partes.length == 2 && partes[0].equals("piscina")) {
                try {
                    int numero = Integer.parseInt(partes[1]);
                    maxId = Math.max(maxId, numero);
                } catch (NumberFormatException e) {
                    // Ignorar IDs que no sean números válidos
                }
            }
        }

        // Generar un nuevo ID incrementado
        return "piscina-" + (maxId + 1);
    }




    @Override
    protected float calcularCoste(float valorGrupo) {
        return valorGrupo * 0.40f;
    }
}
