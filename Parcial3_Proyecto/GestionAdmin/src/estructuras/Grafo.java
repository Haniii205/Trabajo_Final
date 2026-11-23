package estructuras;

import java.util.ArrayList;

public class Grafo {

    private ArrayList<Edificio> edificios;
    private int[][] matrizAdy;  // Aquí la matriz int[N][N]
    private int cantidadEdificios;

    public Grafo(int maxEdificios) {
        this.edificios = new ArrayList<>();
        this.matrizAdy = new int[maxEdificios][maxEdificios];
        this.cantidadEdificios = 0;
    }

    public void agregarEdificio(Edificio e) {
        if (cantidadEdificios < matrizAdy.length) {
            edificios.add(e);
            cantidadEdificios++;
        }
    }

    public boolean agregarConexion(int idOrigen, int idDestino, int distancia) {
        if (idOrigen < 0 || idOrigen >= cantidadEdificios
                || idDestino < 0 || idDestino >= cantidadEdificios) {
            return false;
        }
        if (idOrigen == idDestino || distancia <= 0) { //Evita conectar un edificio consigo mismo
            return false;
        }
        matrizAdy[idOrigen][idDestino] = distancia;  
        matrizAdy[idDestino][idOrigen] = distancia; // guarda la distancia en ambos sentidos (el camino sirve de ida y devuelta)
        return true;
    }

    public void mostrarEdificios() {
        System.out.println("Edificios registrados:");
        for (int i = 0; i < cantidadEdificios; i++) {
            System.out.println(i + ": " + edificios.get(i).getNombre());
        }
    }

    public void calcularRutaMasCorta(int idOrigen, int idDestino) {
        if (idOrigen < 0 || idOrigen >= cantidadEdificios || idDestino < 0 || idDestino >= cantidadEdificios) {
            System.out.println("Error: IDs de edificios invalidos.");
            return;
        }

        int[] distancias = new int[cantidadEdificios];
        boolean[] visitados = new boolean[cantidadEdificios];
        int[] anteriores = new int[cantidadEdificios];

        // Inicia las distancias
        for (int i = 0; i < cantidadEdificios; i++) {
            distancias[i] = Integer.MAX_VALUE;
            visitados[i] = false;
            anteriores[i] = -1;
        }

        distancias[idOrigen] = 0; // La distancia al origen es 0

        // Algoritmo de Dijkstraaa °°°°°
        for (int i = 0; i < cantidadEdificios - 1; i++) {
            int u = distanciaMinima(distancias, visitados);
            if (u == -1) {
                break; // Si no hay más caminos, salir
            }
            visitados[u] = true;

            for (int v = 0; v < cantidadEdificios; v++) {
                // Si hay conexion, no ha sido visitado, y el camino nuevo es más corto
                if (!visitados[v]
                        && matrizAdy[u][v] != 0
                        && distancias[u] != Integer.MAX_VALUE
                        && distancias[u] + matrizAdy[u][v] < distancias[v]) {

                    distancias[v] = distancias[u] + matrizAdy[u][v];
                    anteriores[v] = u;
                }
            }
        }

        // Muestra loss resultados
        if (distancias[idDestino] == Integer.MAX_VALUE) {
            System.out.println("No hay ruta entre estos edificios.");
            return;
        }

        System.out.println("\n--- RESULTADO ---");
        System.out.print("Ruta mas corta: ");

        // Reconstruir el camino usando el arreglo anteriores
        ArrayList<Integer> camino = new ArrayList<>();
        int actual = idDestino;
        while (actual != -1) {
            camino.add(0, actual); // Lo mete al principio
            actual = anteriores[actual];
        }

        for (int i = 0; i < camino.size(); i++) {
            int id = camino.get(i);
            System.out.print(edificios.get(id).getNombre());
            if (i > 0) {
                System.out.print(" (" + matrizAdy[camino.get(i - 1)][id] + "m)");
            }
            if (i < camino.size() - 1) {
                System.out.print(" ---> ");
            }
        }
        System.out.println("\nDistancia TOTAL: " + distancias[idDestino] + " metros");
    }

    // Método auxiliar para encontrar el nodo con la distancia mínima
    private int distanciaMinima(int[] distancias, boolean[] visitados) {
        int min = Integer.MAX_VALUE;
        int indiceMin = -1;
        for (int i = 0; i < cantidadEdificios; i++) {
            if (!visitados[i] && distancias[i] <= min) {
                min = distancias[i];
                indiceMin = i;
            }
        }
        return indiceMin;
    }
}
