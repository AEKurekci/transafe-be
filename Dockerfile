FROM openjdk:8-jre-alpine
RUN apk update && apk add bash
WORKDIR /app
COPY build/nodes/Node1/corda.jar /app
EXPOSE 8085
CMD ["java", "-jar", "corda.jar"]