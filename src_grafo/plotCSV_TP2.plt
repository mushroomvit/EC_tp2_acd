# Executar a partir de src_grafo: gnuplot plotCSV_TP2.plt
reset
base = "../resultados"
saida = base . "/graficos"
system(sprintf("mkdir -p '%s'", saida))

set terminal pngcairo size 1500,640 font "Arial,19"
set datafile separator ";"
set style boxplot range 1.5 outliers
set style fill solid 0.65 border
set boxwidth 0.5
unset key
set grid ytics
set xrange [0.5:5.5]
set xtics ("20%%" 1, "40%%" 2, "60%%" 3, "80%%" 4, "100%%" 5)
set xlabel "Densidade nominal do grafo"
set ylabel "Tempo de execucao (ms)"

# Identifica a densidade pelo nome oficial, anterior ao tratamento de arcos repetidos.

sample(n,i) = sprintf("sample%d-%d.gr", n, int(n*(n-1)*i/5))

do for [alg in "BellmanFord Dijkstra"] {
    nome = alg eq "BellmanFord" ? "Bellman-Ford" : "Dijkstra"
    pasta = base . "/resultados" . alg
    do for [rep in "lista matriz"] {
        arquivo = pasta . "/resultados_" . rep . ".csv"
        set output sprintf("%s/boxplot_%s_%s.png", saida, nome, rep)
        set multiplot layout 1,3 title sprintf("%s | %s de adjacencia", nome, rep)
        do for [t=1:3] {
            n = t == 1 ? 100 : (t == 2 ? 200 : 500)
            set title sprintf("%d vertices", n)
            set yrange [0:*]
            plot for [i=1:5] arquivo skip 1 using (i): \
                (strcol(1) eq sample(n,i) && strcol(4) eq nome && strcol(5) eq rep && \
                 $6 == 1 ? $8 / 1e6 : 1/0) with boxplot lc rgb "#3973AC"
        }
        unset multiplot
        unset output
    }
}
