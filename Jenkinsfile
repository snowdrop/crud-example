node("launchpad-maven") {
  checkout scm
  stage("Test") {
    sh "mvn test"
  }
  stage("Deploy database") {
    sh "if ! oc get service my-database | grep my-database; then oc new-app -e POSTGRESQL_USER=luke -ePOSTGRESQL_PASSWORD=secret -ePOSTGRESQL_DATABASE=my_data centos/postgresql-92-centos7 --name=my-database; fi"
  }
  stage("Deploy") {
    sh "mvn clean install -Popenshift -Ddekorate.deploy=true -DskipTests"
  }
}
