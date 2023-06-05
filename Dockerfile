
FROM openjdk:17
COPY build/libs/first-project-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]