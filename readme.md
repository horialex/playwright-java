# Playwright with JAVA




### How to run tests from cmd

```bash
mvn clean test -Dtest=SimpleTest -DBROWSER=chromium -DHEADLESS=false -Dthreads=2 -DDEVICE=mobile
```

### How to run tests from cmd with debug enabled

```bash
$env:PWDEBUG="1"; $env:PLAYWRIGHT_JAVA_SRC="src\test\java"; mvn clean test -Dtest=SimpleTest -DBROWSER=chromium -DHEADLESS=false -Dthreads=2 -DDEVICE=mobile
```

### How to open traces from cmd:

```bash
 mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace target\traces\trace_SimpleTest_simpleTest1_20250731_110243_490.zip"
```


### Open Allure results

```bash
allure serve target/allure-results
```

