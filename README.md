# CRUD Spring Boot Example

https://appdev.openshift.io/docs/spring-boot-runtime.html#mission-crud-spring-boot

## Deploying application on OpenShift using Dekorate:

```
mvn clean verify -Popenshift -Ddekorate.push=true
```

## Running Tests on OpenShift using Dekorate:

```
./run_tests_with_dekorate.sh
```

## Running Tests on OpenShift using S2i from Source:

```
./run_tests_with_s2i.sh
```
