# syntax=docker/dockerfile:1
FROM amd64/eclipse-temurin:20-jdk-alpine
WORKDIR /app
RUN apk update && apk upgrade && \
    apk add --no-cache git 
COPY . /app
RUN ./mvnw package
CMD ["java", "-jar", "target/spring-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081
