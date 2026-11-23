package src_grafo;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import algoritmos.BellmanFord;
import algoritmos.BFS;
import algoritmos.DFS;
import algoritmos.OrdenacaoTopologica;
import algoritmos.Dijkstra;
import algoritmos.FloydWarshall;
import algoritmos.FordFulkerson;
import algoritmos.Kruskal;
import algoritmos.Prim;

public class main {
    record Medicao(double[] distancias, long nanos) {}

    static Medicao medirBellmanFord(Grafo grafo, int fonte) {
        long inicio = System.nanoTime();
        double[] distancias = BellmanFord.calcular(grafo, fonte);
        return new Medicao(distancias, System.nanoTime() - inicio);
    }

    static Medicao medirDijkstra(Grafo grafo, int fonte) {
        Dijkstra algoritmo = new Dijkstra();
        long inicio = System.nanoTime();
        algoritmo.executarDijkstra(grafo, fonte);
        long nanos = System.nanoTime() - inicio;
        return new Medicao(algoritmo.getDistancias(), nanos);
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
        coletarResultados(arquivos, true);
        coletarResultados(arquivos, false);
    }

    static void coletarResultados(List<Path> arquivos, boolean matriz) throws IOException {
        String representacao = matriz ? "matriz" : "lista";
        String cabecalho = "arquivo;vertices;arestas;algoritmo;representacao;origem;repeticao;tempo_ns";
        List<String> linhasBellmanFord = new ArrayList<>();
        List<String> linhasDijkstra = new ArrayList<>();
        linhasBellmanFord.add(cabecalho);
        linhasDijkstra.add(cabecalho);

        for (Path arquivo : arquivos) {
            Grafo grafo = LeitorGr.ler(arquivo, matriz);
            String nome = arquivo.getFileName().toString().replace("\"", "\"\"");
            String entrada = "\"" + nome + "\";" + grafo.numeroVertices() + ";" + grafo.numeroArestas();
            for (int vez = 1; vez <= 10; vez++) {
                Medicao bellmanFord = medirBellmanFord(grafo, 0);
                Medicao dijkstra = medirDijkstra(grafo, 0);
                String configuracao = ";" + representacao + ";1;" + vez + ";";
                linhasBellmanFord.add(entrada + ";Bellman-Ford" + configuracao + bellmanFord.nanos());
                linhasDijkstra.add(entrada + ";Dijkstra" + configuracao + dijkstra.nanos());
            }
        }
        salvarResultados(Path.of("resultados", "resultadosBellmanFord"), representacao, linhasBellmanFord);
        salvarResultados(Path.of("resultados", "resultadosDijkstra"), representacao, linhasDijkstra);
    }

    static void salvarResultados(Path pasta, String representacao, List<String> linhas) throws IOException {
        Files.createDirectories(pasta);
        Path saida = pasta.resolve("resultados_" + representacao + ".csv");
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
            System.out.println("1 Carregar .gr");
            System.out.println("2 Bellman-Ford");
            System.out.println("3 DFS");
            System.out.println("4 BFS");
            System.out.println("5 Ordenacao topologica");
            System.out.println("6 Dijkstra");
            System.out.println("7 Floyd-Warshall");
            System.out.println("8 Ford-Fulkerson");
            System.out.println("9 Kruskal");
            System.out.println("10 Prim");
            System.out.println("11 Experimento");
            System.out.println("0 Sair");
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
                    case "2", "3", "4", "5", "6", "7", "8", "9", "10" -> {
                        if (grafo == null) {
                            System.out.println("Carregue um grafo primeiro.");
                            continue;
                        }
                        System.out.println("Vertice de origem (1 a " + grafo.numeroVertices() + "):");
                        int fonte = Integer.parseInt(entrada.nextLine().trim());
                        if (fonte < 1 || fonte > grafo.numeroVertices()) throw new IllegalArgumentException("Origem invalida.");
                        if (opcao.equals("2")) {
                            double[] distancias = BellmanFord.calcular(grafo, fonte - 1);
                            System.out.println("Distancias: " + Arrays.toString(distancias));
                        }
                        else if (opcao.equals("3") || opcao.equals("4")) {
                            List<Integer> ordem = opcao.equals("4")
                                    ? BFS.buscar(grafo, fonte - 1) : DFS.buscar(grafo, fonte - 1);
                            System.out.println("Ordem de visita: " + ordem.stream().map(v -> v + 1).toList());
                            continue;
                        }
                        else if (opcao.equals("5")) {
                             List<Integer> ordem = OrdenacaoTopologica.ordenar(grafo);
                            System.out.println("Ordem topologica: " + ordem.stream().map(v -> v + 1).toList());
                        }
                        else if (opcao.equals("6")) {
                            Dijkstra dijkstra = new Dijkstra();
                            dijkstra.executarDijkstra(grafo, fonte - 1);
                            System.out.println("Dijkstra: " + Arrays.toString(dijkstra.getDistancias()));
                        } else if (opcao.equals("7")) {
                            int n = grafo.numeroVertices();
                            double[][] pesos = new double[n][n];
                            for (int u = 0; u < n; u++) {
                                for (int v = 0; v < n; v++) {
                                    Aresta aresta = grafo.obterAresta(u, v);
                                    pesos[u][v] = aresta == null ? Double.POSITIVE_INFINITY : aresta.getPeso();
                                }
                                pesos[u][u] = Math.min(0, pesos[u][u]);
                            }
                            FloydWarshall floyd = new FloydWarshall();
                            floyd.executarFloydWarshall(pesos, n);
                            for (double[] linha : floyd.getMatrizDistancias()) {
                                System.out.println(Arrays.toString(linha));
                            }
                        } else if (opcao.equals("8")) {
                            int sorvedouro = grafo.numeroVertices() - 1;
                            if (fonte - 1 == sorvedouro) throw new IllegalArgumentException("Fonte e sorvedouro devem ser distintos.");
                            double fluxo = FordFulkerson.calcular(grafo, fonte - 1, sorvedouro);
                            System.out.println("Fluxo maximo: " + fluxo);
                        } else if (opcao.equals("9")) {
                            Kruskal.ResultadoKruskal resultado = Kruskal.executar(grafo);
                            System.out.println("Kruskal: " + resultado.getArvore());
                            System.out.println("Custo total: " + resultado.getCustoTotal());
                        } else if (opcao.equals("10")) {
                            Prim prim = new Prim();
                            prim.executarPrim(grafo, fonte - 1);
                            System.out.println("Prim (pesos de ligacao): " + Arrays.toString(prim.getChaves()));
                        }
                        Medicao resultado = medirBellmanFord(grafo, fonte - 1);
                        for (int i = 0; i < grafo.numeroVertices(); i++) {
                            System.out.println("Para " + (i + 1) + ": " + resultado.distancias()[i]);
                        }
                        System.out.println("Tempo: " + resultado.nanos() + " ns");
                    }
                    case "11" -> {
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
