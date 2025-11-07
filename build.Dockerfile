FROM eclipse-temurin:21-jdk-alpine-3.22 AS build

WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre-alpine-3.22

RUN apk add --no-cache curl

WORKDIR /app
COPY --from=build /app/target/micronaut-micro-service-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]