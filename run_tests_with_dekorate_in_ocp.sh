#!/usr/bin/env bash
UNMANAGED_PARAMS=""
MAVEN_SETTINGS_REF=""
OCP_DATABASE_FILE=".openshiftio/database.yaml"

while [ $# -gt 0 ]; do
  if [[ $1 == *"--"* ]];
  then
    param="${1/--/}";
    case $1 in
      --ocp-database-file) OCP_DATABASE_FILE="$2";;
      --maven-settings) MAVEN_SETTINGS_REF="-s $2";;
      *) UNMANAGED_PARAMS="${UNMANAGED_PARAMS} $1 $2";;
    esac;
    shift
  elif [[ $1 == "-D"* ]];
  then
    UNMANAGED_PARAMS="${UNMANAGED_PARAMS} $1";
  fi
  shift
done

source scripts/waitFor.sh

# deploy database
oc create -f ${OCP_DATABASE_FILE}
if [[ $(waitFor "my-database" "app") -eq 1 ]] ; then
  echo "Database failed to deploy. Aborting"
  exit 1
fi

# Run Tests
eval "./mvnw clean verify -Popenshift,openshift-it ${UNMANAGED_PARAMS}"
