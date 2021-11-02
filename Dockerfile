FROM maven:3.6.3-jdk-11 as build
WORKDIR /app
ADD pom.xml .
RUN mvn dependency:go-offline
ADD . .
RUN mvn clean package

FROM adoptopenjdk:11-jre-hotspot as runtime
WORKDIR /app
COPY --from=build /app/target/app.jar ./
USER 1000:1000
ENTRYPOINT ["java", "-jar", "app.jar"]
