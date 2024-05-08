package org.zafkiel.backend.edd;

import org.zafkiel.frontend.HomePage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManejadorRutas {
    public static ArrayList<String[]> rutas = new ArrayList<>();
    public static ArrayList<String> rutaOrigenOrdenada = new ArrayList<>();

    public static void llenarRutasOrigen(){
        rutas = new ArrayList<>(HomePage.datos);
        ordenarRutas();
    }

    public static void ordenarRutas(){
        for (String[] fila : rutas) {
            String elemento = fila[1];
            boolean encontrado = false;
            for (int i = 0; i < rutaOrigenOrdenada.size(); i++) {
                if (rutaOrigenOrdenada.get(i).equals(elemento)) {
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado && elemento != null && !elemento.isEmpty()) {
                rutaOrigenOrdenada.add(elemento);
            }
        }

        Collections.sort(rutaOrigenOrdenada);

        /*for (String elemento : rutaOrigenOrdenada) {
            System.out.println(elemento);
        } */
    }
    static class Nodo {
        private int id;
        private List<Nodo> vecinos;

        public Nodo(int id) {
            this.id = id;
            this.vecinos = new ArrayList<>();
        }

        public int getId() {
            return id;
        }

        public List<Nodo> getVecinos() {
            return vecinos;
        }

        public void agregarVecino(Nodo vecino) {
            vecinos.add(vecino);
        }
    }
    public static void llenarRutasDestino(){
        // Supongamos que tienes una lista de nodos llamada 'nodos'
        List<Nodo> nodos = new ArrayList<>();

        Nodo nodo1 = new Nodo(1);
        Nodo nodo2 = new Nodo(2);
        Nodo nodo3 = new Nodo(3);
        Nodo nodo4 = new Nodo(4);

        nodo1.agregarVecino(nodo2);
        nodo2.agregarVecino(nodo3);
        nodo3.agregarVecino(nodo4);
        nodo1.agregarVecino(nodo3);

        nodos.add(nodo1);
        nodos.add(nodo2);
        nodos.add(nodo3);
        nodos.add(nodo4);


        // Itera sobre cada nodo e imprime todos los caminos posibles desde ese nodo
        for (Nodo nodo : nodos) {
            System.out.println("Caminos desde el nodo " + nodo.getId() + ":");
            List<Nodo> visitados = new ArrayList<>();
            List<Nodo> caminoActual = new ArrayList<>();
            dfs(nodo, visitados, caminoActual);
        }
        for (Nodo nodo : nodos) {
            System.out.println("Origen: " + nodo.getId() + ", Destino: ");
            for (Nodo vecino : nodo.getVecinos()) {
                System.out.print(vecino.getId() + " ");
            }
            System.out.println();
        }
    }
    static void dfs(Nodo nodoActual, List<Nodo> visitados, List<Nodo> caminoActual) {
        // Agrega el nodo actual al camino actual y marca como visitado
        visitados.add(nodoActual);
        caminoActual.add(nodoActual);

        // Imprime el camino actual
        for (Nodo nodo : caminoActual) {
            System.out.print(nodo.getId() + " ");
        }
        System.out.println();

        // Explora los vecinos no visitados
        for (Nodo vecino : nodoActual.getVecinos()) {
            if (!visitados.contains(vecino)) {
                dfs(vecino, visitados, caminoActual);
            }
        }

        // Elimina el nodo actual del camino actual y marca como no visitado
        visitados.remove(nodoActual);
        caminoActual.remove(nodoActual);

    }
}

