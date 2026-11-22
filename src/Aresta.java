package src;

public class Aresta {

    private final Vertice u;
    private final Vertice v;
    private final double peso;

    // construtor sem peso
    public Aresta(Vertice u, Vertice v) {
        this(u, v, 1.0);
    }

    // construtor com peso
    public Aresta(Vertice u, Vertice v, double peso) {
        this.u = u;
        this.v = v;
        this.peso = peso;
    }

    public Vertice getU() {
        return u;
    }

    public Vertice getV() {
        return v;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "(" + u.getId() + ", " + v.getId() + ", " + peso + ")";
    }
}
