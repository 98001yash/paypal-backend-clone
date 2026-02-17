FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml ./parent-pom.xml
RUN mvn -B -q install -f parent-pom.xml -N

COPY event-contracts/pom.xml event-contracts/pom.xml
COPY event-contracts/src event-contracts/src

RUN mvn -B -q install \
    -f event-contracts/pom.xml \
    -DskipTests


COPY auth-service/pom.xml pom.xml
COPY auth-service/src src

RUN mvn -B -q clean package -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java","-jar","/app/app.jar"]
