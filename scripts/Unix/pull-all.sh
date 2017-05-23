#!/bin/bash
githubAccount=$1

cd core

# REM pull lang
cd lang
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop

cd ..

# REM pull async
cd async
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull cassandra
cd cassandra
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull mariadb
cd mariadb
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull data-jpa
cd data-jpa
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull command
cd command
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull api
cd api
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull test
cd test
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

cd ..

cd tools

# REM pull crypto
cd crypto
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop
cd ..

cd ..

# REM pull anubis
cd anubis
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull permitted-feign-client
cd permitted-feign-client
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop
cd ..

# REM pull provisioner
cd provisioner
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull identity
cd identity
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull rhythm
cd rhythm
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop
cd ..

# REM pull template
cd template
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull office
cd office
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull customer
cd customer
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull group
cd group
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop
cd ..

# REM pull accounting
cd accounting
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull portfolio
cd portfolio
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

cd integration-tests

# REM pull service-starter
cd service-starter
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull demo-server
cd demo-server
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull test-provisioner-identity-organization
cd test-provisioner-identity-organization
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

# REM pull test-accounting-portfolio
cd test-accounting-portfolio
git checkout develop
git pull upstream develop
chmod +x gradlew
./gradlew publishToMavenLocal
git push origin develop 
cd ..

cd ..

# REM pull Web App
cd fims-web-app
git checkout develop
git checkout -- src/main.ts
git checkout -- src/favicon.png
git pull upstream develop
sudo rm -rf node_modules
npm i
git push origin develop
cd ..