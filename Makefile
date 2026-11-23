all:
	mkdir -p bin
	javac -d bin src/*.java
	java -cp bin src.main

clean:
	rm -rf bin