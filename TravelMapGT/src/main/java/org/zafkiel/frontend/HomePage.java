package org.zafkiel.frontend;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class HomePage extends JFrame {
    public JPanel panelHomePage;
    private JButton SALIRButton;
    private JLabel labelHoraActual;
    private JButton botonHoraMas;
    private JButton botonMinutoMas;
    private JButton botonHoraMenos;
    private JButton botonMinutoMenos;
    private JButton botonReiniciarHora;
    private JButton botonBloquearHora;
    private JButton botonDesbloquearHora;
    private JButton botonCargarRutas;
    private JButton botonCargarTrafico;
    private JLabel labelRutas;
    private JLabel labelTrafico;
    private JComboBox comboBoxInicio;
    private JComboBox comboBoxFin;
    private JComboBox comboBosMetodoDesplazamiento;
    private JButton botonLimpiarArchivos;
    private JButton botonCancelarViaje;
    private JButton botonNuevoViaje;
    private JButton botonIniciarViaje;
    private JTabbedPane panelResultados;
    private JTable tablaRutasDebugger;
    private JPanel panelRuta;
    private JPanel panelArbol;
    private JPanel panelDebugger;
    private JPanel panelReportes;

    ArrayList<String[]> datos = new ArrayList<>();

    //--- VARIABLES DEL RELOJ ---
    private int hora, minutos, segundos;
    public HomePage() {
        //--- Deshabilitamos funciones ---
        botonDesbloquearHora.setEnabled(false);
        botonCargarTrafico.setEnabled(false);
        //--- Iniciar Reloj ---
        reCalcula();
        actualizarHora();
        //--- Iniciamos los parametros ---
        DefaultTableModel modelo1 = new DefaultTableModel();
        modelo1.addColumn(" Origen ");
        modelo1.addColumn(" Destino ");
        modelo1.addColumn(" Tiempo_Vehiculo ");
        modelo1.addColumn(" Tiempo_Pie ");
        modelo1.addColumn(" Consumo_Gas ");
        modelo1.addColumn(" Desgaste_Persona ");
        modelo1.addColumn(" Distancia ");
        modelo1.addColumn(" Trafico_H_Inicio ");
        modelo1.addColumn(" Trafico_H_Fin ");
        modelo1.addColumn(" Trafico_Prob ");
        tablaRutasDebugger.setModel(modelo1);

        SALIRButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        botonReiniciarHora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reCalcula();
                actualizarLabelHora();
            }
        });
        botonHoraMas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hora++;
                if (hora == 24) {
                    hora = 0;
                }
                actualizarLabelHora();
            }
        });
        botonHoraMenos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hora--;
                if (hora < 0) {
                    hora = 23;
                }
                actualizarLabelHora();
            }
        });
        botonMinutoMas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minutos++;
                if (minutos == 60) {
                    minutos = 0;
                    hora++;
                    if (hora == 24) {
                        hora = 0;
                    }
                    actualizarLabelHora();
                }
                actualizarLabelHora();
            }
        });
        botonMinutoMenos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minutos--;
                if (minutos < 0) {
                    minutos = 59;
                    hora--;
                    if (hora < 0) {
                        hora = 23;
                    }
                    actualizarLabelHora();
                }
                actualizarLabelHora();
            }
        });
        botonBloquearHora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bloquearControles();
            }
        });
        botonDesbloquearHora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                desbloquearControles();
            }
        });
        botonCargarRutas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                // Establecer filtro para mostrar solo archivos XML
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos CSV", "csv");
                fileChooser.setFileFilter(filter);

                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (selectedFile.getName().endsWith(".csv")) {
                        try {
                            StringBuilder content = new StringBuilder();
                            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                content.append(line).append("\n");
                            }
                            reader.close();
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                                String linea;
                                datos.clear();

                                while ((linea = br.readLine()) != null) {
                                    String[] valores = linea.split("\\|");
                                    if(valores.length ==7 ){
                                        for (int i = 0; i < valores.length; i++) {
                                            valores[i] = valores[i].trim();
                                        }
                                        for (int i = 0; i < 3; i++) {

                                            valores = Arrays.copyOf(valores, valores.length + 1);
                                            valores[valores.length - 1] = "0";
                                        }
                                        labelRutas.setText(selectedFile.getName());
                                        datos.add(valores);
                                    }else {
                                        br.close();
                                        labelRutas.setText("Sin Rutas");
                                        labelTrafico.setText("Sin Trafico");
                                        botonCargarTrafico.setEnabled(false);
                                        JOptionPane.showMessageDialog(null, "Archivo de Carga de Rutas INCORRECTO", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    }

                                }
                                br.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                            DefaultTableModel modeloTabla = (DefaultTableModel) tablaRutasDebugger.getModel();
                            modeloTabla.setRowCount(0);
                            for (String[] fila : datos) {
                                modeloTabla.addRow(fila);
                            }
                            tablaRutasDebugger.setModel(modeloTabla);
                            botonCargarTrafico.setEnabled(true);
                            for (int i = 0; i < datos.size(); i++) {
                                String[] fila = datos.get(i);
                                if (fila[0].equals("Quetzaltenango") && fila[1].equals("Sumpango")) {
                                    System.out.println("El valor de la tercera columna es: " + fila[2] + " en la fila " + i);
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, selecciona un archivo CSV.", "Error", JOptionPane.ERROR_MESSAGE);

                    }
                }
            }
        });
        botonCargarTrafico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        botonLimpiarArchivos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonCargarTrafico.setEnabled(false);
                datos.clear();
                DefaultTableModel modeloTabla = (DefaultTableModel) tablaRutasDebugger.getModel();
                modeloTabla.setRowCount(0);
                labelRutas.setText("Sin Rutas");
                labelTrafico.setText("Sin Trafico");
            }
        });
        botonIniciarViaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarArchivoDOT();
                generarImagenDesdeDOT();
            }
        });
    }

    //--- Método para calcular la hora actual ---
    private void reCalcula() {
        Calendar calendario = new GregorianCalendar();
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
    }

    //--- Método para actualizar la etiqueta de la hora ---
    private void actualizarLabelHora() {
        labelHoraActual.setText(String.format("%02d:%02d:%02d", hora, minutos, segundos));
    }

    //--- Método para bloquear los controles ---
    private void bloquearControles() {

        botonDesbloquearHora.setEnabled(true);
        botonReiniciarHora.setEnabled(false);
        botonHoraMas.setEnabled(false);
        botonHoraMenos.setEnabled(false);
        botonMinutoMas.setEnabled(false);
        botonMinutoMenos.setEnabled(false);
        botonBloquearHora.setEnabled(false);
    }

    //--- Método para desbloquear los controles ---
    private void desbloquearControles() {
        botonBloquearHora.setEnabled(true);
        botonReiniciarHora.setEnabled(true);
        botonHoraMas.setEnabled(true);
        botonHoraMenos.setEnabled(true);
        botonMinutoMas.setEnabled(true);
        botonMinutoMenos.setEnabled(true);
        botonDesbloquearHora.setEnabled(false);
    }

    //--- Método para actualizar la hora cada segundo ---
    private void actualizarHora() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                segundos++;
                if (segundos == 60) {
                    segundos = 0;
                    minutos++;
                    if (minutos == 60) {
                        minutos = 0;
                        hora++;
                        if (hora == 24) {
                            hora = 0;
                        }
                    }
                }
                actualizarLabelHora();
            }
        });
        timer.start();
    }

    //---  Graphiz ---
    public static void generarArchivoDOT() {
        String contenidoDOT = "digraph finite_state_machine {\n" +
                "\tfontname=\"Helvetica,Arial,sans-serif\"\n" +
                "\tnode [fontname=\"Helvetica,Arial,sans-serif\"]\n" +
                "\tedge [fontname=\"Helvetica,Arial,sans-serif\"]\n" +
                "\trankdir=LR;\n" +
                "\tnode [shape = doublecircle]; Ciudad Ciudad2;\n" +
                "\tnode [shape = circle];\n" +
                "\tCiudad -> Ciudad2 [label = \"SS(B)\"];\n" +
                "}";

        try {
            // Escribimos el contenido en un archivo DOT
            FileWriter writer = new FileWriter("graphvizRutas.dot");
            writer.write(contenidoDOT);
            writer.close();
            System.out.println("Archivo DOT generado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo DOT: " + e.getMessage());
        }
    }

    public static void generarImagenDesdeDOT() {
        try {
            // Ejecutamos el comando de Graphviz para generar la imagen desde el archivo DOT
            ProcessBuilder processBuilder = new ProcessBuilder("dot", "-Tpng", "graphvizRutas.dot", "-o", "graphvizRutas.png");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Imagen generada correctamente.");
            } else {
                System.err.println("Error al generar la imagen. Código de salida: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al generar la imagen: " + e.getMessage());
        }
    }

}
