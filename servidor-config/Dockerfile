FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml ./
COPY servidor-config/pom.xml servidor-config/pom.xml
COPY servico-funcionario/pom.xml servico-funcionario/pom.xml
COPY servico-categoria-epi/pom.xml servico-categoria-epi/pom.xml
COPY servico-retirada-epi/pom.xml servico-retirada-epi/pom.xml
RUN mvn -pl servidor-config -am dependency:go-offline
COPY servidor-config/src servidor-config/src
RUN mvn -pl servidor-config -am package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/servidor-config/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
