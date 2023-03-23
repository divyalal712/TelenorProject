
FROM openjdk:17
COPY build/libs/healthcheck-1.0-SNAPSHOT.jar app.jar
CMD ["java","-jar","app.jar"]
# EXPOSE 8080