package algoritmos;

import java.util.PriorityQueue;
import src_grafo.Grafo;
import src_grafo.Aresta;

// classe auxiliar para a fila de prioridade Q
class NoVertice implements Comparable<NoVertice> {
    int id;
    double distancia;

    public NoVertice(int id, double distancia) {
        this.id = id;
        this.distancia = distancia;
    }

    @Override
    public int compareTo(NoVertice outro) {
        return Double.compare(this.distancia, outro.distancia);
    }
}

// implementação do algoritmo
public class Dijkstra {

    private double[] d;
    private int[] pi;

    public void executarDijkstra(Grafo grafo, int origem) {
        if (grafo.arestas().stream().anyMatch(a -> a.getPeso() < 0)) {
            throw new IllegalArgumentException("Dijkstra exige pesos nao negativos.");
        }
        int n = grafo.numeroVertices();
        d = new double[n];
        pi = new int[n];

        // mantém um conjunto S de vértices que   pesos finais de caminhos mínimos que partem da origem já foram determinados
        boolean[] S = new boolean[n];

        // inicialização
        for (int i = 0; i < n; i++) {
            d[i] = Double.POSITIVE_INFINITY;
            pi[i] = -1;
        }
        d[origem] = 0;

        // fila de prioridades mínimas Q de vértices, cujas chaves são os valores de d
        PriorityQueue<NoVertice> Q = new PriorityQueue<>();
        Q.add(new NoVertice(origem, 0.0));

        while (!Q.isEmpty()) {
            // seleciona repetidamente o vértice u ∈ V – S que tem a mínima estimativa do caminho mínimo
            NoVertice atual = Q.poll();
            int u = atual.id;

            // se o vértice já está no conjunto S, ignoramos (substitui a necessidade do DIMINUI-CHAVE)
            if (S[u]) continue;

            // adiciona u a S
            S[u] = true;

            // relaxa todas as arestas que saem de u
            for (Aresta aresta : grafo.adjacentes(u)) {
                int v = aresta.getU().getId() == u ? aresta.getV().getId() : aresta.getU().getId();
                double pesoUV = aresta.getPeso();

                if (!S[v] && d[u] + pesoUV < d[v]) {
                    d[v] = d[u] + pesoUV;
                    pi[v] = u;
                    // como não há DIMINUI-CHAVE nativo em O(log V) no Java, reinserimos o vértice com o peso atualizado
                    Q.add(new NoVertice(v, d[v]));
                }
            }
        }
    }

    public double[] getDistancias() {
        return d;
    }

    public int[] getPredecessores() {
        return pi;
    }
}
