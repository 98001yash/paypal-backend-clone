FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

COPY event-contracts ./event-contracts
COPY auth-service ./auth-service
COPY discovery-server ./discovery-server
COPY api-gateway ./api-gateway
COPY merchant-service ./merchant-service
COPY account-service ./account-service
COPY ledger-service ./ledger-service
COPY balance-project-service ./balance-project-service
COPY order-service ./order-service
COPY payment-intent-service ./payment-intent-service
COPY payout-service ./payout-service
COPY user-service ./user-service

RUN mvn clean package -pl user-service -am -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/user-service/target/*.jar app.jar

EXPOSE 8091

ENTRYPOINT ["java", "-jar", "app.jar"]