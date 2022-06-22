FROM openjdk:8-jre-alpine
RUN apk update && apk add bash
WORKDIR /app
COPY build/nodes/Node1/corda.jar /app
COPY build/nodes/Node1_node.conf /app
COPY build/nodes/Node1/node.conf /app
COPY build/nodes/Node1/persistence.mv.db /app
COPY build/nodes/Node1/persistence.trace.db /app
COPY build/nodes/Node1/process-id /app
COPY build/nodes/Node1/network-parameters /app
COPY build/nodes/Notary/corda.jar /app
ADD  build/nodes/Node1/cordapps /app
ADD  build/nodes/Node1/drivers /app
ADD  build/nodes/Node1/certificates /app
ADD  build/nodes/Node1/additional-node-infos /app
ADD  build/nodes/Node1/djvm /app

EXPOSE 8085 8081 80 8090

CMD ["java", "-jar", "corda.jar"]