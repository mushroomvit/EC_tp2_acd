package algoritmos;

import java.util.ArrayList;
import java.util.List;
import src.Aresta;
import src.Grafo;

public class DFS {
    public static List<Integer> buscar(Grafo grafo, int origem) {
        if (origem < 0 || origem >= grafo.numeroVertices()) {
            throw new IllegalArgumentException("Origem invalida.");
        }
        boolean[] visitado = new boolean[grafo.numeroVertices()];
        List<Integer> ordem = new ArrayList<>();
        visitar(grafo, origem, visitado, ordem);
        return ordem;
    }

    private static void visitar(Grafo grafo, int atual, boolean[] visitado, List<Integer> ordem) {
        visitado[atual] = true;
        ordem.add(atual);
        for (Aresta aresta : grafo.adjacentes(atual)) {
            int u = aresta.getU().getId();
            int vizinho = u == atual ? aresta.getV().getId() : u;
            if (!visitado[vizinho]) {
                visitar(grafo, vizinho, visitado, ordem);
            }
        }
    }
}
