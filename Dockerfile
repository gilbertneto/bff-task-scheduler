


FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/bff-scheduler-0.0.1-SNAPSHOT.jar /app/bff-scheduler.jar

EXPOSE 8083

CMD ["java", "-jar", "/app/bff-scheduler.jar"]