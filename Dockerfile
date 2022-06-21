FROM openjdk:8-jre-alpine
RUN apk update && apk add bash
WORKDIR /app
COPY /clients/build/libs/clients.jar /app
EXPOSE 8085
CMD ["java", "-jar", "clients.jar"]