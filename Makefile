all:
	mkdir -p bin
	javac -d bin src/*.java
	java -cp bin src.Teste

clean:
	rm -rf bin