#!/usr/bin/env bash

# deploy database: won't be needed in Dekorate 1.7.0. Related to https://github.com/dekorateio/dekorate/pull/798
oc create -f .openshiftio/database.yaml
timeout 300s bash -c 'while [[ $(oc get pod -o json | jq  ".items[] | select(.metadata.name | contains(\"my-database\"))  | .status  " | jq -rs "sort_by(.startTme) | last | .phase") != "Running" ]]; do sleep 20; done; echo ""'

# Run OpenShift Tests
./mvnw -s .github/mvn-settings.xml clean verify -Popenshift,openshift-it
