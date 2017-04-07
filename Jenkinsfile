node("maven") {
  checkout scm
  stage("Test") {
    sh "mvn test"
  }
  stage("Deploy database") {
    sh "if ! oc get service my-database | grep my-database; then oc new-app -p POSTGRESQL_USER=luke -p POSTGRESQL_PASSWORD=secret -p POSTGRESQL_DATABASE=my_data -p DATABASE_SERVICE_NAME=my-database --name=my-database --template=postgresql-ephemeral; fi"
  }
  stage("Deploy") {
    sh "mvn fabric8:deploy -Popenshift -DskipTests"
  }
}
