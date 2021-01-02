FROM openjdk:11-oracle
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]