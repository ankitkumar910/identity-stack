FROM eclipse-temurin:21-jdk
LABEL authors="ankit kumar"

WORKDIR /app
COPY /target/identity-stack.jar .
ENTRYPOINT ["java", "-jar","identity-stack.jar"]