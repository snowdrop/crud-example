#!/usr/bin/env bash
# Parameters allowed:
#   . --repository-url
#   . --branch-to-test
#   . --maven-settings
#   . --maven-mirror-url
#   . --maven-args-append
#   . --ocp-database-file
UNMANAGED_PARAMS=""
SOURCE_REPOSITORY_URL="https://github.com/snowdrop/crud-example"
SOURCE_REPOSITORY_REF="sb-2.7.x"
MAVEN_SETTINGS_REF=""
MAVEN_MIRROR_URL=""
OCP_DATABASE_FILE=".openshiftio/database.yaml"

while [ $# -gt 0 ]; do
  if [[ $1 == *"--"* ]]; then
    param="${1/--/}"
    case $1 in
      --repository-url) SOURCE_REPOSITORY_URL="$2";;
      --branch-to-test) SOURCE_REPOSITORY_REF="$2";;
      --maven-settings) MAVEN_SETTINGS_REF="-s $2";;
      --maven-mirror-url) MAVEN_MIRROR_URL="$2";;
      --ocp-database-file) OCP_DATABASE_FILE="$2";;
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

# deploy application
oc create -f .openshiftio/application.yaml
oc new-app --template=crud -p SOURCE_REPOSITORY_URL=$SOURCE_REPOSITORY_URL -p SOURCE_REPOSITORY_REF=$SOURCE_REPOSITORY_REF \
  -p MAVEN_MIRROR_URL=$MAVEN_MIRROR_URL -p MAVEN_ARGS_APPEND="$UNMANAGED_PARAMS"
if [[ $(waitFor "crud" "app") -eq 1 ]] ; then
  echo "Application failed to deploy. Aborting"
  exit 1
fi

# Run Tests
eval "./mvnw ${MAVEN_SETTINGS_REF} clean verify -Popenshift,openshift-it -Dunmanaged-test=true ${UNMANAGED_PARAMS}"
