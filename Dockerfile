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
COPY build/nodes/Node1/cordapps/accounts-contracts-1.0.jar /app
COPY build/nodes/Node1/cordapps/accounts-workflows-1.0.jar /app
COPY build/nodes/Node1/cordapps/ci-workflows-1.0.jar /app
COPY build/nodes/Node1/cordapps/contracts.jar /app
COPY build/nodes/Node1/cordapps/workflows.jar /app

EXPOSE 8085 8081 80 8090

CMD ["java", "-jar", "corda.jar"]