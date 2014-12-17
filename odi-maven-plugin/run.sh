#!/bin/bash

echo "Hard to run without me..."

echo
export JAVA_HOME=/Library/Java/JavaVirtualMachines/1.6.0_29-b11-402.jdk/Contents/Home
echo $JAVA_HOME
echo

cd target/classes/

java -cp /u01/app/oracle/odi11116/oracledi.sdk/lib/*:\
/u01/app/oracle/calmlow-own-libs/ojdbc6.jar:\
/u01/app/oracle/odi11116/modules/oracle.jps_11.1.1/*:. \
com/github/calmlow/instructions/GenerateScenario
