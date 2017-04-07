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

To get started with this booster you'll need the following prerequisites:

Name | Description | Version
--- | --- | ---
[java][1] | Java JDK | 8
[maven][2] | Apache Maven | 3.3.9
[oc][3] | OpenShift Client Tools | v3.3.x
[git][4] | Git version management | 2.x

[1]: http://www.oracle.com/technetwork/java/javase/downloads/
[2]: https://maven.apache.org/download.cgi?Preferred=ftp://mirror.reverse.net/pub/apache/
[3]: https://docs.openshift.com/enterprise/3.2/cli_reference/get_started_cli.html
[4]: https://git-scm.com/book/en/v2/Getting-Started-Installing-Git

# Run Locally

1. Run the following command to start the maven goal of Spring Boot:

    ```
    mvn clean spring-boot:run
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
   oc new-app \
    -p POSTGRESQL_USER=luke \
    -p POSTGRESQL_PASSWORD=secret \
    -p POSTGRESQL_DATABASE=my_data \
    -p DATABASE_SERVICE_NAME=my-database \
    --name=my-database \
    --template=postgresql-ephemeral 
  ```

  This will create a database pod with the name `my-database`

## Working with a service that exposes the data using a REST interface

4. Use the Fabric8 Maven Plugin to launch the S2I process on the OpenShift Online machine & start the pod.

    ```
    mvn clean fabric8:deploy -Popenshift
    ```

5. Get the route url.

    ```
    oc get route/spring-boot-crud
    ```
    
    NAME | HOST/PORT | PATH | SERVICES | PORT | TERMINATION
    ---- | --------- | ---- | -------- | ---- | -----------
    spring-boot-crud | <HOST_PORT_ADDRESS> | | spring-boot-crud | 8080 | 
    

6. Access the web interface to modify the data in the postgres database.
    ```
    http://<HOST_PORT_ADDRESS>/
    ```