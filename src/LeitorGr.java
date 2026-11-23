package src;

import java.io.*;
import java.nio.file.*;
import java.util.*;

class LeitorGr {
    static Grafo ler(Path arquivo, boolean matriz) throws IOException {
        Grafo grafo = null;
        record Par(int origem, int destino) {}
        Map<Par, Double> pesos = new LinkedHashMap<>();
        int lidas = 0;
        int vertices = 0, esperadas = -1, numero = 0;
        try (BufferedReader leitor = Files.newBufferedReader(arquivo)) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                numero++;
                linha = linha.strip();
                if (linha.isEmpty()) continue;
                String[] campos = linha.split("\\s+");
                if (campos[0].equals("c")) continue;
                if (campos.length != 4) throw new IllegalArgumentException();
                if (campos[0].equals("p") && campos[1].equals("sp") && esperadas == -1) {
                    vertices = Integer.parseInt(campos[2]);
                    esperadas = Integer.parseInt(campos[3]);
                    if (vertices < 1 || esperadas < 0) throw new IllegalArgumentException();
                    grafo = matriz ? new MatrizAdjacencia(vertices, TipoGrafo.DIRECIONADO)
                            : new ListaAdjacencia(vertices, TipoGrafo.DIRECIONADO);
                } else if (campos[0].equals("a") && grafo != null && lidas < esperadas) {
                    int origem = Integer.parseInt(campos[1]);
                    int destino = Integer.parseInt(campos[2]);
                    double peso = Double.parseDouble(campos[3]);
                    if (origem < 1 || destino < 1 || origem > vertices || destino > vertices
                            || !Double.isFinite(peso)) throw new IllegalArgumentException();
                    pesos.merge(new Par(origem - 1, destino - 1), peso, Math::min);
                    lidas++;
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } catch (IllegalArgumentException erro) {
            throw new IOException(arquivo + ", linha " + numero + ": registro invalido.", erro);
        }
        if (grafo == null || lidas != esperadas) throw new IOException(arquivo + ": arquivo incompleto.");
        for (var item : pesos.entrySet()) {
            Par par = item.getKey();
            grafo.adicionarAresta(par.origem(), par.destino(), item.getValue());
        }
        return grafo;
    }
}