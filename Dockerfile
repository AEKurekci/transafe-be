FROM openjdk:8-jre-alpine
RUN apk update && apk add bash
WORKDIR /app
COPY /build/nodes/transafe-be.jar /app
EXPOSE 8085
CMD ["java", "-jar", "transafe-be.jar"]