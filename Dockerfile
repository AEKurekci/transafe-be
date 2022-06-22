FROM openjdk:8-jre-alpine
RUN apk update && apk add bash
WORKDIR /app
COPY build/nodes/Node1/corda.jar /app
COPY build/nodes/Node1_node.conf /app
COPY build/nodes/Node2_node.conf /app
COPY build/nodes/Notary_node.conf /app
COPY build/nodes/Node1/node.conf /app
COPY build/nodes/Node1/persistence.mv.db /app
COPY build/nodes/Node1/persistence.trace.db /app
COPY build/nodes/Node1/process-id /app
COPY build/nodes/Node1/network-parameters /app
COPY build/nodes/runnodes.jar /app
COPY build/nodes/Node1_node.conf /app
COPY build/nodes/Node2_node.conf /app
COPY build/nodes/Notary_node.conf /app
COPY build/nodes/runnodes /app
COPY build/nodes/runnodes.bat /app
EXPOSE 8085
CMD ["java", "-jar", "corda.jar"]
CMD ["java", "-jar", "runnodes.jar"]