REM
REM Licensed to the Apache Software Foundation (ASF) under one
REM or more contributor license agreements.  See the NOTICE file
REM distributed with this work for additional information
REM regarding copyright ownership.  The ASF licenses this file
REM to you under the Apache License, Version 2.0 (the
REM "License"); you may not use this file except in compliance
REM with the License.  You may obtain a copy of the License at
REM
REM   http://www.apache.org/licenses/LICENSE-2.0
REM
REM Unless required by applicable law or agreed to in writing,
REM software distributed under the License is distributed on an
REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
REM KIND, either express or implied.  See the License for the
REM specific language governing permissions and limitations
REM under the License.
REM

SET githubAccount=%1

REM create core folder
mkdir core
cd core

REM initialize lang
git clone https://github.com/%githubAccount%/fineract-cn-lang.git
cd lang
git remote add upstream https://github.com/apache/fineract-cn-lang.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze async
git clone https://github.com/%githubAccount%/fineract-cn-async.git
cd async
git remote add upstream https://github.com/apache/fineract-cn-async.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze cassandra
git clone https://github.com/%githubAccount%/fineract-cn-cassandra.git
cd cassandra
git remote add upstream https://github.com/apache/fineract-cn-cassandra.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze mariadb
git clone https://github.com/%githubAccount%/fineract-cn-mariadb.git
cd mariadb
git remote add upstream https://github.com/apache/fineract-cn-mariadb.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze data-jpa
git clone https://github.com/%githubAccount%/fineract-cn-data-jpa.git
cd data-jpa
git remote add upstream https://github.com/apache/fineract-cn-data-jpa.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze command
git clone https://github.com/%githubAccount%/fineract-cn-command.git
cd command
git remote add upstream https://github.com/apache/fineract-cn-command.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze api
git clone https://github.com/%githubAccount%/fineract-cn-api.git
cd api
git remote add upstream https://github.com/apache/fineract-cn-api.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze test
git clone https://github.com/%githubAccount%/fineract-cn-test.git
cd test
git remote add upstream https://github.com/apache/fineract-cn-test.git
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
git clone https://github.com/%githubAccount%/fineract-cn-crypto.git
cd crypto
git remote add upstream https://github.com/apache/fineract-cn-crypto.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM return to start folder
cd ..

REM initialze anubis
git clone https://github.com/%githubAccount%/fineract-cn-anubis.git
cd anubis
git remote add upstream https://github.com/apache/fineract-cn-anubis.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize permitted-feign-client
git clone https://github.com/%githubAccount%/fineract-cn-permitted-feign-client.git
cd permitted-feign-client
git remote add upstream https://github.com/apache/fineract-cn-permitted-feign-client.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze provisioner
git clone https://github.com/%githubAccount%/fineract-cn-provisioner.git
cd provisioner
git remote add upstream https://github.com/apache/fineract-cn-provisioner.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze identity
git clone https://github.com/%githubAccount%/fineract-cn-identity.git
cd identity
git remote add upstream https://github.com/apache/fineract-cn-identity.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze rhythm
git clone https://github.com/%githubAccount%/fineract-cn-rhythm.git
cd rhythm
git remote add upstream https://github.com/apache/fineract-cn-rhythm.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze template
git clone https://github.com/%githubAccount%/fineract-cn-template.git
cd template
git remote add upstream https://github.com/apache/fineract-cn-template.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze office
git clone https://github.com/%githubAccount%/fineract-cn-office.git
cd office
git remote add upstream https://github.com/apache/fineract-cn-office.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze customer
git clone https://github.com/%githubAccount%/fineract-cn-customer.git
cd customer
git remote add upstream https://github.com/apache/fineract-cn-customer.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze group
git clone https://github.com/%githubAccount%/fineract-cn-group.git
cd group
git remote add upstream https://github.com/apache/fineract-cn-group.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze accounting
git clone https://github.com/%githubAccount%/fineract-cn-accounting.git
cd accounting
git remote add upstream https://github.com/apache/fineract-cn-accounting.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze portfolio
git clone https://github.com/%githubAccount%/fineract-cn-portfolio.git
cd portfolio
git remote add upstream https://github.com/apache/fineract-cn-portfolio.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze deposit-account-management
git clone https://github.com/%githubAccount%/fineract-cn-deposit-account-management.git
cd deposit-account-management
git remote add upstream https://github.com/apache/fineract-cn-deposit-account-management.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze cheques
git clone https://github.com/%githubAccount%/fineract-cn-cheques.git
cd cheques
git remote add upstream https://github.com/apache/fineract-cn-cheques.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze teller
git clone https://github.com/%githubAccount%/fineract-cn-teller.git
cd teller
git remote add upstream https://github.com/apache/fineract-cn-teller.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze reporting
git clone https://github.com/%githubAccount%/fineract-cn-reporting.git
cd reporting
git remote add upstream https://github.com/apache/fineract-cn-reporting.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze payroll
git clone https://github.com/%githubAccount%/fineract-cn-payroll.git
cd payroll
git remote add upstream https://github.com/apache/fineract-cn-payroll.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

mkdir integration-tests
cd integration-tests

REM initialze service-starter
git clone https://github.com/%githubAccount%/fineract-cn-service-starter.git
cd service-starter
git remote add upstream https://github.com/apache/fineract-cn-service-starter.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze default-setup
git clone https://github.com/%githubAccount%/fineract-cn-default-setup.git
cd default-setup
git remote add upstream https://github.com/apache/fineract-cn-default-setup.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialze demo-server
git clone https://github.com/%githubAccount%/fineract-cn-demo-server.git
cd demo-server
git remote add upstream https://github.com/apache/fineract-cn-demo-server.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

cd ..

REM initialze Web App
git clone https://github.com/%githubAccount%/fineract-cn-fims-web-app.git
cd fims-web-app
git remote add upstream https://github.com/apache/fineract-cn-fims-web-app.git
git checkout develop
CALL npm i
TIMEOUT /T 5
cd ..

