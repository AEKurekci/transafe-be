FROM openjdk:8-jre-alpine
RUN apk update && apk add bash
WORKDIR /app
COPY clients/build/libs/clients.jar /app
EXPOSE 8085 8081 80 8090
CMD ["java", "-jar", "clients.jar"]

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/clients.jar"]