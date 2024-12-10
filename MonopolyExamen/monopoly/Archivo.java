package monopoly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Archivo {

    private String rutaArchivo;

    // Constructor que inicializa la ruta del archivo
    public Archivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    // Función para leer la línea n del archivo
    public String leerLinea(int n) {
        try (InputStream is = getClass().getResourceAsStream(rutaArchivo)) {
            if (is == null) {
                System.out.println("Archivo no encontrado.");
                return "fin";
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String linea;
            int contador = 0;

            while ((linea = br.readLine()) != null) {
                contador++;
                if (contador == n) {
                    return linea;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return "fin";
    }

    // Función para contar el número total de líneas del archivo
    public int contarLineas() {
        int totalLineas = 0;

        try (InputStream is = getClass().getResourceAsStream(rutaArchivo)) {
            if (is == null) {
                System.out.println("Archivo no encontrado.");
                return 0;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (br.readLine() != null) {
                totalLineas++;
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        return totalLineas;
    }

    // Función para depurar una línea según las reglas dadas
    public String depurarLinea(String linea) {
        if (linea == null || linea.isEmpty()) {
            return ""; // Retorna vacío si la línea es nula o vacía
        }

        // Eliminar todo hasta '>' (incluido)
        int index = linea.indexOf('>');
        if (index != -1) {
            linea = linea.substring(index + 1);
        }

        // Quitar espacios iniciales
        linea = linea.stripLeading();

        // Si encuentra "lanzar", procesar
        if (linea.startsWith("lanzar")) {
            linea = linea.replace("lanzar", "").stripLeading(); // Quitar "lanzar" y espacios siguientes
            linea = linea.replace("(", "").replace(")", ""); // Quitar paréntesis
            linea = linea.replace("+", " "); // Sustituir '+' por un espacio
        }

        // Si encuentra "seleccionar", devolver "pausa"


        // Si encuentra "avanzar", devolver "siguiente"
        if (linea.startsWith("avanzar")) {
            return "siguiente";
        }

        return linea; // Retorna la línea depurada
    }
}
