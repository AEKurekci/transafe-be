FROM openjdk:8-jre-alpine
RUN apk update && apk add bash
WORKDIR /app
COPY build/tmp/runnodes.jar /app
EXPOSE 8085
CMD ["java", "-jar", "runnodes.jar"]