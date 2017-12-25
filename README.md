#Docker
1. cd docker
2. docker build -t test-postgresql .
3. docker run --name test-postgres -p 5432:5432 -d test-postgres:latest

log to docker container:
docker exec -it <mycontainer> bash

#mvn
mvn commands:
- mvn clean
- mvn compile
- mvn compile war:war
- mvn compile war:exploded
war can be run with tomcat:
1. stop tomcat
2. delete existing deployment in $tomcat_home/webapps
3. copy war file to tomcat_home_webapps
4. start tomcat

#run jetty server:
mvn jetty:run