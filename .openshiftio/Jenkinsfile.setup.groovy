def setupEnvironmentPre(env) {
      sh "if ! oc get -n ${env} service my-database | grep my-database; then oc new-app -n ${env} -e POSTGRESQL_USER=luke -e POSTGRESQL_PASSWORD=secret -e POSTGRESQL_DATABASE=my_data openshift/postgresql-92-centos7 --name=my-database; fi"
}

def setupEnvironmentPost(env) {
}

return this