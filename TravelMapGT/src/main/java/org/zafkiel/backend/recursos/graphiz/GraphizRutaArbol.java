package org.zafkiel.backend.recursos.graphiz;
import org.zafkiel.frontend.HomePage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
public class GraphizRutaArbol {


    public void generarArchivoDOT() {
        String contenidoDOT = HomePage.rutaArbolB;


        try {
            // Escribimos el contenido en un archivo DOT
            FileWriter writer = new FileWriter("graphvizRutasArbol.dot");
            writer.write(contenidoDOT);
            writer.close();
            System.out.println("Archivo DOT graphvizRutas.dot generado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo DOT: " + e.getMessage());
        }
    }

    public void generarImagenDesdeDOT() {
        try {
            // Ejecutamos el comando de Graphviz para generar la imagen desde el archivo DOT
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", "graphvizRutasArbol.dot", "-o", "graphvizRutasArbol.png");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Imagen graphvizRutas.png generada correctamente.");
            } else {
                System.err.println("Error al generar la imagen. Código de salida: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al generar la imagen: " + e.getMessage());
        }
    }
}
