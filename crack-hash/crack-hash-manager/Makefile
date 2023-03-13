docker:
	./mvnw clean
	./mvnw compile
	./mvnw package -Dmaven.test.skip
	docker build . -t crackhash/crack-hash-manager:0.0.1-SNAPSHOT