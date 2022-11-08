#!/usr/bin/env bash

# deploy database
oc create -f .openshiftio/database.yaml
if [[ $(waitFor "my-database" "app") -eq 1 ]] ; then
  echo "Database failed to deploy. Aborting"
  exit 1
fi

# Run Tests
eval "./mvnw clean verify -Popenshift,openshift-it $@"
