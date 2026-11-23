# TP2 ACD - Boxplots de Bellman-Ford com matriz de adjacencia
reset

arquivo = "../resultados/resultados.csv"
saida   = "../resultados/graficos/"
system("test -d '". saida . "' || mkdir -- '" . saida . "'")

set terminal pngcairo size 1280,720 font "Arial,16"
set datafile separator ";"
set style data boxplot
set style fill solid 0.65 border
set boxwidth 0.5
unset key

set grid ytics
set xlabel "Densidade nominal do grafo"
set ylabel "Tempo de execucao (ms)"
set xrange [0.5:5.5]
set yrange [0:*]
set xtics ("20%%" 1, "40%%" 2, "60%%" 3, "80%%" 4, "100%%" 5)

# Nome do sample para cada tamanho e densidade.
sample(n, i) = sprintf("sample%d-%d.gr", int(n), int(n*(n-1)*i/5))

do for [v=1:3] {
    n = (v == 1 ? 100 : v == 2 ? 200 : 500)

    set output sprintf("%sboxplot_Bellman-Ford_matriz_%d.png", saida, n)
    set title sprintf("Bellman-Ford | Matriz de adjacencia | %d vertices", n)

    plot for [i=1:5] arquivo skip 1 using (i): \
        (strcol(1) eq sample(n,i) && \
         strcol(4) eq "Bellman-Ford" && \
         strcol(5) eq "matriz" && \
         $6 == 1 ? $8 / 1e6 : 1/0) \
        with boxplot lc rgb "#3973AC"

    unset output
}