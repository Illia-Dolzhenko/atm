FROM openjdk:17-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=target/atm-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} atm.jar
ENTRYPOINT ["java","-jar","/atm.jar"]