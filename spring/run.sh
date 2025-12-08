#!/bin/bash
cd "$(dirname "$0")"
mkdir -p bin lib
find src/main/java -name "*.java" > sources.txt
javac -d bin @sources.txt
COMP_CODE=$?
rm sources.txt
if [ $COMP_CODE -ne 0 ]; then
  exit $COMP_CODE
fi
java -cp "bin:lib/*" com.ticketvoyage.Main
