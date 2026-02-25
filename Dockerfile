FROM eclipse-temurin:25-jre-alpine-3.23

RUN apk add --no-cache curl

WORKDIR /app
COPY target/micronaut-micro-service-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]