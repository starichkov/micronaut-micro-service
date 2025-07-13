FROM eclipse-temurin:21.0.7_6-jdk-alpine-3.21 AS build

WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21.0.7_6-jre-alpine-3.21

WORKDIR /app
COPY --from=build /app/target/micronaut-micro-service-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]