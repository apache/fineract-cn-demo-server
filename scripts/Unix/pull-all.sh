#!/bin/bash
githubAccount=$1

get_modules() {
  for module in "$@"; do
    (
    cd "$module"
    git checkout develop
    git pull upstream develop
    chmod +x gradlew
    ./gradlew publishToMavenLocal
    git push origin develop
    )
  done
}

(
cd core
get_modules lang async cassandra mariadb data-jpa 'command' api 'test'
)

(
cd tools
get_modules crypto
)

get_modules anubis permitted-feign-client provisioner identity rhythm template \
  office customer group accounting portfolio deposit-account-management teller \
  reporting payroll

(
cd integration-tests
get_modules service-starter default-setup demo-server \
  test-provisioner-identity-organization test-accounting-portfolio
)

# REM pull Web App
(
cd fims-web-app
git checkout develop
git checkout -- src/main.ts
git checkout -- src/favicon.png
git pull upstream develop
sudo rm -rf node_modules
npm i
git push origin develop
)
