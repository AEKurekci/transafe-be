FROM openjdk:8-jre-alpine
RUN apk update && apk add bash
WORKDIR /app
COPY build/nodes/Node1/corda.jar /app
COPY build/nodes/Node1_node.conf /app
COPY build/nodes/Node2_node.conf /app
COPY build/nodes/Notary_node.conf /app
EXPOSE 8085
CMD ["java", "-jar", "corda.jar"]