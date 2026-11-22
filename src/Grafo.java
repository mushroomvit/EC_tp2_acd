package src;

import java.util.List;

public interface Grafo {

    // tipo do grafo
    TipoGrafo tipo();

    // retorna vertices
    List<Vertice> vertices();

    // retorna arestas
    List<Aresta> arestas();

    // addiciona aresta sem peso
    default void adicionarAresta(int u, int v) {
        adicionarAresta(u, v, 1.0);
    }

    // adiciona uma aresta com peso
    void adicionarAresta(int u, int v, double peso);

    // retorna a aresta entre dois vertices
    Aresta obterAresta(int u, int v);

    // retorna arestas adjacentes a um vertice
    List<Aresta> adjacentes(int u);

    // retorna quantidade de vertices
    int numeroVertices();

    // retorna quantidade de arestas
    int numeroArestas();
}