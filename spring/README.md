## запуск приложения

### через терминал (Linux/Mac):
```bash
cd /Users/admin/Documents/GitHub/ticket-system-desktop/spring
mkdir -p bin lib
javac -d bin $(find src/main/java -name "*.java")
java -cp "bin:lib/*" com.ticketvoyage.Main
```

### через IDEA на Windows:
1. Открой проект в IntelliJ IDEA
2. Настрой JDK: File → Project Structure → Project SDK → выбери JDK
3. Добавь библиотеки: File → Project Structure → Modules → Dependencies → `+` → JARs or directories → выбери папку `lib/`
4. Правой кнопкой на `Main.java` → Run 'Main.main()'

### через скрипт run.sh:
```bash
./run.sh
```

## запуск тестов

### через скрипт:
```bash
./run_tests.sh
```

### через IDEA на Windows:
1. Настрой classpath как описано выше для приложения
2. Правой кнопкой на любом тестовом файле → Run
3. Или используй TestRunner.java для запуска всех тестов сразу

Подробная инструкция по тестам в `src/test/java/com/ticketvoyage/README.md`

## тестовые аккаунты:
- автоматический вход: auto_user / auto123 (ADMIN)
- временные пользователи: user / user123
