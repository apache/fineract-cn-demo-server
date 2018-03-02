# Apache Fineract CN Demo Server
Simple environment used for demo purposes

## Preconditions
All Apache Fineract CN projects must be published to your local Maven repository

## Run
### IDE
Just start the class DemoServer

### Pre-quisites
1. Install Docker v1.12+ and docker-compose v1.10 as per documentation in docker.com

### Executable JAR file
1. Open a terminal window
2. Change directory to your project location
3. Run `gradlew publishToMavenLocal`
4. Run `./start-dependencies.sh` to start Eureka and ActiveMQ
5. Change directory to build/libs
6. Run `java -jar demo-server-0.1.0-BUILD-SNAPSHOT.jar`

Note:
a) To view Eureka console, navigate to http://localhost:8761
b) To view ActiveMQ admin console queues page, navigate to http://localhost:8161/admin/queues.jsp and use admin/admin as user id and password
c) To shutdown Eureka and ActiveMQ, run `./stop-dependencies.sh`
d) To launch built in Eureka and ActiveMQ, Run `java -jar -Dstart.infrastructure=true demo-server-0.1.0-BUILD-SNAPSHOT.jar`

#### Supported Environment Variables

Sample usage: `java -jar -Ddemoserver.persistent=true demo-server-0.1.0-BUILD-SNAPSHOT.jar`

##### demoserver.persistent (true/false)
Run in persistent mode and to NOT use embedded datastores

##### demoserver.provision (true/false)
Run the provision steps against the services to bootstrap tenants

##### custom.cassandra.contactPoints
Custom cassandra contact points (multiple values allowed separated by comma e.g. 127.0.0.1:9042,127.0.0.2:9042)

##### cassandra.cluster.user
cassandra user to use

##### cassandra.cluster.pwd
cassandra password to use

##### custom.mariadb.host
mariadb host to use

##### custom.mariadb.user
mariadb user to use

##### custom.mariadb.password
mariadb password to use
