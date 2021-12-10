#!/usr/bin/env bash

# deploy database
oc create -f .openshiftio/database.yaml
sleep 30 # needed in order to bypass the 'Pending' state
timeout 300s bash -c 'while [[ $(oc get pod -o json | jq  ".items[] | select(.metadata.name | contains(\"my-database\"))  | .status  " | jq -rs "sort_by(.startTme) | last | .phase") != "Running" ]]; do sleep 20; done; echo ""'
oc logs $(oc get pod -o json | jq  ".items[] | select(.metadata.name | contains(\"my-database\"))  | .metadata  " | jq -rs "sort_by(.startTme) | last | .name")

# Run OpenShift Tests
./mvnw -s .github/mvn-settings.xml clean verify -Popenshift,openshift-it
