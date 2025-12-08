для запуска через терминал необходимо написать в терминал : 
cd /Users/admin/Documents/GitHub/ticket-system-desktop/spring
mkdir -p bin lib
javac -d bin $(find src/main/java -name "*.java")
java -cp "bin:lib/*" com.ticketvoyage.Main

второй вариант для запуска это скрипт, который находиться в директории spring: run.sh 

временные пользователи: user user123