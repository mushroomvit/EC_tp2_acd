package src_grafo;

import java.util.Scanner;

// classe para testar a implementação - será ajustada para o main do trabalho depois
public class Teste {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Grafo grafo = null;

        System.out.println("=== TESTE DE GRAFOS ===");

        // escolha do tipo
        System.out.println("\nEscolha o tipo de grafo:");
        System.out.println("1 - Direcionado");
        System.out.println("2 - Não direcionado");
        System.out.print("Opção: ");

        int tipoOpcao = scanner.nextInt();

        TipoGrafo tipo;

        if (tipoOpcao == 1) {
            tipo = TipoGrafo.DIRECIONADO;
        } else if (tipoOpcao == 2) {
            tipo = TipoGrafo.NAO_DIRECIONADO;
        } else {
            System.out.println("Opção inválida.");
            scanner.close();
            return;
        }

        // escolha da representação
        System.out.println("\nEscolha a representação:");
        System.out.println("1 - Lista de adjacência");
        System.out.println("2 - Matriz de adjacência");
        System.out.print("Opção: ");

        int representacao = scanner.nextInt();

        // quantidade de vertices
        System.out.print("\nQuantidade de vértices: ");
        int numVertices = scanner.nextInt();

        if (numVertices < 0) {
            System.out.println("Quantidade inválida.");
            scanner.close();
            return;
        }

        // instanciação do grafo
        if (representacao == 1) {
            grafo = new ListaAdjacencia(numVertices, tipo);
        } else if (representacao == 2) {
            grafo = new MatrizAdjacencia(numVertices, tipo);
        } else {
            System.out.println("Representação inválida.");
            scanner.close();
            return;
        }

        System.out.println("\nGrafo criado com sucesso!");

        int opcao;

        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1 - Adicionar aresta");
            System.out.println("2 - Listar vértices");
            System.out.println("3 - Listar arestas");
            System.out.println("4 - Consultar aresta");
            System.out.println("5 - Consultar adjacentes");
            System.out.println("6 - Exibir quantidade de vértices e arestas");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            opcao = scanner.nextInt();

            switch (opcao) {

                case 1:
                    System.out.print("Vértice de origem: ");
                    int u = scanner.nextInt();

                    System.out.print("Vértice de destino: ");
                    int v = scanner.nextInt();

                    System.out.print("Peso da aresta: ");
                    double peso = scanner.nextDouble();

                    try {
                        grafo.adicionarAresta(u, v, peso);
                        System.out.println("Aresta adicionada com sucesso!");
                    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.println("\nVértices:");
                    System.out.println(grafo.vertices());
                    break;

                case 3:
                    System.out.println("\nArestas:");
                    System.out.println(grafo.arestas());
                    break;

                case 4:
                    System.out.print("Vértice de origem: ");
                    u = scanner.nextInt();

                    System.out.print("Vértice de destino: ");
                    v = scanner.nextInt();

                    try {
                        Aresta aresta = grafo.obterAresta(u, v);

                        if (aresta != null) {
                            System.out.println("Aresta encontrada: " + aresta);
                        } else {
                            System.out.println("Não existe aresta entre esses vértices.");
                        }
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.print("Identificador do vértice: ");
                    u = scanner.nextInt();

                    try {
                        System.out.println("Arestas adjacentes: " + grafo.adjacentes(u));
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                    break;

                case 6:
                    System.out.println("\nTipo: " + grafo.tipo());
                    System.out.println("Número de vértices: " + grafo.numeroVertices());
                    System.out.println("Número de arestas: " + grafo.numeroArestas());
                    break;

                case 0:
                    System.out.println("Encerrando o programa...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);

        scanner.close();
    }
}