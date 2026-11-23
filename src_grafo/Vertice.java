package src_grafo;

public class Vertice {

    private final int id;
    private String label;

    // construtor sem rotulo
    public Vertice(int id) {
        this.id = id;
        this.label = String.valueOf(id);
    }

    // construtor com rótulo
    public Vertice(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}