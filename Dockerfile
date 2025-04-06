# Pulling the jdk image from the Docker Hub
FROM eclipse-temurin:17-jdk-alpine

#Copying or Getting the Jar file that is built using mvn clean package
COPY target/devops-0.0.1-SNAPSHOT.jar devops.jar

#Exposing the Port that the app run in the container to host port
EXPOSE 8080

#Command used to run the Jar file while running the image as a Container
ENTRYPOINT ["java","-jar","devops.jar"]