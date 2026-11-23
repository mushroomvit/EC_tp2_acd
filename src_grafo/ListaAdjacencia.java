package src_grafo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaAdjacencia implements Grafo {

    private final List<Vertice> vertices;
    private final List<Aresta> arestas;
    private final List<List<Aresta>> adj;
    private final TipoGrafo tipo;

    // construtor
    public ListaAdjacencia(int numVertices, TipoGrafo tipo) {

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
        adj = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {

            vertices.add(new Vertice(i));

            adj.add(new ArrayList<>());
        }
    }

    // retorna tipo do grafo
    @Override
    public TipoGrafo tipo() {
        return tipo;
    }

    // retorna lista de vertices
    @Override
    public List<Vertice> vertices() {
        return Collections.unmodifiableList(vertices);
    }

    // retorna lista de arestas
    @Override
    public List<Aresta> arestas() {
        return Collections.unmodifiableList(arestas);
    }

    // adiciona aresta com peso
    @Override
    public void adicionarAresta(int u, int v, double peso) {

        validarVertice(u);
        validarVertice(v);

        // ve se aresta já existe
        if (obterAresta(u, v) != null) {
            throw new IllegalArgumentException(
                "Aresta já existente."
            );
        }

        // cria aresta com os vertices que hja existem
        Aresta aresta = new Aresta(
            vertices.get(u),
            vertices.get(v),
            peso
        );

        arestas.add(aresta);

        adj.get(u).add(aresta);

        // em grafos não direcionados adiciona em v tambem
        if (tipo == TipoGrafo.NAO_DIRECIONADO && u != v) {
            adj.get(v).add(aresta);
        }
    }

    @Override
    public Aresta obterAresta(int u, int v) {

        validarVertice(u);
        validarVertice(v);

        for (Aresta aresta : adj.get(u)) {

            int origem = aresta.getU().getId();
            int destino = aresta.getV().getId();

            if (origem == u && destino == v) {
                return aresta;
            }

            if (tipo == TipoGrafo.NAO_DIRECIONADO
                    && origem == v && destino == u) {
                return aresta;
            }
        }

        return null;
    }

    @Override
    public List<Aresta> adjacentes(int u) {

        validarVertice(u);

        return Collections.unmodifiableList(adj.get(u));
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