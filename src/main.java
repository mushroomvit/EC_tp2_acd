package src;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import algoritmos.BellmanFord;
import algoritmos.BFS;
import algoritmos.DFS;
import algoritmos.OrdenacaoTopologica;

public class main {
    record Medicao(double[] distancias, long nanos) {}

    static Medicao medir(Grafo grafo, int fonte) {
        long inicio = System.nanoTime();
        double[] distancias = BellmanFord.calcular(grafo, fonte);
        return new Medicao(distancias, System.nanoTime() - inicio);
    }

    static void experimentar(Path pasta) throws IOException {
        List<Path> arquivos;
        try (Stream<Path> itens = Files.list(pasta)) {
            arquivos = itens.filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase(Locale.ROOT).endsWith(".gr"))
                    .sorted().toList();
        }
        if (arquivos.size() != 15) {
            throw new IOException("Esperados 15 arquivos .gr em " + pasta + ", encontrados " + arquivos.size());
        }
        List<String> linhas = new ArrayList<>();
        linhas.add("arquivo;vertices;arestas;algoritmo;representacao;origem;repeticao;tempo_ns");
        for (Path arquivo : arquivos) {
            Grafo grafo = LeitorGr.ler(arquivo, true);
            for (int vez = 1; vez <= 10; vez++) {
                Medicao resultado = medir(grafo, 0);
                linhas.add("\"" + arquivo.getFileName() + "\";" + grafo.numeroVertices() + ";" + grafo.numeroArestas()
                        + ";Bellman-Ford;matriz;1;" + vez + ";" + resultado.nanos());
            }
        }
        Path saida = Files.createTempFile(Path.of("."), "tempos-", ".csv");
        Files.write(saida, linhas);
        System.out.println("Resultados: " + saida.toAbsolutePath().normalize());
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 2 && args[0].equals("experimento")) {
            experimentar(Path.of(args[1]));
            return;
        }
        Scanner entrada = new Scanner(System.in);
        Grafo grafo = null;
        while (true) {
            System.out.println("1 Carregar .gr | 2 Bellman-Ford | 3 DFS | 4 BFS | 5 Ordenacao topologica | 6 Experimento | 0 Sair");
            System.out.print("Opção: ");
            if (!entrada.hasNextLine()) return;
            try {
                String opcao = entrada.nextLine().trim();
                switch (opcao) {
                    case "0" -> { return; }
                    case "1" -> {
                        System.out.print("Caminho do arquivo .gr: ");
                        grafo = LeitorGr.ler(Path.of(entrada.nextLine().trim()), true);
                        System.out.printf("Carregado: %d vertices, %d arestas.%n", grafo.numeroVertices(), grafo.numeroArestas());
                    }
                    case "2", "3", "4", "5" -> {
                        if (grafo == null) {
                            System.out.println("Carregue um grafo primeiro.");
                            continue;
                        }
                        System.out.println("Vertice de origem (1 a " + grafo.numeroVertices() + "):");
                        int fonte = Integer.parseInt(entrada.nextLine().trim());
                        if (fonte < 1 || fonte > grafo.numeroVertices()) throw new IllegalArgumentException("Origem invalida.");
                        if (!opcao.equals("2")) {
                            List<Integer> ordem = opcao.equals("4")
                                    ? BFS.buscar(grafo, fonte - 1) : DFS.buscar(grafo, fonte - 1);
                            System.out.println("Ordem de visita: " + ordem.stream().map(v -> v + 1).toList());
                            continue;
                        }
                        if (opcao.equals("5")) {
                             List<Integer> ordem = OrdenacaoTopologica.ordenar(grafo);
                            System.out.println("Ordem topologica: " + ordem.stream().map(v -> v + 1).toList());
                        }
                        Medicao resultado = medir(grafo, fonte - 1);
                        for (int i = 0; i < grafo.numeroVertices(); i++) {
                            System.out.println("Para " + (i + 1) + ": " + resultado.distancias()[i]);
                        }
                        System.out.println("Tempo: " + resultado.nanos() + " ns");
                    }
                    case "6" -> {
                        System.out.println("Pasta dos 15 arquivos .gr (Enter para grafos_samples):");
                        String pasta = entrada.nextLine().trim();
                        experimentar(Path.of(pasta.isEmpty() ? "grafos_samples" : pasta));
                    }
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (IOException | IllegalArgumentException erro) {
                System.out.println("Erro: " + erro.getMessage());
            } catch (NoSuchElementException fim) {
                return;
            }
        }
    }
}
