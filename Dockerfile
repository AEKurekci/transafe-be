FROM openjdk:8-jre-alpine
RUN apk update && apk add bash
WORKDIR /app
COPY /lib/quasar.jar /app
EXPOSE 8085
CMD ["java", "-jar", "quasar.jar"]