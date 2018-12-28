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

cd core

REM pull lang
cd fineract-cn-lang
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull async
cd fineract-cn-async
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull cassandra
cd fineract-cn-cassandra
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull mariadb
cd fineract-cn-mariadb
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull data-jpa
cd fineract-cn-data-jpa
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull command
cd fineract-cn-command
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull api
cd fineract-cn-api
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull test
cd fineract-cn-test
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull postgresql
cd fineract-cn-postgresql
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

cd ..

cd tools

REM pull crypto
cd fineract-cn-crypto
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

cd ..

REM pull anubis
cd fineract-cn-anubis
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull permitted-feign-client
cd fineract-cn-permitted-feign-client
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull provisioner
cd fineract-cn-provisioner
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull identity
cd fineract-cn-identity
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull rhythm
cd fineract-cn-rhythm
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull template
cd fineract-cn-template
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull office
cd fineract-cn-office
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull customer
cd fineract-cn-customer
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull group
cd fineract-cn-group
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull accounting
cd fineract-cn-accounting
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull portfolio
cd fineract-cn-portfolio
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull deposit-account-management
cd fineract-cn-deposit-account-management
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull teller
cd fineract-cn-teller
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull reporting
cd fineract-cn-reporting
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull payroll
cd fineract-cn-payroll
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull notifications
cd fineract-cn-notifications
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

cd integration-tests

REM pull service-starter
cd fineract-cn-service-starter
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull default-setup
cd fineract-cn-default-setup
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull demo-server
cd fineract-cn-demo-server
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull test-provisioner-identity-organization
cd fineract-cn-test-provisioner-identity-organization
git checkout develop
git pull upstream develop
CALL gradlew build
git push origin develop
TIMEOUT /T 5
cd ..

REM pull test-accounting-portfolio
cd fineract-cn-test-accounting-portfolio
git checkout develop
git pull upstream develop
CALL gradlew build
git push origin develop
TIMEOUT /T 5
cd ..

cd ..

REM pull Web App
cd fineract-cn-fims-web-app
git checkout develop
git checkout -- src/main.ts
git checkout -- src/favicon.png
git pull upstream develop
rmdir node_modules /S /Q
CALL npm i
git push origin develop
TIMEOUT /T 5
cd ..
