# CRUD Spring Boot Example

https://appdev.openshift.io/docs/spring-boot-runtime.html#mission-crud-spring-boot

## Table of Contents

* [CRUD Spring Boot Example](#crud-spring-boot-example)
    * [Prerequisites](#prerequisites)
    * [Deploying application on OpenShift using Dekorate](#deploying-application-on-openshift-using-dekorate)
    * [Deploying application on OpenShift using Helm](#deploying-application-on-openshift-using-helm)
    * [Deploying application on Kubernetes using Helm](#deploying-application-on-kubernetes-using-helm)
    * [Running Tests on OpenShift using Dekorate](#running-tests-on-openshift-using-dekorate)
    * [Running Tests on OpenShift using S2i from Source](#running-tests-on-openshift-using-s2i-from-source)
    * [Running Tests on OpenShift using Helm](#running-tests-on-openshift-using-helm)
    * [Running Tests on Kubernetes with Helm](#running-tests-on-kubernetes-using-helm)

## Prerequisites

- JDK 11+ installed with JAVA_HOME configured appropriately

## Deploying application on OpenShift using Dekorate

Before deploying the application using Dekorate, make sure you have deployed the database first:

```
oc create -f .openshiftio/database.yaml
```

Once the database is up and running, we can deploy the application using Dekorate:

```bash
mvn clean verify -Popenshift -Ddekorate.deploy=true
```

To deploy the application using a specific Spring Boot version use the `-Dspring-boot.version` switch.

```bash
mvn clean verify -Popenshift -Ddekorate.deploy=true -Dspring-boot.version=2.6.7
```

## Deploying application on OpenShift using Helm

First, make sure you have installed [the Helm command line](https://helm.sh/docs/intro/install/) and connected/logged to a kubernetes cluster.

Then, you need to install the example by doing:

```
helm install crud ./helm --set app.route.expose=true --set app.s2i.source.repo=https://github.com/snowdrop/crud-example --set app.s2i.source.ref=<branch-to-use>
```

**note**: Replace `<branch-to-use>` with one branch from `https://github.com/snowdrop/crud-example/branches/all`.

And to uninstall the chart, execute:

```
helm uninstall crud
```

## Deploying application on Kubernetes using Helm

Requirements:
- Have installed [the Helm command line](https://helm.sh/docs/intro/install/)
- Have connected/logged to a kubernetes cluster

You need to install the example by doing:

```
helm install crud ./helm --set app.ingress.host=<your k8s domain>
```

And to uninstall the chart, execute:

```
helm uninstall crud
```


## Running Tests on OpenShift using Dekorate

```bash
./run_tests_with_dekorate_in_ocp.sh
```

Alternatively, tests can be executed against a specific Spring Boot or Dekorate version by passing the
version as a `-D<variable property name>=value` parameter. For instance overriding both the Spring Boot and the Dekorate versions using their corresponding version properties is done the following way:

```bash
./run_tests_with_dekorate_in_ocp.sh -Dspring-boot.version=2.7.3 -Ddekorate.version=2.11.1
```

## Running Tests on OpenShift using S2i from Source

```
./run_tests_with_s2i.sh
```

This script can take 2 parameters referring to the repository and the branch to use to source the images from.

```bash
./run_tests_with_s2i.sh "https://github.com/snowdrop/crud-example" branch-to-test
```

## Running Tests on OpenShift using Helm

```
./run_tests_with_helm_in_ocp.sh
```

This script can take 2 parameters referring to the repository and the branch to use to source the images from.

```bash
./run_tests_with_helm_in_ocp.sh "https://github.com/snowdrop/crud-example" branch-to-test
```

## Running Tests on Kubernetes using Helm

First, you need to create the k8s namespace:

```
kubectl create namespace <the k8s namespace>
```

Then, run the tests by specifying the container registry and the kubernetes namespace:
```
./run_tests_with_helm_in_k8s.sh <your container registry: for example "quay.io/user"> <the k8s namespace>
```

For example:

```
./run_tests_with_helm_in_k8s.sh "quay.io/user" "myNamespace"
```
