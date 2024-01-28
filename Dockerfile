FROM openjdk:17-jdk-slim
MAINTAINER batuhanakbas
COPY target/flight-search-0.0.1-SNAPSHOT.jar flight-search-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "flight-search-0.0.1-SNAPSHOT.jar"]
