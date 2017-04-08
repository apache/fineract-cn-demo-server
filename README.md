# Mifos I/O Demo Server
Simple environment used for demo purposes

## Preconditions
All Mifos I/O projects must be published to your local Maven repository

## Run
### IDE
Just start the class DemoServer

### Executable JAR file
1. Open a terminal window
3. Change directory to your project location
3. Run `gradlew publishToMavenLocal`
4. Change directory to build/libs
4. Run `java -jar demo-server-0.1.0-BUILD-SNAPSHOT.jar`

