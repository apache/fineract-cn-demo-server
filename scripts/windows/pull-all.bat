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
cd lang
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull async
cd async
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull cassandra
cd cassandra
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull mariadb
cd mariadb
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull data-jpa
cd data-jpa
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull command
cd command
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull api
cd api
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull test
cd test
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

cd ..

cd tools

REM pull crypto
cd crypto
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

cd ..

REM pull anubis
cd anubis
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull permitted-feign-client
cd permitted-feign-client
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull provisioner
cd provisioner
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull identity
cd identity
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull rhythm
cd rhythm
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull template
cd template
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull office
cd office
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull customer
cd customer
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull group
cd group
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull accounting
cd accounting
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull portfolio
cd portfolio
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull deposit-account-management
cd deposit-account-management
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull teller
cd teller
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull reporting
cd reporting
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull payroll
cd payroll
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull notifications
cd notifications
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

cd integration-tests

REM pull service-starter
cd service-starter
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull default-setup
cd default-setup
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull demo-server
cd demo-server
git checkout develop
git pull upstream develop
CALL gradlew publishToMavenLocal
git push origin develop
TIMEOUT /T 5
cd ..

REM pull test-provisioner-identity-organization
cd test-provisioner-identity-organization
git checkout develop
git pull upstream develop
CALL gradlew build
git push origin develop
TIMEOUT /T 5
cd ..

REM pull test-accounting-portfolio
cd test-accounting-portfolio
git checkout develop
git pull upstream develop
CALL gradlew build
git push origin develop
TIMEOUT /T 5
cd ..

cd ..

REM pull Web App
cd fims-web-app
git checkout develop
git checkout -- src/main.ts
git checkout -- src/favicon.png
git pull upstream develop
rmdir node_modules /S /Q
CALL npm i
git push origin develop
TIMEOUT /T 5
cd ..
