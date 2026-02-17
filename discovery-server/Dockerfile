
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build


COPY pom.xml ./parent-pom.xml
RUN mvn -B -q install -f parent-pom.xml -N


COPY discovery-server/pom.xml pom.xml
COPY discovery-server/src src

RUN mvn -B -q clean package -DskipTests



FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java","-jar","/app/app.jar"]
