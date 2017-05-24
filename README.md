# Mifos I/O Demo Server
Simple environment used for demo purposes

## Preconditions
All Mifos I/O projects must be published to your local Maven repository

## Run
### IDE
Just start the class DemoServer

### Executable JAR file
1. Open a terminal window
2. Change directory to your project location
3. Run `gradlew publishToMavenLocal`
4. Change directory to build/libs
5. Run `java -jar demo-server-0.1.0-BUILD-SNAPSHOT.jar`

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
