package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatrizAdjacencia implements Grafo {

    private final List<Vertice> vertices;
    private final List<Aresta> arestas;
    private final Aresta[][] matriz;
    private final TipoGrafo tipo;

    public MatrizAdjacencia(int numVertices, TipoGrafo tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException("O tipo do grafo não pode ser nulo.");
        }

        if (numVertices < 0) {
            throw new IllegalArgumentException(
                "A quantidade de vértices não pode ser negativa."
            );
        }

        this.tipo = tipo;

        vertices = new ArrayList<>();
        arestas = new ArrayList<>();

        matriz = new Aresta[numVertices][numVertices];

        // inicializa os vertices
        for (int i = 0; i < numVertices; i++) {
            vertices.add(new Vertice(i));
        }
    }

    @Override
    public TipoGrafo tipo() {
        return tipo;
    }

    @Override
    public List<Vertice> vertices() {
        return Collections.unmodifiableList(vertices);
    }

    @Override
    public List<Aresta> arestas() {
        return Collections.unmodifiableList(arestas);
    }

    @Override
    public void adicionarAresta(int u, int v, double peso) {

        validarVertice(u);
        validarVertice(v);

        if (obterAresta(u, v) != null) {
            throw new IllegalArgumentException(
                "Aresta já existente."
            );
        }

        Aresta aresta = new Aresta(
            vertices.get(u),
            vertices.get(v),
            peso
        );

        arestas.add(aresta);

        matriz[u][v] = aresta;

        if (tipo == TipoGrafo.NAO_DIRECIONADO) {
            matriz[v][u] = aresta;
        }
    }

    @Override
    public Aresta obterAresta(int u, int v) {

        validarVertice(u);
        validarVertice(v);

        return matriz[u][v];
    }

    @Override
    public List<Aresta> adjacentes(int u) {

        validarVertice(u);

        List<Aresta> adjacentes = new ArrayList<>();

        for (int v = 0; v < numeroVertices(); v++) {

            if (matriz[u][v] != null) {
                adjacentes.add(matriz[u][v]);
            }
        }

        return Collections.unmodifiableList(adjacentes);
    }

    @Override
    public int numeroVertices() {
        return vertices.size();
    }

    @Override
    public int numeroArestas() {
        return arestas.size();
    }

    private void validarVertice(int u) {

        if (u < 0 || u >= vertices.size()) {
            throw new IndexOutOfBoundsException(
                "Identificador de vértice inválido: " + u
            );
        }
    }
}