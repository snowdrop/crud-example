# Introduction

## User Problem

User would like to implement a CRUD-API exposed over HTTP.

## Description

The purpose of this experience is to demonstrate how a developer can quickly implement a “CRUD” application using the Obsidian platform. The application is using a database to store the managed items. The API has been designed to be simple and intuitive.

The experience is built upon an application to manage fruits. The user experience is delivered using a web page proposing the CRUD operations (create / retrieve / update and delete). The user can also use the service endpoint directly.

While this experience does not showcase a fully-matured RESTful model (level 3), it uses HTTP verbs and status.

## Concepts & Architectural Patterns

HTTP API, CRUD, Database

# Prerequisites

To get started with this quickstart you'll need the following prerequisites:

Name | Description | Version
--- | --- | ---
[java][1] | Java JDK | 8
[maven][2] | Apache Maven | 3.2.x
[oc][3] | OpenShift Client Tools | v3.3.x
[git][4] | Git version management | 2.x

[1]: http://www.oracle.com/technetwork/java/javase/downloads/
[2]: https://maven.apache.org/download.cgi?Preferred=ftp://mirror.reverse.net/pub/apache/
[3]: https://docs.openshift.com/enterprise/3.2/cli_reference/get_started_cli.html
[4]: https://git-scm.com/book/en/v2/Getting-Started-Installing-Git

# Build the Project

The project uses Spring Boot to create and package the service.

Execute the following maven command:

```
mvn clean install
```

# Launch and test

1. Run the following command to start the maven goal of Spring Boot:

    ```
    mvn spring-boot:run
    ```

1. If the application launched without error, use the following command to access the web interface to view and modify the data in the database:

    ```
    http http://localhost:8080/    
    ```

# OpenShift Online

## Login and prepare your openshift account

1. Go to [OpenShift Online](https://console.dev-preview-int.openshift.com/console/command-line) to get the token used by the oc client for authentication and project access.

2. On the oc client, execute the following command to replace MYTOKEN with the one from the Web Console:

    ```
    oc login https://api.dev-preview-int.openshift.com --token=MYTOKEN
    ```

## Deploy a Postgres Database to Openshift

1. Start the PostgreSQL database on Openshift

  ```
   oc new-app -e POSTGRESQL_USER=springboot -e POSTGRESQL_PASSWORD=password -e POSTGRESQL_DATABASE=fruits openshift/postgresql-92-centos7 --name=my-database
  ```

  This will create a database pod with the name `my-database`

## Working with a service that exposes the data using a REST interface

4. Use the Fabric8 Maven Plugin to launch the S2I process on the OpenShift Online machine & start the pod.

    ```
    mvn clean fabric8:deploy -Popenshift  -DskipTests
    ```

5. Get the route url.

    ```
    oc get route/springboot-rest-jdbc
    NAME              HOST/PORT                                          PATH      SERVICE                TERMINATION   LABELS
    springboot-rest-jdbc   <HOST_PORT_ADDRESS>             springboot-rest-jdbc:8080
    ```

6. Access the web interface to modify the data in the postgres database.
    ```
    http://<HOST_PORT_ADDRESS>/
    ```