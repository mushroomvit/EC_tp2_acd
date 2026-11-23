package algoritmos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import src_grafo.Aresta;
import src_grafo.Grafo;

// estrutura de conjuntos disjuntos (Union-Find)
class UnionFind {
    private int[] pai;
    private int[] rank;

    public UnionFind(int n) {
        pai = new int[n];
        rank = new int[n];

        // inicialmente, cada vertice e pai de si mesmo
        for (int i = 0; i < n; i++) {
            pai[i] = i;
            rank[i] = 0;
        }
    }

    // encontra a raiz do conjunto com compressao de caminho
    public int encontrar(int i) {
        if (pai[i] == i) {
            return i;
        }
        pai[i] = encontrar(pai[i]);
        return pai[i];
    }

    // une dois conjuntos. retorna false se ja estiverem no mesmo conjunto (ciclo)
    public boolean unir(int i, int j) {
        int raizI = encontrar(i);
        int raizJ = encontrar(j);

        if (raizI != raizJ) {
            // uniao baseada no rank para manter a arvore balanceada
            if (rank[raizI] < rank[raizJ]) {
                pai[raizI] = raizJ;
            } else if (rank[raizI] > rank[raizJ]) {
                pai[raizJ] = raizI;
            } else {
                pai[raizJ] = raizI;
                rank[raizI]++;
            }
            return true; // aresta adicionada com sucesso
        }
        return false; // forma ciclo, aresta rejeitada
    }
}

public class Kruskal {

    public static ResultadoKruskal executar(Grafo grafo) {
        List<Aresta> todasArestas = new ArrayList<>();

        // extrai todas as arestas utilizando a interface base do grafo
        for (Aresta aresta : grafo.arestas()) {
            todasArestas.add(aresta);
        }

        // ordena as arestas em ordem crescente de peso (metodo guloso)
        todasArestas.sort(Comparator.comparingDouble(Aresta::getPeso));

        // inicializa a estrutura de conjuntos disjuntos
        UnionFind uf = new UnionFind(grafo.numeroVertices());

        List<Aresta> arvoreGeradora = new ArrayList<>();
        double custoTotal = 0.0;

        // itera sobre as arestas ordenadas
        for (Aresta aresta : todasArestas) {
            int u = aresta.getU().getId(); // obtem ID do vertice de origem
            int v = aresta.getV().getId(); // obtem ID do vertice de destino

            // se unir os vertices nao formar um ciclo, adiciona a arvore geradora minima (AGM)
            if (uf.unir(u, v)) {
                arvoreGeradora.add(aresta);
                custoTotal += aresta.getPeso();

                // a AGM de um grafo conexo tem V - 1 arestas
                if (arvoreGeradora.size() == grafo.numeroVertices() - 1) {
                    break;
                }
            }
        }

        return new ResultadoKruskal(arvoreGeradora, custoTotal);
    }

    // classe auxiliar para armazenar o resultado final (arestas + custo)
    public static class ResultadoKruskal {
        private List<Aresta> arvore;
        private double custo;

        public ResultadoKruskal(List<Aresta> arvore, double custo) {
            this.arvore = arvore;
            this.custo = custo;
        }

        public List<Aresta> getArvore() {
            return arvore;
        }

        public double getCustoTotal() {
            return custo;
        }
    }
}