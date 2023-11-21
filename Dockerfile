ARG JAVA_VERSION=17
FROM openjdk:${JAVA_VERSION}
COPY target/*.jar test-estimate-hub.jar
EXPOSE 8999
CMD ["java","-jar","/test-estimate-hub.jar"]
