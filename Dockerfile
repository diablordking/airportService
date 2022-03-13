FROM openjdk:17.0.1-jdk-slim
EXPOSE 11010
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]