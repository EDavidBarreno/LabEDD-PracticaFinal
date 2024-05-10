package org.zafkiel.frontend;
import org.zafkiel.backend.recursos.graphiz.GraphizRutaArbol;
import org.zafkiel.backend.recursos.graphiz.GraphizRutaCaminando;
import org.zafkiel.backend.recursos.graphiz.GraphizRutaCompleta;
import org.zafkiel.backend.recursos.graphiz.GraphizRutaVehiculo;

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
    private JLabel labelHoraLLegada;
    private JLabel labelMostrarRutaArbolImagen;
    private JLabel labelTipoRuta;
    private JComboBox comboBoxOpcionesRuta;
    private JButton MOSTRARButton;
    private JComboBox comboBoxRutaFinCaminando;
    private JButton botomTimbre;


    public static ArrayList<String[]> datos = new ArrayList<>();
    public static ArrayList<String[]> datos2 = new ArrayList<>();
    public static ArrayList<String[]> datos3 = new ArrayList<>();
    List<Nodo> nodos = new ArrayList<>();
    public static String rutaArbolB;

    ImageIcon icono;
    ImageIcon iconoArbol;
    private int horaRutaInicio, minutosRutaInicio, segundosRutaInicio;
    private int horaRutaFinal, minutosRutaFinal, segundosRutaFinal;
    public static String colorearRutaCaminando = "";
    public static String colorearRutaVehiculo = "";
    int distanciaRecorrida=0;
    int tiempoRecorrido = 0;
    //--- VARIABLES DEL RELOJ ---
    Timer timer;
    private int hora, minutos, segundos;

    public HomePage() {
        //--- Deshabilitamos funciones ---
        comboBoxRutaInicio.setEnabled(false);
        comboBoxRutaFin.setEnabled(false);
        botonDesbloquearHora.setEnabled(false);
        botonCargarTrafico.setEnabled(false);
        botonCancelarViaje.setEnabled(false);
        botonNuevoViaje.setEnabled(false);
        botonIniciarViaje.setEnabled(false);
        comboBoxSiguienteParada.setEnabled(false);
        botonMapaMas.setEnabled(false);
        botonMapaMenos.setEnabled(false);
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
                                datos2.clear();
                                datos.clear();
                                datos3.clear();
                                HashSet<String> combinacionesUnicas = new HashSet<>();
                                while ((linea = br.readLine()) != null) {
                                    String[] valores = linea.split("\\|");
                                    if(valores.length ==7 ){
                                        for (int i = 0; i < valores.length; i++) {
                                            valores[i] = valores[i].trim();

                                        }
                                        String combinacion = valores[1] + "|" + valores[2];
                                        if (combinacionesUnicas.contains(combinacion)) {
                                            for (int i = 0; i < 3; i++) {
                                                valores = Arrays.copyOf(valores, valores.length + 1);
                                                valores[valores.length - 1] = "0";
                                            }
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
                            botonMapaMas.setEnabled(true);
                            botonMapaMenos.setEnabled(true);
                            DefaultTableModel modeloTabla = (DefaultTableModel) tablaRutasDebugger.getModel();
                            modeloTabla.setRowCount(0);
                            datos2=datos;

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
                            //--- Mapa para Vehiculos ---
                            GraphizRutaVehiculo graphizRutaVehiculo = new GraphizRutaVehiculo();
                            graphizRutaVehiculo.procesarArrayList(datos);
                            graphizRutaVehiculo.generarArchivoDOT();
                            graphizRutaVehiculo.generarImagenDesdeDOT();
                            //--- Mapa para Caminar ---
                            GraphizRutaCaminando graphizRutaCaminando = new GraphizRutaCaminando();
                            graphizRutaCaminando.procesarArrayList(datos);
                            graphizRutaCaminando.generarArchivoDOT();
                            graphizRutaCaminando.generarImagenDesdeDOT();

                            //--- LLenamos el Origen ---
                            comboBoxRutaInicio.setEnabled(true);
                            //NOODOSSSSSSSSSSSSSS
                            for (String[] dato : datos) {
                                Nodo origen = buscarOCrearNodo(nodos, dato[0]);
                                Nodo destino = buscarOCrearNodo(nodos, dato[1]);
                                origen.agregarVecino(destino);
                            }
                            for (String[] dato : datos2) {
                                Nodo origen = buscarOCrearNodo(nodos, dato[1]);
                                Nodo destino = buscarOCrearNodo(nodos, dato[0]);
                                origen.agregarVecino(destino);
                            }

                            comboBoxRutaInicio.removeAllItems();
                            comboBoxRutaInicio.addItem("---   Seleccione una Opcion   ---");
                            for (Nodo nodo : nodos) {
                                comboBoxRutaInicio.addItem(nodo.getId());
                            }


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

                JFileChooser fileChooser = new JFileChooser();
                // Establecer filtro para mostrar solo archivos CSV
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos CSV", "csv");
                fileChooser.setFileFilter(filter);

                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (selectedFile.getName().endsWith(".csv")) {
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                            String linea;
                            HashSet<String> combinacionesUnicas = new HashSet<>();
                            while ((linea = br.readLine()) != null) {
                                String[] valores = linea.split("\\|");
                                if (valores.length == 5) {
                                    for (int i = 0; i < valores.length; i++) {
                                        valores[i] = valores[i].trim();
                                    }
                                    for (int a = 0; a < datos.size(); a++) {
                                        String[] fila = datos.get(a);
                                        if (fila[0] != null && fila[1] != null && fila[0].equals(valores[0]) && fila[1].equals(valores[1])) {

                                            labelTrafico.setText(selectedFile.getName());
                                            for (int j = 2; j < valores.length; j++) {
                                                fila[fila.length - valores.length + j] = valores[j];
                                            }
                                            datos.set(a, fila);
                                        }
                                    }
                                } else {
                                    labelTrafico.setText("Sin Trafico");
                                    JOptionPane.showMessageDialog(null, "Archivo de Carga de Trafico INCORRECTO", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                            br.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, selecciona un archivo CSV.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                DefaultTableModel modeloTabla = (DefaultTableModel) tablaRutasDebugger.getModel();
                modeloTabla.setRowCount(0);
                datos2=datos;
                datos3=datos;
                for (String[] fila : datos) {
                    modeloTabla.addRow(fila);
                }
            }
        });
        botonLimpiarArchivos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboBoxRutaFin.removeAllItems();
                comboBoxSiguienteParada.removeAllItems();

                comboBoxRutaInicio.removeAllItems();
                botonCargarTrafico.setEnabled(false);
                botonAvanzar.setEnabled(false);
                botonCancelarViaje.setEnabled(false);
                botonIniciarViaje.setEnabled(false);
                botonNuevoViaje.setEnabled(false);
                datos.clear();
                DefaultTableModel modeloTabla = (DefaultTableModel) tablaRutasDebugger.getModel();
                modeloTabla.setRowCount(0);
                labelRutas.setText("Sin Rutas");
                labelTrafico.setText("Sin Trafico");
                MOSTRARButton.setEnabled(false);
                labelMostrarRutaCompleta.setText("");
                labelRutaFinal.setText("");
                labelRutaActual.setText("");


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

                    // Utilizamos un conjunto para evitar agregar nodos duplicados
                    Set<Nodo> visitados = new HashSet<>();
                    Stack<Nodo> stack = new Stack<>();
                    stack.push(selectedOrigen);

                    while (!stack.isEmpty()) {
                        Nodo actual = stack.pop();
                        visitados.add(actual);

                        // Agregamos los vecinos al comboBoxRutaFin
                        for (Nodo vecino : actual.getVecinos()) {
                            if (!visitados.contains(vecino)) {
                                stack.push(vecino);
                                comboBoxRutaFin.addItem(vecino.getId());
                            }
                        }
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
                        icono = new ImageIcon("graphvizRutas.png");
                        labelMostrarRutaCompleta.setIcon(icono);
                        botonIniciarViaje.setEnabled(false);
                    }else {
                        if(selectedOrigenId==("Vehiculo")) {
                            icono = new ImageIcon("graphvizRutasVehiculo.png");
                            labelMostrarRutaCompleta.setIcon(icono);
                            botonIniciarViaje.setEnabled(true);


                        }else {
                            if(selectedOrigenId=="Caminando") {
                                icono = new ImageIcon("graphvizRutasCaminando.png");
                                labelMostrarRutaCompleta.setIcon(icono);
                                botonIniciarViaje.setEnabled(true);

                            }
                        }
                    }
                }



            }
        });
        comboBoxRutaFin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
                MOSTRARButton.setEnabled(true);
                botonAvanzar.setEnabled(true);
                labelTipoRuta.setText(comboBoxTIpoDesplazamiento.getSelectedItem().toString());
                if(labelTipoRuta.getText()=="----"){
                    comboBoxOpcionesRuta.removeAllItems();
                }else {
                    if(labelTipoRuta.getText()=="Vehiculo"){
                        comboBoxOpcionesRuta.removeAllItems();
                        comboBoxOpcionesRuta.addItem("Mejor ruta en base a Gasolina.");
                        comboBoxOpcionesRuta.addItem("Mejor ruta en base a Distancia.");
                        comboBoxOpcionesRuta.addItem("Mejor ruta en base a Gasolina y Distancia.");
                        comboBoxOpcionesRuta.addItem("Mejor ruta en base a Tiempo y Distancia y Trafico.");
                        comboBoxOpcionesRuta.addItem("Peor ruta en base a Gasolina.");
                        comboBoxOpcionesRuta.addItem("Peor ruta en base a Distancia.");
                        comboBoxOpcionesRuta.addItem("Peor ruta en base a Gasolina y Distancia.");
                        comboBoxOpcionesRuta.addItem("Peor ruta en base a Tiempo y Distancia y Trafico.");

                    }else {
                        if(labelTipoRuta.getText()=="Caminando"){
                            comboBoxOpcionesRuta.removeAllItems();
                            comboBoxOpcionesRuta.addItem("Mejor ruta en base a Desgaste.");
                            comboBoxOpcionesRuta.addItem("Mejor ruta en base a Distancia Caminando.");
                            comboBoxOpcionesRuta.addItem("Mejor ruta en base a Desgaste y Distancia.");
                            comboBoxOpcionesRuta.addItem("Mejor ruta en base a Tiempo y Distancia .");
                            comboBoxOpcionesRuta.addItem("Peor ruta en base a Gasolina.");
                            comboBoxOpcionesRuta.addItem("Peor ruta en base a Distancia.");
                            comboBoxOpcionesRuta.addItem("Peor ruta en base a Gasolina y Distancia.");
                            comboBoxOpcionesRuta.addItem("Peor ruta en base a Tiempo y Distancia y Trafico.");

                        }
                    }
                }


                // Imprime el camino más corto


                horaRutaFinal=0;
                minutosRutaFinal=0;
                segundosRutaFinal=0;
                horaRutaInicio = hora;
                minutosRutaInicio = minutos;
                segundosRutaInicio = segundos;
                labelHoraInicio.setText(Integer.toString(horaRutaInicio)+":"+Integer.toString(minutosRutaInicio)+":"+Integer.toString(segundosRutaInicio));
                labelHoraActual.setText(String.format("%02d:%02d:%02d", hora, minutos, segundos));
                labelRutaOrigen.setText((String) comboBoxRutaInicio.getSelectedItem());
                labelRutaFinal.setText((String) comboBoxRutaFin.getSelectedItem());
                botonCancelarViaje.setEnabled(true);
                comboBoxRutaInicio.setEnabled(false);
                comboBoxRutaFin.setEnabled(false);
                comboBoxTIpoDesplazamiento.setEnabled(false);
                String selectedOrigenId = labelRutaOrigen.getText();
                labelRutaActual.setText(selectedOrigenId);
                comboBoxSiguienteParada.removeAllItems();


                colorearRutaVehiculo = labelRutaOrigen.getText() +" "+labelRutaFinal.getText()+";";
                GraphizRutaVehiculo graphizRutaVehiculo = new GraphizRutaVehiculo();
                graphizRutaVehiculo.procesarArrayList(datos);
                graphizRutaVehiculo.generarArchivoDOT();
                graphizRutaVehiculo.generarImagenDesdeDOT();

                GraphizRutaCaminando graphizRutaCaminando = new GraphizRutaCaminando();
                colorearRutaCaminando = labelRutaOrigen.getText() +" "+labelRutaFinal.getText()+";";
                graphizRutaCaminando.procesarArrayList(datos);
                graphizRutaCaminando.generarArchivoDOT();
                graphizRutaCaminando.generarImagenDesdeDOT();
                String nuevoIcono = "graphvizRutas"+labelTipoRuta.getText()+".png";

                icono = new ImageIcon(nuevoIcono);
                System.out.println(nuevoIcono);
                labelMostrarRutaCompleta.setIcon(icono);




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

                    // Utilizamos un conjunto para evitar agregar nodos duplicados
                    Set<Nodo> visitados = new HashSet<>();
                    Stack<Nodo> stack = new Stack<>();
                    stack.push(selectedOrigen);

                    // Modificamos el ciclo para que solo muestre el siguiente nodo
                    if (selectedOrigen.equals(labelRutaFinal.getText())) {
                        // Si el nodo actual es el destino, lo agregamos al JComboBox
                        comboBoxSiguienteParada.addItem(labelRutaFinal.getText());
                    } else {
                        // Si no es el destino, agregamos todos los nodos vecinos al JComboBox
                        for (Nodo siguienteNodo : selectedOrigen.getVecinos()) {
                            comboBoxSiguienteParada.addItem(siguienteNodo.getId());
                        }
                    }
                }

            }
        });
        botonCancelarViaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,"Nop no sirve el boton");
            }
        });
        botonAvanzar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sumarHoraTrasncurrida= 0;
                sumarHoraTrasncurrida = Integer.parseInt(labelHoraTotal.getText());
                if (sumarHoraTrasncurrida>=60){

                }
                int sumarDistancias = 0;
                for (int i = 0; i < datos.size(); i++) {
                    String[] fila = datos.get(i);
                    if (fila[0].equals(labelRutaActual.getText()) && fila[1].equals(comboBoxSiguienteParada.getSelectedItem().toString())) {
                        String sumarDistanciasString = fila[6];
                        if(labelTipoRuta.getText()=="Vehiculo"){
                            sumarHoraTrasncurrida += Integer.parseInt(fila[2]);
                            minutos += Integer.parseInt(fila[2]);
                        }else {
                            if (labelTipoRuta.getText()=="Vehiculo"){
                                sumarHoraTrasncurrida += Integer.parseInt(fila[3]);
                                minutos += Integer.parseInt(fila[2]);
                            }
                        }
                        sumarDistancias += Integer.parseInt(sumarDistanciasString);
                        labelDistanciaTotal.setText(sumarDistanciasString);
                        JOptionPane.showMessageDialog(null, "El valor es: " + fila[6]);
                    }
                    if (fila[1].equals(labelRutaActual.getText()) && fila[0].equals(comboBoxSiguienteParada.getSelectedItem().toString())) {
                        String sumarDistanciasString = fila[6];
                        if(labelTipoRuta.getText()=="Vehiculo"){
                            sumarHoraTrasncurrida += Integer.parseInt(fila[2]);
                            minutos += Integer.parseInt(fila[2]);
                        }else {
                            if (labelTipoRuta.getText()=="Vehiculo"){
                                sumarHoraTrasncurrida += Integer.parseInt(fila[3]);
                                minutos += Integer.parseInt(fila[2]);
                            }
                        }
                        sumarDistancias += Integer.parseInt(sumarDistanciasString);
                        labelDistanciaTotal.setText(sumarDistanciasString);

                    }
                    labelHoraTotal.setText(String.valueOf(sumarHoraTrasncurrida));
                }


                String selectedOrigenId = (String) comboBoxSiguienteParada.getSelectedItem();
                labelRutaActual.setText(selectedOrigenId);
                comboBoxSiguienteParada.removeAllItems();





                /////
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
                //////////////////
                if(labelRutaActual.getText()==labelRutaFinal.getText()){
                    JOptionPane.showMessageDialog(null, "SE LLEGO AL DESTINO, VER REPORTES DE ESTADO");
                    botonAvanzar.setEnabled(false);
                    comboBoxSiguienteParada.setEnabled(false);
                    comboBoxTIpoDesplazamiento.setEnabled(false);
                    comboBoxOpcionesRuta.removeAllItems();
                    MOSTRARButton.setEnabled(false);
                }

            }
        });
        comboBoxSiguienteParada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        MOSTRARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBoxOpcionesRuta.getSelectedItem().toString().equals("Mejor ruta en base a Distancia.")){
                    Map<String, Map<String, Integer>> graph = new HashMap<>();
                    for (String[] dato : datos) {
                        String ciudad1 = dato[0];
                        String ciudad2 = dato[1];
                        int distancia = Integer.parseInt(dato[6]);
                        graph.putIfAbsent(ciudad1, new HashMap<>());
                        graph.putIfAbsent(ciudad2, new HashMap<>());
                        graph.get(ciudad1).put(ciudad2, distancia);
                        graph.get(ciudad2).put(ciudad1, distancia);
                    }
                    String inicio = labelRutaActual.getText();
                    String fin = labelRutaFinal.getText();
                    Map<String, Integer> distances = new HashMap<>();
                    Map<String, String> parents = new HashMap<>();
                    PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
                    Set<String> visited = new HashSet<>();
                    for (String city : graph.keySet()) {
                        distances.put(city, Integer.MAX_VALUE);
                    }
                    distances.put(inicio, 0);
                    pq.offer(inicio);

                    while (!pq.isEmpty()) {
                        String city = pq.poll();
                        if (visited.contains(city)) continue;
                        visited.add(city);

                        Map<String, Integer> neighbors = graph.get(city);
                        for (String neighbor : neighbors.keySet()) {
                            int newDist = distances.get(city) + neighbors.get(neighbor);
                            if (newDist < distances.get(neighbor)) {
                                distances.put(neighbor, newDist);
                                parents.put(neighbor, city);
                                pq.offer(neighbor);
                            }
                        }
                    }

                    List<String> path = new ArrayList<>();
                    List<Integer> distancesList = new ArrayList<>();
                    String current = fin;
                    int totalDistance = 0;
                    while (current != null) {
                        path.add(current);
                        String parent = parents.get(current);
                        if (parent != null) {
                            int distanceToParent = graph.get(current).get(parent);
                            distancesList.add(distanceToParent);
                            totalDistance += distanceToParent;
                        }
                        current = parent;
                    }
                    Collections.reverse(path);
                    Collections.reverse(distancesList);

                    int[] arr = new int[distancesList.size()];
                    for (int i = 0; i < distancesList.size(); i++) {
                        arr[i] = distancesList.get(i);
                    }

                    String reporte1 = "Distancia más corta de " + inicio + " a " + fin + ": " + totalDistance;
                    String reporte2 = "Camino recorrido: " + String.join(" -> ", path);
                    String reporte3 = "Parametros individuales: " + Arrays.toString(arr);
                    JOptionPane.showMessageDialog(null, reporte1+"\n"+reporte2+"\n"+reporte3, "SUGERENCIA", JOptionPane.INFORMATION_MESSAGE);




                    Arrays.sort(arr); // Ordena el arreglo de manera ascendente

                    BTree bTree = new BTree(5); // Crear un árbol B con grado 5

                    // Insertar los elementos ordenados en el árbol B
                    for (int key : arr) {
                        bTree.insert(key);
                    }
                    rutaArbolB = bTree.generateDigraphTree();
                    GraphizRutaArbol graphizRutaArbol = new GraphizRutaArbol();
                    graphizRutaArbol.generarArchivoDOT();
                    graphizRutaArbol.generarImagenDesdeDOT();
                    iconoArbol = new ImageIcon("graphvizRutasArbol.png");
                    labelMostrarRutaArbolImagen.setIcon(iconoArbol);


                }
                if(comboBoxOpcionesRuta.getSelectedItem().toString().equals("Mejor ruta en base a Gasolina.")){
                    Map<String, Map<String, Integer>> graph = new HashMap<>();
                    for (String[] dato : datos) {
                        String ciudad1 = dato[0];
                        String ciudad2 = dato[1];
                        int distancia = Integer.parseInt(dato[4]);
                        graph.putIfAbsent(ciudad1, new HashMap<>());
                        graph.putIfAbsent(ciudad2, new HashMap<>());
                        graph.get(ciudad1).put(ciudad2, distancia);
                        graph.get(ciudad2).put(ciudad1, distancia);
                    }
                    String inicio = labelRutaActual.getText();
                    String fin = labelRutaFinal.getText();
                    Map<String, Integer> distances = new HashMap<>();
                    Map<String, String> parents = new HashMap<>();
                    PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
                    Set<String> visited = new HashSet<>();
                    for (String city : graph.keySet()) {
                        distances.put(city, Integer.MAX_VALUE);
                    }
                    distances.put(inicio, 0);
                    pq.offer(inicio);

                    while (!pq.isEmpty()) {
                        String city = pq.poll();
                        if (visited.contains(city)) continue;
                        visited.add(city);

                        Map<String, Integer> neighbors = graph.get(city);
                        for (String neighbor : neighbors.keySet()) {
                            int newDist = distances.get(city) + neighbors.get(neighbor);
                            if (newDist < distances.get(neighbor)) {
                                distances.put(neighbor, newDist);
                                parents.put(neighbor, city);
                                pq.offer(neighbor);
                            }
                        }
                    }

                    List<String> path = new ArrayList<>();
                    List<Integer> distancesList = new ArrayList<>();
                    String current = fin;
                    int totalDistance = 0;
                    while (current != null) {
                        path.add(current);
                        String parent = parents.get(current);
                        if (parent != null) {
                            int distanceToParent = graph.get(current).get(parent);
                            distancesList.add(distanceToParent);
                            totalDistance += distanceToParent;
                        }
                        current = parent;
                    }
                    Collections.reverse(path);
                    Collections.reverse(distancesList);

                    int[] arr = new int[distancesList.size()];
                    for (int i = 0; i < distancesList.size(); i++) {
                        arr[i] = distancesList.get(i);
                    }

                    String reporte1 = "Mejor ruta en base a Gasolina de " + inicio + " a " + fin + ": " + totalDistance;
                    String reporte2 = "Camino recorrido: " + String.join(" -> ", path);
                    String reporte3 = "Parametros individuales: " + Arrays.toString(arr);
                    JOptionPane.showMessageDialog(null, reporte1+"\n"+reporte2+"\n"+reporte3, "SUGERENCIA", JOptionPane.INFORMATION_MESSAGE);




                    Arrays.sort(arr); // Ordena el arreglo de manera ascendente

                    BTree bTree = new BTree(5); // Crear un árbol B con grado 5

                    // Insertar los elementos ordenados en el árbol B
                    for (int key : arr) {
                        bTree.insert(key);
                    }
                    rutaArbolB = bTree.generateDigraphTree();
                    GraphizRutaArbol graphizRutaArbol = new GraphizRutaArbol();
                    graphizRutaArbol.generarArchivoDOT();
                    graphizRutaArbol.generarImagenDesdeDOT();
                    iconoArbol = new ImageIcon("graphvizRutasArbol.png");
                    labelMostrarRutaArbolImagen.setIcon(iconoArbol);
                }
                if(comboBoxOpcionesRuta.getSelectedItem().toString().equals("Mejor ruta en base a Gasolina y Distancia.")){
                    Map<String, Map<String, Integer>> graph = new HashMap<>();
                    for (String[] dato : datos) {
                        String ciudad1 = dato[0];
                        String ciudad2 = dato[1];
                        int distancia = Integer.parseInt(dato[4])+Integer.parseInt(dato[6]);
                        graph.putIfAbsent(ciudad1, new HashMap<>());
                        graph.putIfAbsent(ciudad2, new HashMap<>());
                        graph.get(ciudad1).put(ciudad2, distancia);
                        graph.get(ciudad2).put(ciudad1, distancia);
                    }
                    String inicio = labelRutaActual.getText();
                    String fin = labelRutaFinal.getText();
                    Map<String, Integer> distances = new HashMap<>();
                    Map<String, String> parents = new HashMap<>();
                    PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
                    Set<String> visited = new HashSet<>();
                    for (String city : graph.keySet()) {
                        distances.put(city, Integer.MAX_VALUE);
                    }
                    distances.put(inicio, 0);
                    pq.offer(inicio);

                    while (!pq.isEmpty()) {
                        String city = pq.poll();
                        if (visited.contains(city)) continue;
                        visited.add(city);

                        Map<String, Integer> neighbors = graph.get(city);
                        for (String neighbor : neighbors.keySet()) {
                            int newDist = distances.get(city) + neighbors.get(neighbor);
                            if (newDist < distances.get(neighbor)) {
                                distances.put(neighbor, newDist);
                                parents.put(neighbor, city);
                                pq.offer(neighbor);
                            }
                        }
                    }

                    List<String> path = new ArrayList<>();
                    List<Integer> distancesList = new ArrayList<>();
                    String current = fin;
                    int totalDistance = 0;
                    while (current != null) {
                        path.add(current);
                        String parent = parents.get(current);
                        if (parent != null) {
                            int distanceToParent = graph.get(current).get(parent);
                            distancesList.add(distanceToParent);
                            totalDistance += distanceToParent;
                        }
                        current = parent;
                    }
                    Collections.reverse(path);
                    Collections.reverse(distancesList);

                    int[] arr = new int[distancesList.size()];
                    for (int i = 0; i < distancesList.size(); i++) {
                        arr[i] = distancesList.get(i);
                    }

                    String reporte1 = "Mejor ruta en base a Gasolina y Distancia de " + inicio + " a " + fin + ": " + totalDistance;
                    String reporte2 = "Camino recorrido: " + String.join(" -> ", path);
                    String reporte3 = "Parametros individuales: " + Arrays.toString(arr);
                    JOptionPane.showMessageDialog(null, reporte1+"\n"+reporte2+"\n"+reporte3, "SUGERENCIA", JOptionPane.INFORMATION_MESSAGE);




                    Arrays.sort(arr); // Ordena el arreglo de manera ascendente

                    BTree bTree = new BTree(5); // Crear un árbol B con grado 5

                    // Insertar los elementos ordenados en el árbol B
                    for (int key : arr) {
                        bTree.insert(key);
                    }
                    rutaArbolB = bTree.generateDigraphTree();
                    GraphizRutaArbol graphizRutaArbol = new GraphizRutaArbol();
                    graphizRutaArbol.generarArchivoDOT();
                    graphizRutaArbol.generarImagenDesdeDOT();
                    iconoArbol = new ImageIcon("graphvizRutasArbol.png");
                    labelMostrarRutaArbolImagen.setIcon(iconoArbol);
                }
                if(comboBoxOpcionesRuta.getSelectedItem().toString().equals("Mejor ruta en base a Tiempo y Distancia y Trafico.")){
                    Map<String, Map<String, Integer>> graph = new HashMap<>();
                    for (String[] dato : datos) {
                        String ciudad1 = dato[0];
                        String ciudad2 = dato[1];
                        if(hora>=Integer.parseInt(dato[7]) && hora<=Integer.parseInt(dato[8])){

                            int distancia = ((Integer.parseInt(dato[6]))/((Integer.parseInt(dato[2])) * (1+ (Integer.parseInt(dato[6]))/100)));
                            graph.putIfAbsent(ciudad1, new HashMap<>());
                            graph.putIfAbsent(ciudad2, new HashMap<>());
                            graph.get(ciudad1).put(ciudad2, distancia);
                            graph.get(ciudad2).put(ciudad1, distancia);
                        }else {
                            int distancia = Integer.parseInt(dato[6])/Integer.parseInt(dato[2]);
                            graph.putIfAbsent(ciudad1, new HashMap<>());
                            graph.putIfAbsent(ciudad2, new HashMap<>());
                            graph.get(ciudad1).put(ciudad2, distancia);
                            graph.get(ciudad2).put(ciudad1, distancia);
                        }
                    }
                    String inicio = labelRutaActual.getText();
                    String fin = labelRutaFinal.getText();
                    Map<String, Integer> distances = new HashMap<>();
                    Map<String, String> parents = new HashMap<>();
                    PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
                    Set<String> visited = new HashSet<>();
                    for (String city : graph.keySet()) {
                        distances.put(city, Integer.MAX_VALUE);
                    }
                    distances.put(inicio, 0);
                    pq.offer(inicio);

                    while (!pq.isEmpty()) {
                        String city = pq.poll();
                        if (visited.contains(city)) continue;
                        visited.add(city);

                        Map<String, Integer> neighbors = graph.get(city);
                        for (String neighbor : neighbors.keySet()) {
                            int newDist = distances.get(city) + neighbors.get(neighbor);
                            if (newDist < distances.get(neighbor)) {
                                distances.put(neighbor, newDist);
                                parents.put(neighbor, city);
                                pq.offer(neighbor);
                            }
                        }
                    }

                    List<String> path = new ArrayList<>();
                    List<Integer> distancesList = new ArrayList<>();
                    String current = fin;
                    int totalDistance = 0;
                    while (current != null) {
                        path.add(current);
                        String parent = parents.get(current);
                        if (parent != null) {
                            int distanceToParent = graph.get(current).get(parent);
                            distancesList.add(distanceToParent);
                            totalDistance += distanceToParent;
                        }
                        current = parent;
                    }
                    Collections.reverse(path);
                    Collections.reverse(distancesList);

                    int[] arr = new int[distancesList.size()];
                    for (int i = 0; i < distancesList.size(); i++) {
                        arr[i] = distancesList.get(i);
                    }

                    String reporte1 = "Mejor ruta en base a Tiempo y Distancia y Trafico de " + inicio + " a " + fin + ": " + totalDistance;
                    String reporte2 = "Camino recorrido: " + String.join(" -> ", path);
                    String reporte3 = "Parametros individuales: " + Arrays.toString(arr);
                    JOptionPane.showMessageDialog(null, reporte1+"\n"+reporte2+"\n"+reporte3, "SUGERENCIA", JOptionPane.INFORMATION_MESSAGE);




                    Arrays.sort(arr); // Ordena el arreglo de manera ascendente

                    BTree bTree = new BTree(5); // Crear un árbol B con grado 5

                    // Insertar los elementos ordenados en el árbol B
                    for (int key : arr) {
                        bTree.insert(key);
                    }
                    rutaArbolB = bTree.generateDigraphTree();
                    GraphizRutaArbol graphizRutaArbol = new GraphizRutaArbol();
                    graphizRutaArbol.generarArchivoDOT();
                    graphizRutaArbol.generarImagenDesdeDOT();
                    iconoArbol = new ImageIcon("graphvizRutasArbol.png");
                    labelMostrarRutaArbolImagen.setIcon(iconoArbol);
                }
                if(comboBoxOpcionesRuta.getSelectedItem().toString().equals("Mejor ruta en base a Distancia Caminando.")){
                    Map<String, Map<String, Integer>> graph = new HashMap<>();
                    for (String[] dato : datos) {
                        String ciudad1 = dato[0];
                        String ciudad2 = dato[1];
                        int distancia = Integer.parseInt(dato[6]);
                        graph.putIfAbsent(ciudad1, new HashMap<>());
                        graph.putIfAbsent(ciudad2, new HashMap<>());
                        graph.get(ciudad1).put(ciudad2, distancia);
                        graph.get(ciudad2).put(ciudad1, distancia);
                    }
                    for (String[] dato : datos2) {
                        String ciudad1 = dato[1];
                        String ciudad2 = dato[0];
                        int distancia = Integer.parseInt(dato[6]);
                        graph.putIfAbsent(ciudad1, new HashMap<>());
                        graph.putIfAbsent(ciudad2, new HashMap<>());
                        graph.get(ciudad1).put(ciudad2, distancia);
                        graph.get(ciudad2).put(ciudad1, distancia);
                    }
                    String inicio = labelRutaActual.getText();
                    String fin = labelRutaFinal.getText();
                    Map<String, Integer> distances = new HashMap<>();
                    Map<String, String> parents = new HashMap<>();
                    PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
                    Set<String> visited = new HashSet<>();
                    for (String city : graph.keySet()) {
                        distances.put(city, Integer.MAX_VALUE);
                    }
                    distances.put(inicio, 0);
                    pq.offer(inicio);

                    while (!pq.isEmpty()) {
                        String city = pq.poll();
                        if (visited.contains(city)) continue;
                        visited.add(city);

                        Map<String, Integer> neighbors = graph.get(city);
                        for (String neighbor : neighbors.keySet()) {
                            int newDist = distances.get(city) + neighbors.get(neighbor);
                            if (newDist < distances.get(neighbor)) {
                                distances.put(neighbor, newDist);
                                parents.put(neighbor, city);
                                pq.offer(neighbor);
                            }
                        }
                    }

                    List<String> path = new ArrayList<>();
                    List<Integer> distancesList = new ArrayList<>();
                    String current = fin;
                    int totalDistance = 0;
                    while (current != null) {
                        path.add(current);
                        String parent = parents.get(current);
                        if (parent != null) {
                            int distanceToParent = graph.get(current).get(parent);
                            distancesList.add(distanceToParent);
                            totalDistance += distanceToParent;
                        }
                        current = parent;
                    }
                    Collections.reverse(path);
                    Collections.reverse(distancesList);

                    int[] arr = new int[distancesList.size()];
                    for (int i = 0; i < distancesList.size(); i++) {
                        arr[i] = distancesList.get(i);
                    }

                    String reporte1 = "Mejor ruta en base a Distancia Caminando de " + inicio + " a " + fin + ": " + totalDistance;
                    String reporte2 = "Camino recorrido: " + String.join(" -> ", path);
                    String reporte3 = "Parametros individuales: " + Arrays.toString(arr);
                    JOptionPane.showMessageDialog(null, reporte1+"\n"+reporte2+"\n"+reporte3, "SUGERENCIA", JOptionPane.INFORMATION_MESSAGE);




                    Arrays.sort(arr); // Ordena el arreglo de manera ascendente

                    BTree bTree = new BTree(5); // Crear un árbol B con grado 5

                    // Insertar los elementos ordenados en el árbol B
                    for (int key : arr) {
                        bTree.insert(key);
                    }
                    rutaArbolB = bTree.generateDigraphTree();
                    GraphizRutaArbol graphizRutaArbol = new GraphizRutaArbol();
                    graphizRutaArbol.generarArchivoDOT();
                    graphizRutaArbol.generarImagenDesdeDOT();
                    iconoArbol = new ImageIcon("graphvizRutasArbol.png");
                    labelMostrarRutaArbolImagen.setIcon(iconoArbol);
                }
                if(comboBoxOpcionesRuta.getSelectedItem().toString().equals("Mejor ruta en base a Desgaste.")){
                    Map<String, Map<String, Integer>> graph = new HashMap<>();
                    for (String[] dato : datos) {
                        String ciudad1 = dato[0];
                        String ciudad2 = dato[1];
                        int distancia = Integer.parseInt(dato[5]);
                        graph.putIfAbsent(ciudad1, new HashMap<>());
                        graph.putIfAbsent(ciudad2, new HashMap<>());
                        graph.get(ciudad1).put(ciudad2, distancia);
                        graph.get(ciudad2).put(ciudad1, distancia);
                    }
                    for (String[] dato : datos2) {
                        String ciudad1 = dato[1];
                        String ciudad2 = dato[0];
                        int distancia = Integer.parseInt(dato[5]);
                        graph.putIfAbsent(ciudad1, new HashMap<>());
                        graph.putIfAbsent(ciudad2, new HashMap<>());
                        graph.get(ciudad1).put(ciudad2, distancia);
                        graph.get(ciudad2).put(ciudad1, distancia);
                    }
                    String inicio = labelRutaActual.getText();
                    String fin = labelRutaFinal.getText();
                    Map<String, Integer> distances = new HashMap<>();
                    Map<String, String> parents = new HashMap<>();
                    PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
                    Set<String> visited = new HashSet<>();
                    for (String city : graph.keySet()) {
                        distances.put(city, Integer.MAX_VALUE);
                    }
                    distances.put(inicio, 0);
                    pq.offer(inicio);

                    while (!pq.isEmpty()) {
                        String city = pq.poll();
                        if (visited.contains(city)) continue;
                        visited.add(city);

                        Map<String, Integer> neighbors = graph.get(city);
                        for (String neighbor : neighbors.keySet()) {
                            int newDist = distances.get(city) + neighbors.get(neighbor);
                            if (newDist < distances.get(neighbor)) {
                                distances.put(neighbor, newDist);
                                parents.put(neighbor, city);
                                pq.offer(neighbor);
                            }
                        }
                    }

                    List<String> path = new ArrayList<>();
                    List<Integer> distancesList = new ArrayList<>();
                    String current = fin;
                    int totalDistance = 0;
                    while (current != null) {
                        path.add(current);
                        String parent = parents.get(current);
                        if (parent != null) {
                            int distanceToParent = graph.get(current).get(parent);
                            distancesList.add(distanceToParent);
                            totalDistance += distanceToParent;
                        }
                        current = parent;
                    }
                    Collections.reverse(path);
                    Collections.reverse(distancesList);

                    int[] arr = new int[distancesList.size()];
                    for (int i = 0; i < distancesList.size(); i++) {
                        arr[i] = distancesList.get(i);
                    }

                    String reporte1 = "Mejor ruta en base a Desgaste " + inicio + " a " + fin + ": " + totalDistance;
                    String reporte2 = "Camino recorrido: " + String.join(" -> ", path);
                    String reporte3 = "Parametros individuales: " + Arrays.toString(arr);
                    JOptionPane.showMessageDialog(null, reporte1+"\n"+reporte2+"\n"+reporte3, "SUGERENCIA", JOptionPane.INFORMATION_MESSAGE);




                    Arrays.sort(arr); // Ordena el arreglo de manera ascendente

                    BTree bTree = new BTree(5); // Crear un árbol B con grado 5

                    // Insertar los elementos ordenados en el árbol B
                    for (int key : arr) {
                        bTree.insert(key);
                    }
                    rutaArbolB = bTree.generateDigraphTree();
                    GraphizRutaArbol graphizRutaArbol = new GraphizRutaArbol();
                    graphizRutaArbol.generarArchivoDOT();
                    graphizRutaArbol.generarImagenDesdeDOT();
                    iconoArbol = new ImageIcon("graphvizRutasArbol.png");
                    labelMostrarRutaArbolImagen.setIcon(iconoArbol);
                }
                if(comboBoxOpcionesRuta.getSelectedItem().toString().equals("Mejor ruta en base a Desgaste y Distancia.")){
                    Map<String, Map<String, Integer>> graph = new HashMap<>();
                    for (String[] dato : datos) {
                        String ciudad1 = dato[0];
                        String ciudad2 = dato[1];
                        int distancia = (Integer.parseInt(dato[5])+Integer.parseInt(dato[6]))/2;
                        graph.putIfAbsent(ciudad1, new HashMap<>());
                        graph.putIfAbsent(ciudad2, new HashMap<>());
                        graph.get(ciudad1).put(ciudad2, distancia);
                        graph.get(ciudad2).put(ciudad1, distancia);
                    }
                    for (String[] dato : datos2) {
                        String ciudad1 = dato[1];
                        String ciudad2 = dato[0];
                        int distancia = (Integer.parseInt(dato[5])+Integer.parseInt(dato[6]))/2;
                        graph.putIfAbsent(ciudad1, new HashMap<>());
                        graph.putIfAbsent(ciudad2, new HashMap<>());
                        graph.get(ciudad1).put(ciudad2, distancia);
                        graph.get(ciudad2).put(ciudad1, distancia);
                    }
                    String inicio = labelRutaActual.getText();
                    String fin = labelRutaFinal.getText();
                    Map<String, Integer> distances = new HashMap<>();
                    Map<String, String> parents = new HashMap<>();
                    PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
                    Set<String> visited = new HashSet<>();
                    for (String city : graph.keySet()) {
                        distances.put(city, Integer.MAX_VALUE);
                    }
                    distances.put(inicio, 0);
                    pq.offer(inicio);

                    while (!pq.isEmpty()) {
                        String city = pq.poll();
                        if (visited.contains(city)) continue;
                        visited.add(city);

                        Map<String, Integer> neighbors = graph.get(city);
                        for (String neighbor : neighbors.keySet()) {
                            int newDist = distances.get(city) + neighbors.get(neighbor);
                            if (newDist < distances.get(neighbor)) {
                                distances.put(neighbor, newDist);
                                parents.put(neighbor, city);
                                pq.offer(neighbor);
                            }
                        }
                    }

                    List<String> path = new ArrayList<>();
                    List<Integer> distancesList = new ArrayList<>();
                    String current = fin;
                    int totalDistance = 0;
                    while (current != null) {
                        path.add(current);
                        String parent = parents.get(current);
                        if (parent != null) {
                            int distanceToParent = graph.get(current).get(parent);
                            distancesList.add(distanceToParent);
                            totalDistance += distanceToParent;
                        }
                        current = parent;
                    }
                    Collections.reverse(path);
                    Collections.reverse(distancesList);

                    int[] arr = new int[distancesList.size()];
                    for (int i = 0; i < distancesList.size(); i++) {
                        arr[i] = distancesList.get(i);
                    }

                    String reporte1 = "Mejor ruta en base a Desgaste y Distancia de " + inicio + " a " + fin + ": " + totalDistance;
                    String reporte2 = "Camino recorrido: " + String.join(" -> ", path);
                    String reporte3 = "Parametros individuales: " + Arrays.toString(arr);
                    JOptionPane.showMessageDialog(null, reporte1+"\n"+reporte2+"\n"+reporte3, "SUGERENCIA", JOptionPane.INFORMATION_MESSAGE);




                    Arrays.sort(arr); // Ordena el arreglo de manera ascendente

                    BTree bTree = new BTree(5); // Crear un árbol B con grado 5

                    // Insertar los elementos ordenados en el árbol B
                    for (int key : arr) {
                        bTree.insert(key);
                    }
                    rutaArbolB = bTree.generateDigraphTree();
                    GraphizRutaArbol graphizRutaArbol = new GraphizRutaArbol();
                    graphizRutaArbol.generarArchivoDOT();
                    graphizRutaArbol.generarImagenDesdeDOT();
                    iconoArbol = new ImageIcon("graphvizRutasArbol.png");
                    labelMostrarRutaArbolImagen.setIcon(iconoArbol);
                }
                if(comboBoxOpcionesRuta.getSelectedItem().toString().equals("Mejor ruta en base a Tiempo y Distancia .")){
                    Map<String, Map<String, Integer>> graph = new HashMap<>();
                    for (String[] dato : datos) {
                        String ciudad1 = dato[0];
                        String ciudad2 = dato[1];
                            int distancia = Integer.parseInt(dato[6])/Integer.parseInt(dato[3]);
                            graph.putIfAbsent(ciudad1, new HashMap<>());
                            graph.putIfAbsent(ciudad2, new HashMap<>());
                            graph.get(ciudad1).put(ciudad2, distancia);
                            graph.get(ciudad2).put(ciudad1, distancia);
                    }
                    String inicio = labelRutaActual.getText();
                    String fin = labelRutaFinal.getText();
                    Map<String, Integer> distances = new HashMap<>();
                    Map<String, String> parents = new HashMap<>();
                    PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
                    Set<String> visited = new HashSet<>();
                    for (String city : graph.keySet()) {
                        distances.put(city, Integer.MAX_VALUE);
                    }
                    distances.put(inicio, 0);
                    pq.offer(inicio);

                    while (!pq.isEmpty()) {
                        String city = pq.poll();
                        if (visited.contains(city)) continue;
                        visited.add(city);

                        Map<String, Integer> neighbors = graph.get(city);
                        for (String neighbor : neighbors.keySet()) {
                            int newDist = distances.get(city) + neighbors.get(neighbor);
                            if (newDist < distances.get(neighbor)) {
                                distances.put(neighbor, newDist);
                                parents.put(neighbor, city);
                                pq.offer(neighbor);
                            }
                        }
                    }

                    List<String> path = new ArrayList<>();
                    List<Integer> distancesList = new ArrayList<>();
                    String current = fin;
                    int totalDistance = 0;
                    while (current != null) {
                        path.add(current);
                        String parent = parents.get(current);
                        if (parent != null) {
                            int distanceToParent = graph.get(current).get(parent);
                            distancesList.add(distanceToParent);
                            totalDistance += distanceToParent;
                        }
                        current = parent;
                    }
                    Collections.reverse(path);
                    Collections.reverse(distancesList);

                    int[] arr = new int[distancesList.size()];
                    for (int i = 0; i < distancesList.size(); i++) {
                        arr[i] = distancesList.get(i);
                    }

                    String reporte1 = "Mejor ruta en base a Tiempo y Distancia de " + inicio + " a " + fin + ": " + totalDistance;
                    String reporte2 = "Camino recorrido: " + String.join(" -> ", path);
                    String reporte3 = "Parametros individuales: " + Arrays.toString(arr);
                    JOptionPane.showMessageDialog(null, reporte1+"\n"+reporte2+"\n"+reporte3, "SUGERENCIA", JOptionPane.INFORMATION_MESSAGE);




                    Arrays.sort(arr); // Ordena el arreglo de manera ascendente

                    BTree bTree = new BTree(5); // Crear un árbol B con grado 5

                    // Insertar los elementos ordenados en el árbol B
                    for (int key : arr) {
                        bTree.insert(key);
                    }
                    rutaArbolB = bTree.generateDigraphTree();
                    GraphizRutaArbol graphizRutaArbol = new GraphizRutaArbol();
                    graphizRutaArbol.generarArchivoDOT();
                    graphizRutaArbol.generarImagenDesdeDOT();
                    iconoArbol = new ImageIcon("graphvizRutasArbol.png");
                    labelMostrarRutaArbolImagen.setIcon(iconoArbol);
                }


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

        timer.stop();

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
        timer.start();

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
        timer = new Timer(1000, new ActionListener() {
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

    class BTreeNode {
        int[] keys;
        int t; // grado mínimo del árbol
        BTreeNode[] children;
        int n; // Número actual de claves
        boolean leaf; // Indica si el nodo es una hoja

        BTreeNode(int t, boolean leaf) {
            this.t = t;
            this.leaf = leaf;
            this.keys = new int[2 * t - 1];
            this.children = new BTreeNode[2 * t];
            this.n = 0;
        }
    }

    class BTree {
        private BTreeNode root;
        private int t; // grado mínimo del árbol

        BTree(int t) {
            this.t = t;
            this.root = null;
        }

        // Función para insertar un nuevo valor en el árbol
        void insert(int key) {
            // Si el árbol está vacío
            if (root == null) {
                root = new BTreeNode(t, true);
                root.keys[0] = key;
                root.n = 1;
            } else {
                // Si la raíz está llena, se divide
                if (root.n == 2 * t - 1) {
                    BTreeNode newRoot = new BTreeNode(t, false);
                    newRoot.children[0] = root;
                    splitChild(newRoot, 0);
                    int i = 0;
                    if (newRoot.keys[0] < key)
                        i++;
                    insertNonFull(newRoot.children[i], key);
                    root = newRoot;
                } else {
                    insertNonFull(root, key);
                }
            }
        }

        // Función auxiliar para insertar cuando el nodo no está lleno
        private void insertNonFull(BTreeNode node, int key) {
            int i = node.n - 1;
            if (node.leaf) {
                while (i >= 0 && node.keys[i] > key) {
                    node.keys[i + 1] = node.keys[i];
                    i--;
                }
                node.keys[i + 1] = key;
                node.n++;
            } else {
                while (i >= 0 && node.keys[i] > key)
                    i--;
                i++;
                if (node.children[i].n == 2 * t - 1) {
                    splitChild(node, i);
                    if (node.keys[i] < key)
                        i++;
                }
                insertNonFull(node.children[i], key);
            }
        }

        // Función para dividir el hijo del nodo x
        private void splitChild(BTreeNode x, int i) {
            BTreeNode y = x.children[i];
            BTreeNode z = new BTreeNode(t, y.leaf);
            z.n = t - 1;
            for (int j = 0; j < t - 1; j++)
                z.keys[j] = y.keys[j + t];
            if (!y.leaf) {
                for (int j = 0; j < t; j++)
                    z.children[j] = y.children[j + t];
            }
            y.n = t - 1;
            for (int j = x.n; j >= i + 1; j--)
                x.children[j + 1] = x.children[j];
            x.children[i + 1] = z;
            for (int j = x.n - 1; j >= i; j--)
                x.keys[j + 1] = x.keys[j];
            x.keys[i] = y.keys[t - 1];
            x.n++;
        }

        // Función para recorrer el árbol y generar el formato de digraph
        String generateDigraphTree() {
            StringBuilder sb = new StringBuilder();
            sb.append("digraph Tree {\n");
            sb.append("    node [shape=record];\n");

            int counter = 1;
            sb.append(generateDigraphNode(root, counter));

            sb.append("}");
            return sb.toString();
        }

        private String generateDigraphNode(BTreeNode node, int counter) {
            StringBuilder sb = new StringBuilder();
            sb.append("    ").append(counter).append("[label=\"");
            for (int i = 0; i < node.n; i++) {
                sb.append("<C").append(i).append(">").append(node.keys[i]);
                if (i < node.n - 1)
                    sb.append("|");
            }
            sb.append("\"]\n");

            if (!node.leaf) {
                int childCounter = counter + 1; // Contador para los hijos
                for (int i = 0; i <= node.n; i++) {
                    sb.append("    ").append(counter).append(":C").append(i).append(" -> ").append(childCounter).append("\n");
                    sb.append(generateDigraphNode(node.children[i], childCounter));
                    childCounter++; // Incrementa el contador para los hijos
                }
            }

            return sb.toString();
        }


    }



}
