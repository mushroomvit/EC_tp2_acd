package algoritmos;

import java.util.Arrays;
import src.Aresta;
import src.Grafo;

public class BellmanFord {
    public static double[] calcular(Grafo grafo, int fonte) {
        double[] dist = new double[grafo.numeroVertices()];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[fonte] = 0;
        for (int i = 1; i < grafo.numeroVertices(); i++) {
            boolean mudou = false;
            for (Aresta e : grafo.arestas()) {
                double candidato = dist[e.getU().getId()] + e.getPeso();
                if (candidato < dist[e.getV().getId()]) {
                    dist[e.getV().getId()] = candidato;
                    mudou = true;
                }
            }
            if (!mudou) break;
        }
        for (Aresta e : grafo.arestas()) {
            if (dist[e.getU().getId()] + e.getPeso() < dist[e.getV().getId()]) {
                throw new IllegalArgumentException("Ciclo negativo alcancavel pela origem.");
            }
        }
        return dist;
    }
}
