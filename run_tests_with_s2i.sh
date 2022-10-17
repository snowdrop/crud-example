#!/usr/bin/env bash
SOURCE_REPOSITORY_URL=${1:-https://github.com/snowdrop/crud-example}
SOURCE_REPOSITORY_REF=${2:-sb-2.7.x}

source scripts/waitFor.sh

# deploy database
oc create -f .openshiftio/database.yaml
if [[ $(waitFor "my-database" "app") -eq 1 ]] ; then
  echo "Database failed to deploy. Aborting"
  exit 1
fi

# deploy application
oc create -f .openshiftio/application.yaml
oc new-app --template=crud -p SOURCE_REPOSITORY_URL=$SOURCE_REPOSITORY_URL -p SOURCE_REPOSITORY_REF=$SOURCE_REPOSITORY_REF
if [[ $(waitFor "crud" "app") -eq 1 ]] ; then
  echo "Application failed to deploy. Aborting"
  exit 1
fi

# Run Tests
./mvnw -s .github/mvn-settings.xml clean verify -Popenshift,openshift-it -Dunmanaged-test=true
