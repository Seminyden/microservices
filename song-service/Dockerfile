# build
FROM maven:latest as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean test package -Dmaven.test.skip=false

# runtime
FROM eclipse-temurin:latest
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8072
CMD ["java", "-jar", "app.jar"]