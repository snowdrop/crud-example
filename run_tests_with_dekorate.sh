#!/usr/bin/env bash

source scripts/waitFor.sh

# deploy database
oc create -f .openshiftio/database.yaml
if [[ $(waitFor "my-database" "app") -eq 1 ]] ; then
  echo "Database failed to deploy. Aborting"
  exit 1
fi

SB_VERSION_SWITCH=""

while getopts v: option
do
    case "${option}"
        in
        v)SB_VERSION_SWITCH="-Dspring-boot.version=${OPTARG}";;
    esac
done

echo "SB_VERSION_SWITCH: ${SB_VERSION_SWITCH}"

# Run OpenShift Tests
eval "./mvnw -s .github/mvn-settings.xml clean verify -Popenshift,openshift-it ${SB_VERSION_SWITCH}"
