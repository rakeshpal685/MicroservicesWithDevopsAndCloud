#Start with the base image containing java runtime
FROM openjdk:17-jdk-slim

MAINTAINER Rakesh Pal

#This will copy our jar file from the target folder to the container's folder
#(Here it will copy it to the containers home folder)
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar


#When someone is creating a container out of this image then Inside the container this will be the first command to get executed
ENTRYPOINT ["java","-jar","accounts-0.0.1-SNAPSHOT.jar"]