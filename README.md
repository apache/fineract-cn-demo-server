# Apache Fineract CN Demo Server [![Build Status](https://api.travis-ci.com/apache/fineract-cn-demo-server.svg?branch=develop)](https://travis-ci.com/apache/fineract-cn-demo-server)
Simple environment used for demo purposes

## Preconditions
All Apache Fineract CN projects must be published to your local Maven repository

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

##### demoserver.mode (lite/basic/full) - defaults to 'full'
* lite mode restricts the working set of micro-services to Provisioner, Identity, Rhythm, Organization and Customer
* basic mode restricts the working set of micro-services to Provisioner, Identity, Rhythm, Organization, Customer, Accounting and Deposit
* full mode runs all micro-services (consumes a lot of resources)

##### demoserver.anyPort (true/false) - default to 'false'
By default micro services are started on fixed ports (range 2020...2033+).
This way fims-web-app knows where to find the apps.
Setting this flag true picks any free port for each micro service.

##### custom.cassandra.contactPoints
Custom cassandra contact points (multiple values allowed separated by comma e.g. 127.0.0.1:9042,127.0.0.2:9042)

##### cassandra.cluster.user
cassandra user to use

##### cassandra.cluster.pwd
cassandra password to use

##### custom.postgresql.host
postgresql host to use

##### custom.postgresql.user
postgresql user to use

##### custom.postgresql.password
postgresql password to use


