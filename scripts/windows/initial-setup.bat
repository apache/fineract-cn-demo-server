SET githubAccount=%1

REM create core folder
mkdir core
cd core

REM initialize lang
git clone https://github.com/%githubAccount%/lang.git
cd lang
git remote add upstream https://github.com/mifosio/lang.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze async
git clone https://github.com/%githubAccount%/async.git
cd async
git remote add upstream https://github.com/mifosio/async.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze cassandra
git clone https://github.com/%githubAccount%/cassandra.git
cd cassandra
git remote add upstream https://github.com/mifosio/cassandra.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze mariadb
git clone https://github.com/%githubAccount%/mariadb.git
cd mariadb
git remote add upstream https://github.com/mifosio/mariadb.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze data-jpa
git clone https://github.com/%githubAccount%/data-jpa.git
cd data-jpa
git remote add upstream https://github.com/mifosio/data-jpa.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze command
git clone https://github.com/%githubAccount%/command.git
cd command
git remote add upstream https://github.com/mifosio/command.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze api
git clone https://github.com/%githubAccount%/api.git
cd api
git remote add upstream https://github.com/mifosio/api.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze test
git clone https://github.com/%githubAccount%/test.git
cd test
git remote add upstream https://github.com/mifosio/test.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM return to start folder
cd ..

REM create tools folder
mkdir tools
cd tools

REM initialze javamoney
git clone https://github.com/%githubAccount%/javamoney-lib.git
cd javamoney-lib
git remote add upstream https://github.com/JavaMoney/javamoney-lib.git
git checkout master
CALL mvn install -Dmaven.test.skip=true
TIMEOUT /T 5
cd ..

REM initialze crypto
git clone https://github.com/%githubAccount%/crypto.git
cd crypto
git remote add upstream https://github.com/mifosio/crypto.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM return to start folder
cd ..

REM initialze anubis
git clone https://github.com/%githubAccount%/anubis.git
cd anubis
git remote add upstream https://github.com/mifosio/anubis.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize permitted-feign-client
git clone https://github.com/%githubAccount%/permitted-feign-client.git
cd permitted-feign-client
git remote add upstream https://github.com/mifosio/permitted-feign-client.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze provisioner
git clone https://github.com/%githubAccount%/provisioner.git
cd provisioner
git remote add upstream https://github.com/mifosio/provisioner.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze identity
git clone https://github.com/%githubAccount%/identity.git
cd identity
git remote add upstream https://github.com/mifosio/identity.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze rhythm
git clone https://github.com/%githubAccount%/rhythm.git
cd rhythm
git remote add upstream https://github.com/mifosio/rhythm.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze template
git clone https://github.com/%githubAccount%/template.git
cd template
git remote add upstream https://github.com/mifosio/template.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze office
git clone https://github.com/%githubAccount%/office.git
cd office
git remote add upstream https://github.com/mifosio/office.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze customer
git clone https://github.com/%githubAccount%/customer.git
cd customer
git remote add upstream https://github.com/mifosio/customer.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze group
git clone https://github.com/%githubAccount%/group.git
cd group
git remote add upstream https://github.com/mifosio/group.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze accounting
git clone https://github.com/%githubAccount%/accounting.git
cd accounting
git remote add upstream https://github.com/mifosio/accounting.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze portfolio
git clone https://github.com/%githubAccount%/portfolio.git
cd portfolio
git remote add upstream https://github.com/mifosio/portfolio.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

mkdir integration-tests
cd integration-tests

REM initialze service-starter
git clone https://github.com/%githubAccount%/service-starter.git
cd service-starter
git remote add upstream https://github.com/mifosio/service-starter.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze demo-server
git clone https://github.com/%githubAccount%/demo-server.git
cd demo-server
git remote add upstream https://github.com/mifosio/demo-server.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze test-provisioner-identity-organization
git clone https://github.com/%githubAccount%/test-provisioner-identity-organization.git
cd test-provisioner-identity-organization
git remote add upstream https://github.com/mifosio/test-provisioner-identity-organization.git
git checkout develop
CALL gradlew build
TIMEOUT /T 5
cd ..

REM initialze test-accounting-portfolio
git clone https://github.com/%githubAccount%/test-accounting-portfolio.git
cd test-accounting-portfolio
git remote add upstream https://github.com/mifosio/test-accounting-portfolio.git
git checkout develop
CALL gradlew build
TIMEOUT /T 5
cd ..

cd ..

REM initialze Web App
git clone https://github.com/%githubAccount%/fims-web-app.git
cd fims-web-app
git remote add upstream https://github.com/mifosio/fims-web-app.git
git checkout develop
CALL npm i
TIMEOUT /T 5
cd ..

