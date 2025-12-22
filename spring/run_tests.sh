#!/bin/bash
cd "$(dirname "$0")"

echo "Building application and tests..."
mkdir -p bin lib
find src/main/java -name "*.java" > sources.txt
find src/test/java -name "*.java" >> sources.txt
javac -cp "lib/*" -d bin @sources.txt 2>/dev/null
rm sources.txt

if [ $? -ne 0 ]; then
  echo "Build failed!"
  exit 1
fi

echo "Running unit tests..."
java -cp "bin:lib/*" com.ticketvoyage.DataServiceTest

echo "Running integration tests..."
java -cp "bin:lib/*" com.ticketvoyage.IntegrationTest

echo "Running functional tests..."
java -cp "bin:lib/*" com.ticketvoyage.FunctionalTest

echo "Running performance tests..."
java -cp "bin:lib/*" com.ticketvoyage.PerformanceTest

echo "All tests completed!"
