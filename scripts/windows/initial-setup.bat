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
cd fineract-cn-lang
git remote add upstream https://github.com/apache/fineract-cn-lang.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize async
git clone https://github.com/%githubAccount%/fineract-cn-async.git
cd fineract-cn-async
git remote add upstream https://github.com/apache/fineract-cn-async.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize cassandra
git clone https://github.com/%githubAccount%/fineract-cn-cassandra.git
cd fineract-cn-cassandra
git remote add upstream https://github.com/apache/fineract-cn-cassandra.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize postgresql
git clone https://github.com/%githubAccount%/fineract-cn-postgresql.git
cd fineract-cn-postgresql
git remote add upstream https://github.com/apache/fineract-cn-postgresql.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize data-jpa
git clone https://github.com/%githubAccount%/fineract-cn-data-jpa.git
cd fineract-cn-data-jpa
git remote add upstream https://github.com/apache/fineract-cn-data-jpa.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize command
git clone https://github.com/%githubAccount%/fineract-cn-command.git
cd fineract-cn-command
git remote add upstream https://github.com/apache/fineract-cn-command.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize api
git clone https://github.com/%githubAccount%/fineract-cn-api.git
cd fineract-cn-api
git remote add upstream https://github.com/apache/fineract-cn-api.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize test
git clone https://github.com/%githubAccount%/fineract-cn-test.git
cd fineract-cn-test
git remote add upstream https://github.com/apache/fineract-cn-test.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize postgresql
git clone https://github.com/%githubAccount%/fineract-cn-postgresql.git
cd fineract-cn-postgresql
git remote add upstream https://github.com/apache/fineract-cn-postgresql.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM return to start folder
cd ..

REM create tools folder
mkdir tools
cd tools

REM initialize crypto
git clone https://github.com/%githubAccount%/fineract-cn-crypto.git
cd fineract-cn-crypto
git remote add upstream https://github.com/apache/fineract-cn-crypto.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM return to start folder
cd ..

REM initialize anubis
git clone https://github.com/%githubAccount%/fineract-cn-anubis.git
cd fineract-cn-anubis
git remote add upstream https://github.com/apache/fineract-cn-anubis.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize permitted-feign-client
git clone https://github.com/%githubAccount%/fineract-cn-permitted-feign-client.git
cd fineract-cn-permitted-feign-client
git remote add upstream https://github.com/apache/fineract-cn-permitted-feign-client.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize provisioner
git clone https://github.com/%githubAccount%/fineract-cn-provisioner.git
cd fineract-cn-provisioner
git remote add upstream https://github.com/apache/fineract-cn-provisioner.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize identity
git clone https://github.com/%githubAccount%/fineract-cn-identity.git
cd fineract-cn-identity
git remote add upstream https://github.com/apache/fineract-cn-identity.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize rhythm
git clone https://github.com/%githubAccount%/fineract-cn-rhythm.git
cd fineract-cn-rhythm
git remote add upstream https://github.com/apache/fineract-cn-rhythm.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize template
git clone https://github.com/%githubAccount%/fineract-cn-template.git
cd fineract-cn-template
git remote add upstream https://github.com/apache/fineract-cn-template.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize office
git clone https://github.com/%githubAccount%/fineract-cn-office.git
cd fineract-cn-office
git remote add upstream https://github.com/apache/fineract-cn-office.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize customer
git clone https://github.com/%githubAccount%/fineract-cn-customer.git
cd fineract-cn-customer
git remote add upstream https://github.com/apache/fineract-cn-customer.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize group
git clone https://github.com/%githubAccount%/fineract-cn-group.git
cd fineract-cn-group
git remote add upstream https://github.com/apache/fineract-cn-group.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize accounting
git clone https://github.com/%githubAccount%/fineract-cn-accounting.git
cd fineract-cn-accounting
git remote add upstream https://github.com/apache/fineract-cn-accounting.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize portfolio
git clone https://github.com/%githubAccount%/fineract-cn-portfolio.git
cd fineract-cn-portfolio
git remote add upstream https://github.com/apache/fineract-cn-portfolio.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize deposit-account-management
git clone https://github.com/%githubAccount%/fineract-cn-deposit-account-management.git
cd fineract-cn-deposit-account-management
git remote add upstream https://github.com/apache/fineract-cn-deposit-account-management.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize cheques
git clone https://github.com/%githubAccount%/fineract-cn-cheques.git
cd fineract-cn-cheques
git remote add upstream https://github.com/apache/fineract-cn-cheques.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize teller
git clone https://github.com/%githubAccount%/fineract-cn-teller.git
cd fineract-cn-teller
git remote add upstream https://github.com/apache/fineract-cn-teller.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize reporting
git clone https://github.com/%githubAccount%/fineract-cn-reporting.git
cd fineract-cn-reporting
git remote add upstream https://github.com/apache/fineract-cn-reporting.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize payroll
git clone https://github.com/%githubAccount%/fineract-cn-payroll.git
cd fineract-cn-payroll
git remote add upstream https://github.com/apache/fineract-cn-payroll.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize notifications
git clone https://github.com/%githubAccount%/fineract-cn-notifications.git
cd notifications
git remote add upstream https://github.com/ebenezergraham/fineract-cn-notifications.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

mkdir integration-tests
cd integration-tests

REM initialize service-starter
git clone https://github.com/%githubAccount%/fineract-cn-service-starter.git
cd fineract-cn-service-starter
git remote add upstream https://github.com/apache/fineract-cn-service-starter.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize default-setup
git clone https://github.com/%githubAccount%/fineract-cn-default-setup.git
cd fineract-cn-default-setup
git remote add upstream https://github.com/apache/fineract-cn-default-setup.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

REM initialize demo-server
git clone https://github.com/%githubAccount%/fineract-cn-demo-server.git
cd fineract-cn-demo-server
git remote add upstream https://github.com/apache/fineract-cn-demo-server.git
git checkout develop
CALL gradlew publishToMavenLocal
TIMEOUT /T 5
cd ..

cd ..

REM initialize Web App
git clone https://github.com/%githubAccount%/fineract-cn-fims-web-app.git
cd fineract-cn-fims-web-app
git remote add upstream https://github.com/apache/fineract-cn-fims-web-app.git
git checkout develop
CALL npm i
TIMEOUT /T 5
cd ..

