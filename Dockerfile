FROM eclipse-temurin:17
WORKDIR /home
COPY ./flowers ./flowers
COPY ./target/balwayscloud-0.0.1-SNAPSHOT.jar balwayscloud.jar
ENTRYPOINT ["java", "-jar", "balwayscloud.jar"]