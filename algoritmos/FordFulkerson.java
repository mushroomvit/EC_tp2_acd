package algoritmos;

import java.util.ArrayDeque;
import src_grafo.Aresta;
import src_grafo.Grafo;

public class FordFulkerson {

    public static double calcular(Grafo grafo, int fonte, int sorvedouro) {
        if (fonte < 0 || sorvedouro < 0 || fonte >= grafo.numeroVertices() || sorvedouro >= grafo.numeroVertices()) {
            throw new IllegalArgumentException("Fonte ou sorvedouro invalidos.");
        }

        int n = grafo.numeroVertices();

        // matriz para armazenar as capacidades residuais das arestas
        double[][] capacidadeResidual = new double[n][n];

        // inicializa o grafo residual com as capacidades (pesos) do grafo original
        for (Aresta aresta : grafo.arestas()) {
            int u = aresta.getU().getId();
            int v = aresta.getV().getId();
            capacidadeResidual[u][v] = aresta.getPeso();
        }

        double fluxoMaximo = 0.0;
        int[] pai = new int[n]; // vetor para reconstruir o caminho aumentante encontrado pela BFS

        // continua enquanto houver um caminho da fonte ao sorvedouro
        while (bfs(n, capacidadeResidual, fonte, sorvedouro, pai)) {

            // encontra a capacidade residual minima ao longo do caminho encontrado
            double fluxoCaminho = Double.POSITIVE_INFINITY;
            int v = sorvedouro;
            while (v != fonte) {
                int u = pai[v];
                fluxoCaminho = Math.min(fluxoCaminho, capacidadeResidual[u][v]);
                v = pai[v];
            }

            // atualiza as capacidades residuais das arestas diretas e reversas
            v = sorvedouro;
            while (v != fonte) {
                int u = pai[v];
                capacidadeResidual[u][v] -= fluxoCaminho;
                capacidadeResidual[v][u] += fluxoCaminho; // adiciona fluxo na aresta reversa
                v = pai[v];
            }

            // adiciona o fluxo do caminho ao fluxo total
            fluxoMaximo += fluxoCaminho;
        }

        return fluxoMaximo;
    }

    // busca em largura (BFS) modificada para encontrar caminhos no grafo residual
    private static boolean bfs(int n, double[][] capacidadeResidual, int fonte, int sorvedouro, int[] pai) {
        boolean[] visitado = new boolean[n];
        ArrayDeque<Integer> fila = new ArrayDeque<>();

        fila.addLast(fonte);
        visitado[fonte] = true;
        pai[fonte] = -1;

        while (!fila.isEmpty()) {
            int u = fila.removeFirst();

            for (int v = 0; v < n; v++) {
                // considera a aresta apenas se houver capacidade residual > 0 e o vertice nao foi visitado
                if (!visitado[v] && capacidadeResidual[u][v] > 0) {
                    if (v == sorvedouro) {
                        pai[v] = u;
                        return true; // chegou no sorvedouro, caminho encontrado
                    }
                    visitado[v] = true;
                    pai[v] = u;
                    fila.addLast(v);
                }
            }
        }
        return false; // nao ha mais caminhos possiveis
    }
}