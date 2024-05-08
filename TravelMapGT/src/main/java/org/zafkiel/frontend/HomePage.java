package org.zafkiel.frontend;
import org.zafkiel.backend.edd.ManejadorRutas;
import org.zafkiel.backend.recursos.graphiz.GraphizRutaCompleta;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

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
    private JComboBox comboBoxRutaInicio;
    private JComboBox comboBoxRutaFin;
    private JComboBox comboBoxTIpoDesplazamiento;
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
    private JLabel labelMostrarRutaCompleta;
    private JButton botonMapaMas;
    private JButton botonMapaMenos;
    private JPanel panelViajero;
    private JComboBox comboBoxSiguienteParada;
    private JButton botonAvanzar;
    private JLabel labelRutaOrigen;
    private JLabel labelRutaFinal;
    private JLabel labelDistanciaTotal;
    private JLabel labelHoraInicio;
    private JLabel labelHoraTotal;
    private JLabel labelRutaActual;
    private JLabel labelDesgasteAcumulado;


    public static ArrayList<String[]> datos = new ArrayList<>();
    List<Nodo> nodos = new ArrayList<>();
    ImageIcon icono;
    private int horaBloquear, minutosBloquear, segundosBloquear;
    private int horaRutaInicio, minutosRutaInicio, segundosRutaInicio;
    private int horaRutaFinal, minutosRutaFinal, segundosRutaFinal;

    //--- VARIABLES DEL RELOJ ---
    private int hora, minutos, segundos;
    public HomePage() {
        //--- Deshabilitamos funciones ---
        comboBoxRutaInicio.setEnabled(false);
        comboBoxRutaFin.setEnabled(false);
        comboBoxTIpoDesplazamiento.setEnabled(false);
        botonDesbloquearHora.setEnabled(false);
        botonCargarTrafico.setEnabled(false);
        botonCancelarViaje.setEnabled(false);
        botonNuevoViaje.setEnabled(false);
        botonIniciarViaje.setEnabled(false);
        comboBoxSiguienteParada.setEnabled(false);
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
                                HashSet<String> combinacionesUnicas = new HashSet<>();
                                while ((linea = br.readLine()) != null) {
                                    String[] valores = linea.split("\\|");
                                    if(valores.length ==7 ){
                                        for (int i = 0; i < valores.length; i++) {
                                            valores[i] = valores[i].trim();

                                        }
                                        String combinacion = valores[1] + "|" + valores[2];
                                        if (combinacionesUnicas.contains(combinacion)) {
                                            for (int i = 0; i < datos.size(); i++) {
                                                String[] fila = datos.get(i);
                                                if (fila[1].equals(valores[1]) && fila[2].equals(valores[2])) {
                                                    datos.set(i, valores);
                                                    break;
                                                }
                                            }
                                        }else {
                                            // La combinación no existe, agregarla al conjunto de combinaciones únicas
                                            combinacionesUnicas.add(combinacion);
                                            // Agregar los valores al array datos
                                            for (int i = 0; i < 3; i++) {
                                                valores = Arrays.copyOf(valores, valores.length + 1);
                                                valores[valores.length - 1] = "0";
                                            }
                                            datos.add(valores);
                                        }

                                        labelRutas.setText(selectedFile.getName());

                                    }else {
                                        labelRutas.setText("Sin Rutas");
                                        labelTrafico.setText("Sin Trafico");
                                        botonCargarTrafico.setEnabled(false);
                                        labelMostrarRutaCompleta.setIcon(null);
                                        DefaultTableModel modeloTabla = (DefaultTableModel) tablaRutasDebugger.getModel();
                                        modeloTabla.setRowCount(0);
                                        JOptionPane.showMessageDialog(null, "Archivo de Carga de Rutas INCORRECTO", "ERROR", JOptionPane.ERROR_MESSAGE);
                                        return;
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
                            //--- Mandamos el Array list a dibujar ---

                            GraphizRutaCompleta graphizRutaCompleta = new GraphizRutaCompleta();
                            graphizRutaCompleta.procesarArrayList(datos);
                            graphizRutaCompleta.generarArchivoDOT();
                            graphizRutaCompleta.generarImagenDesdeDOT();

                            //--- LLenamos el Origen ---
                            comboBoxRutaInicio.setEnabled(true);
                            ManejadorRutas.llenarRutasOrigen();
                            for (String[] dato : datos) {
                                Nodo origen = buscarOCrearNodo(nodos, dato[0]);
                                Nodo destino = buscarOCrearNodo(nodos, dato[1]);
                                origen.agregarVecino(destino);
                            }
                            comboBoxRutaInicio.removeAllItems();
                            comboBoxRutaInicio.addItem("---   Seleccione una Opcion   ---");
                            for (Nodo nodo : nodos) {
                                comboBoxRutaInicio.addItem(nodo.getId());
                            }

                            /*comboBoxRutaInicio.removeAllItems();
                            comboBoxRutaInicio.addItem("---   Seleccione una Opcion   ---");
                            for (String elemento : ManejadorRutas.rutaOrigenOrdenada) {
                                comboBoxRutaInicio.addItem(elemento);
                            }*/
                            //--- Llenamos el Destino ---

                            //--- Ponemos la Imagen General ---
                            icono = new ImageIcon("graphvizRutas.png");
                            labelMostrarRutaCompleta.setIcon(icono);

                            /*for (int i = 0; i < datos.size(); i++) {
                                String[] fila = datos.get(i);
                                if (fila[0].equals("Quetzaltenango") && fila[1].equals("Sumpango")) {
                                    System.out.println("El valor de la tercera columna es: " + fila[2] + " en la fila " + i);
                                }
                            }*/
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

        comboBoxRutaInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOrigenId = (String) comboBoxRutaInicio.getSelectedItem();
                comboBoxRutaFin.removeAllItems();


                Nodo selectedOrigen = null;
                for (Nodo nodo : nodos) {
                    if (nodo.getId().equals(selectedOrigenId)) {
                        selectedOrigen = nodo;
                        break;
                    }
                }

                if (selectedOrigen == null) {
                    comboBoxRutaFin.addItem("---   Seleccione una Opcion   ---");
                    comboBoxRutaFin.setEnabled(false);
                } else {
                    comboBoxRutaFin.setEnabled(true);
                    comboBoxRutaFin.addItem("---   Seleccione una Opcion   ---");
                    for (Nodo vecino : selectedOrigen.getVecinos()) {
                        comboBoxRutaFin.addItem(vecino.getId());
                    }
                }

            }
        });
        comboBoxTIpoDesplazamiento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOrigenId = (String) comboBoxTIpoDesplazamiento.getSelectedItem();

                if (selectedOrigenId == null) {
                    botonIniciarViaje.setEnabled(false);
                } else {
                    if (selectedOrigenId=="---   Seleccione una Opcion   ---") {
                        botonIniciarViaje.setEnabled(false);
                    }else {
                        botonIniciarViaje.setEnabled(true);
                    }
                }
            }
        });
        comboBoxRutaFin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOrigenId = (String) comboBoxRutaFin.getSelectedItem();
                comboBoxTIpoDesplazamiento.removeAllItems();

                if (selectedOrigenId == null) {
                    comboBoxTIpoDesplazamiento.removeAllItems();
                    comboBoxTIpoDesplazamiento.addItem("---   Seleccione una Opcion   ---");
                    comboBoxTIpoDesplazamiento.setEnabled(false);
                } else {
                    if (selectedOrigenId=="---   Seleccione una Opcion   ---") {
                        comboBoxTIpoDesplazamiento.removeAllItems();
                        comboBoxTIpoDesplazamiento.addItem("---   Seleccione una Opcion   ---");
                        comboBoxTIpoDesplazamiento.setEnabled(false);
                    }else {
                        comboBoxTIpoDesplazamiento.setEnabled(true);
                        comboBoxTIpoDesplazamiento.removeAllItems();
                        comboBoxTIpoDesplazamiento.addItem("---   Seleccione una Opcion   ---");
                        comboBoxTIpoDesplazamiento.addItem("Vehiculo");
                        comboBoxTIpoDesplazamiento.addItem("Caminando");
                    }
                }
            }
        });
        botonMapaMas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Image image = icono.getImage();
                Image newImage = image.getScaledInstance(
                        (int) (labelMostrarRutaCompleta.getWidth() * 1.1),
                        (int) (labelMostrarRutaCompleta.getHeight() * 1.1),
                        Image.SCALE_SMOOTH);
                icono = new ImageIcon(newImage);
                labelMostrarRutaCompleta.setIcon(icono);
            }
        });
        botonMapaMenos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Image image = icono.getImage();
                Image newImage = image.getScaledInstance(
                        (int) (labelMostrarRutaCompleta.getWidth() * 0.9),
                        (int) (labelMostrarRutaCompleta.getHeight() * 0.9),
                        Image.SCALE_SMOOTH);
                icono = new ImageIcon(newImage);
                labelMostrarRutaCompleta.setIcon(icono);
            }
        });
        botonNuevoViaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        botonIniciarViaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labelRutaOrigen.setText((String) comboBoxRutaInicio.getSelectedItem());
                labelRutaFinal.setText((String) comboBoxRutaFin.getSelectedItem());
                botonCancelarViaje.setEnabled(true);
                comboBoxRutaInicio.setEnabled(false);
                comboBoxRutaFin.setEnabled(false);
                comboBoxTIpoDesplazamiento.setEnabled(false);
                String selectedOrigenId = labelRutaOrigen.getText();
                comboBoxSiguienteParada.removeAllItems();


                Nodo selectedOrigen = null;
                for (Nodo nodo : nodos) {
                    if (nodo.getId().equals(selectedOrigenId)) {
                        selectedOrigen = nodo;
                        break;
                    }
                }

                if (selectedOrigen == null) {
                    comboBoxSiguienteParada.addItem("---   Seleccione una Opcion   ---");
                    comboBoxSiguienteParada.setEnabled(false);
                } else {
                    comboBoxSiguienteParada.setEnabled(true);
                    comboBoxSiguienteParada.addItem("---   Seleccione una Opcion   ---");
                    for (Nodo vecino : selectedOrigen.getVecinos()) {
                        comboBoxSiguienteParada.addItem(vecino.getId());
                    }
                }

            }
        });
        botonCancelarViaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        botonAvanzar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOrigenId = (String) comboBoxSiguienteParada.getSelectedItem();
                comboBoxSiguienteParada.removeAllItems();


                Nodo selectedOrigen = null;
                for (Nodo nodo : nodos) {
                    if (nodo.getId().equals(selectedOrigenId)) {
                        selectedOrigen = nodo;
                        break;
                    }
                }

                if (selectedOrigen == null) {
                    comboBoxSiguienteParada.addItem("---   Seleccione una Opcion   ---");
                    comboBoxSiguienteParada.setEnabled(false);
                } else {
                    comboBoxSiguienteParada.setEnabled(true);
                    comboBoxSiguienteParada.addItem("---   Seleccione una Opcion   ---");
                    for (Nodo vecino : selectedOrigen.getVecinos()) {
                        comboBoxSiguienteParada.addItem(vecino.getId());
                    }
                }
                if(labelHoraActual.equals(labelRutaFinal)){
                    JOptionPane.showMessageDialog(null, "SE LLEGO AL DESTINO, VER REPORTES DE ESTADO");
                }
            }
        });
        comboBoxSiguienteParada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
    static class Nodo {
        private String id;
        private List<Nodo> vecinos;

        public Nodo(String id) {
            this.id = id;
            this.vecinos = new ArrayList<>();
        }

        public String getId() {
            return id;
        }

        public List<Nodo> getVecinos() {
            return vecinos;
        }

        public void agregarVecino(Nodo vecino) {
            vecinos.add(vecino);
        }
    }
    private static Nodo buscarOCrearNodo(List<Nodo> nodos, String id) {
        for (Nodo nodo : nodos) {
            if (nodo.getId().equals(id)) {
                return nodo;
            }
        }
        Nodo nuevoNodo = new Nodo(id);
        nodos.add(nuevoNodo);
        return nuevoNodo;
    }

}
