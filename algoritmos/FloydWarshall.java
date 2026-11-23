package algoritmos;

public class FloydWarshall {

    private double[][] D;
    private int[][] pi;

    public void executarFloydWarshall(double[][] W, int n) {
        // criar matriz D e matriz π de tamanho n×n
        D = new double[n][n]; 
        pi = new int[n][n];   

        // inicialização
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                D[i][j] = W[i][j]; 
                
                // se i = j ou W[i, j] = ∞ então π[i, j] = NIL (-1)
                if (i == j || W[i][j] == Double.POSITIVE_INFINITY) {
                    pi[i][j] = -1; 
                } else {
                    // senão π[i, j] = i
                    pi[i][j] = i; 
                }
            }
        }

        // processo iterativo do algoritmo
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // se D[i, k] + D[k, j] < D[i, j]
                    if (D[i][k] + D[k][j] < D[i][j]) {
                        D[i][j] = D[i][k] + D[k][j]; // D[i, j] ← D[i, k] + D[k, j]
                        pi[i][j] = pi[k][j];         // π[i, j] ← π[k, j]
                    }
                }
            }
        }
    }

    // função para imprimir o caminho mínimo
    public void imprimeCaminho(int i, int j) {
        if (i == j) {
            System.out.print(i + " "); // escrever i
        } else if (pi[i][j] == -1) {
            System.out.print("não existe caminho de " + i + " para " + j); // escrever "não existe caminho"
        } else {
            imprimeCaminho(i, pi[i][j]); // IMPRIME-CAMINHO(π, i, π[i, j])
            System.out.print(j + " ");   // escrever j
        }
    }

    public double[][] getMatrizDistancias() {
        return D;
    }

    public int[][] getMatrizPredecessores() {
        return pi;
    }
}