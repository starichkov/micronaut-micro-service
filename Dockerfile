FROM eclipse-temurin:21.0.8_9-jre-alpine-3.22

RUN apk add --no-cache curl

WORKDIR /app
COPY target/micronaut-micro-service-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]