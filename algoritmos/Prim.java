package algoritmos;

import java.util.PriorityQueue;
import src_grafo.Grafo;
import src_grafo.Aresta;

public class Prim {

    private double[] chave;
    private int[] pi;

    public void executarPrim(Grafo grafo, int raiz) {
        int n = grafo.numeroVertices();
        chave = new double[n];
        pi = new int[n];

        // simula a verificação de pertencimento à fila Q estipulada pelo invariante do algoritmo.
        // um vértice com valor 'true' já faz parte da árvore geradora mínima (não está mais em Q).
        boolean[] naArvore = new boolean[n];

        // inicialização: para cada vértice u ∈ G.V, u.chave = ∞ e u.π = NIL
        for (int i = 0; i < n; i++) {
            chave[i] = Double.POSITIVE_INFINITY;
            pi[i] = -1;
        }

        // r.chave = 0
        chave[raiz] = 0;

        // fila de prioridade mínima baseada no atributo chave
        PriorityQueue<NoVertice> Q = new PriorityQueue<>();
        Q.add(new NoVertice(raiz, 0.0));

        while (!Q.isEmpty()) {
            // extrai o vértice u (mínimo) da fila de prioridade
            NoVertice atual = Q.poll();
            int u = atual.id;

            // se o vértice já foi consolidado na árvore, ignoramos as instâncias duplicadas (obsoletas) da fila
            if (naArvore[u]) continue;

            // consolida o vértice na árvore
            naArvore[u] = true;

            // para cada vértice v em G.Adj[u]
            for (Aresta aresta : grafo.adjacentes(u)) {
                int v = aresta.getU().getId() == u ? aresta.getV().getId() : aresta.getU().getId();
                double pesoUV = aresta.getPeso();

                // se v ∈ Q e w(u, v) < v.chave
                if (!naArvore[v] && pesoUV < chave[v]) {
                    pi[v] = u;         // v.π = u
                    chave[v] = pesoUV; // v.chave = w(u, v)

                    // substitui a operação DIMINUI-CHAVE inserindo a nova chave atualizada na fila
                    Q.add(new NoVertice(v, chave[v]));
                }
            }
        }
    }

    public double[] getChaves() {
        return chave;
    }

    public int[] getPredecessores() {
        return pi;
    }
}
