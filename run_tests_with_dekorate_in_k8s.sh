#!/usr/bin/env bash
CONTAINER_REGISTRY=${1:-localhost:5000}
K8S_NAMESPACE=${2:-k8s}

source scripts/waitFor.sh

kubectl config set-context --current --namespace=$K8S_NAMESPACE

# deploy database
kubectl create -f .openshiftio/database.yaml
if [[ $(waitFor "my-database" "app") -eq 1 ]] ; then
  echo "Database failed to deploy. Aborting"
  exit 1
fi

# deploy application and run tests
./mvnw -s .github/mvn-settings.xml clean verify -Pkubernetes,kubernetes-it -Ddekorate.docker.registry=$CONTAINER_REGISTRY -Dkubernetes.namespace=$K8S_NAMESPACE -Ddekorate.push=true

