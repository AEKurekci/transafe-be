#!/bin/sh
./gradlew bootRunNode1 &
./gradlew bootRunNode2
./gradlew --stop
killall java -9
../build/nodes/runnodes