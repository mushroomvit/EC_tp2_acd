all:
	mkdir -p bin
	javac -d bin src_grafo/*.java
	java -cp bin src_grafo.main

clean:
	rm -rf bin