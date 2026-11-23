package algoritmos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import src.Aresta;
import src.Grafo;
import src.TipoGrafo;

public class OrdenacaoTopologica {
    private enum Estado { EM_VISITA, CONCLUIDO }

    public static List<Integer> ordenar(Grafo grafo) {
        if (grafo.tipo() != TipoGrafo.DIRECIONADO) {
            throw new IllegalArgumentException("Ordenacao topologica exige um grafo direcionado.");
        }
        Estado[] estados = new Estado[grafo.numeroVertices()];
        List<Integer> ordem = new ArrayList<>();
        for (int v = 0; v < estados.length; v++) {
            if (estados[v] == null) {
                visitar(grafo, v, estados, ordem);
            }
        }
        Collections.reverse(ordem);
        return ordem;
    }

    private static void visitar(Grafo grafo, int atual, Estado[] estados, List<Integer> ordem) {
        estados[atual] = Estado.EM_VISITA;
        for (Aresta aresta : grafo.adjacentes(atual)) {
            int destino = aresta.getV().getId();
            if (estados[destino] == Estado.EM_VISITA) {
                throw new IllegalArgumentException("O grafo contem ciclo e nao admite ordenacao topologica.");
            }
            if (estados[destino] == null) {
                visitar(grafo, destino, estados, ordem);
            }
        }
        estados[atual] = Estado.CONCLUIDO;
        ordem.add(atual);
    }
}