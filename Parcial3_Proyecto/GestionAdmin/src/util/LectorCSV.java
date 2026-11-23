package util;
import excepciones.ArchivoInvalidoException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.MateriaNoEncontradaException;
import excepciones.PreRequisitoNoAprobadoException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import modelo.Universidad;

public class LectorCSV {
    
    public static void procesar(String rutaArchivo, Universidad uni) throws ArchivoInvalidoException {
        Queue<String[]> colaSolicitudes = new LinkedList<>();
        int exitosas = 0;
        int fallidas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numLinea = 0;
            while ((linea = br.readLine()) != null) {
                numLinea++;
                if (linea.trim().isEmpty()) 
                    continue;
                
                String[] datos = linea.trim().split(",");
                if (datos.length != 2) {
                    throw new ArchivoInvalidoException("Linea " + numLinea + " con formato invalido: " + linea);
                }
                colaSolicitudes.add(datos); 
            }
        } catch (IOException e) {
            throw new ArchivoInvalidoException("No se pudo leer el archivo: " + rutaArchivo);
        }
        
        System.out.println("Se encolaron " + colaSolicitudes.size() + " solicitudes.");
        System.out.println("Procesando cola...");
        
        int contador = 1;
        int total = colaSolicitudes.size();

        while (!colaSolicitudes.isEmpty()) {
            String[] solicitud = colaSolicitudes.poll(); 
            String idEst = solicitud[0].trim();
            String codMat = solicitud[1].trim();

            System.out.print("[" + contador + "/" + total + "] " + idEst + " -> " + codMat + " -> ");

            try {
                if (uni.inscribir(idEst, codMat)) {
                    System.out.println("Exitosa");
                    exitosas++;
                } else {
                    System.out.println("Fallida (Ya estaba inscrito o en cola)");
                    fallidas++;
                }
            } catch (EstudianteNoEncontradoException | PreRequisitoNoAprobadoException 
                    | MateriaNoEncontradaException | CupoLlenoException e) {
                System.out.println("Fallida (" + e.getMessage() + ")");
                fallidas++;
            }
            contador++;
        }

        System.out.println("\n=== Resumen ===");
        System.out.println("Exitosas: " + exitosas);
        System.out.println("Fallidas: " + fallidas);
    }
}