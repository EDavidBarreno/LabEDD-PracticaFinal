package org.zafkiel.backend.recursos.graphiz;

import org.zafkiel.frontend.HomePage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GraphizRutaCaminando {
    ArrayList<String[]> datos2 = new ArrayList<>();

    public void  procesarArrayList(ArrayList<String[]> datos) {

        datos2 = new ArrayList<>(datos);
    }

    public void generarArchivoDOT() {
        String ciudades = "";
        String dobleCirculo = new String(HomePage.colorearRutaCaminando);

        for (String[] fila : datos2) {
            if (fila.length >= 6) {
                ciudades +="\t"+ fila[0]+" -- "+ fila[1]+" [label = \""+ fila[6]+"KM\"];\n"  ;
            }
        }

        String contenidoDOT = "graph  rutas_completas {\n" +
                "\tfontname=\"Helvetica,Arial,sans-serif\"\n" +
                "\tnode [fontname=\"Helvetica,Arial,sans-serif\"]\n" +
                "\tedge [fontname=\"Helvetica,Arial,sans-serif\"]\n" +
                "\trankdir=LR;\n" +
                "\tnode [shape = doublecircle, style=filled, color=hotpink];\n"+dobleCirculo+
                "\tnode [shape = circle, color=lightgreen];\n" +
                ciudades +
                "}";


        try {
            // Escribimos el contenido en un archivo DOT
            FileWriter writer = new FileWriter("graphvizRutasCaminando.dot");
            writer.write(contenidoDOT);
            writer.close();
            System.out.println("Archivo DOT graphvizRutasCaminando.dot generado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo DOT: " + e.getMessage());
        }
    }

    public void generarImagenDesdeDOT() {
        try {
            // Ejecutamos el comando de Graphviz para generar la imagen desde el archivo DOT
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", "graphvizRutasCaminando.dot", "-o", "graphvizRutasCaminando.png");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Imagen graphvizRutasCaminando.png generada correctamente.");
            } else {
                System.err.println("Error al generar la imagen. Código de salida: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al generar la imagen: " + e.getMessage());
        }
    }
}
