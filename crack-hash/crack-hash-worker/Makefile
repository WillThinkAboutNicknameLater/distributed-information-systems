docker:
	./mvnw clean
	./mvnw compile
	./mvnw package -Dmaven.test.skip
	docker build . -t crackhash/crack-hash-worker:0.0.1-SNAPSHOT