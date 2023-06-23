FROM openjdk:11-jre-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/service.jar"]