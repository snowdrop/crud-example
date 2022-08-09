FROM openjdk:11
COPY target/*.jar crud.jar
CMD java ${JAVA_OPTS} -jar crud.jar
