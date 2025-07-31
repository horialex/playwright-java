# Playwright with JAVA


## Requirements

- Java - 21 LTS
- Maven - latest version


### How to install the project

Run from cmd first the following command to install all the maven dependencies
```bash
mvn install
```


### How to run tests from cmd

```bash
mvn clean test -Dtest=SimpleTest -Dthreads=2
```

### How to run tests from cmd with debug enabled

```bash
$env:PWDEBUG="1"; $env:PLAYWRIGHT_JAVA_SRC="src\test\java"; mvn clean test -Dtest=SimpleTest -Dthreads=2
```

### Open Allure results

```bash
allure serve target/allure-results
```

### Screenshots
Screenshtos are under `target/screenshots`