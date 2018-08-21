#
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

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
get_modules fineract-cn-lang fineract-cn-api fineract-cn-async fineract-cn-cassandra fineract-cn-mariadb fineract-cn-data-jpa \
fineract-cn-command fineract-cn-test
)

(
cd tools
get_modules fineract-cn-crypto
)

get_modules fineract-cn-anubis fineract-cn-identity fineract-cn-permitted-feign-client fineract-cn-provisioner fineract-cn-rhythm \
fineract-cn-template fineract-cn-office fineract-cn-customer fineract-cn-group fineract-cn-accounting \
fineract-cn-portfolio fineract-cn-deposit-account-management fineract-cn-cheques fineract-cn-payroll fineract-cn-teller fineract-cn-reporting fineract-cn-notifications

(
cd integration-tests
get_modules fineract-cn-service-starter fineract-cn-default-setup fineract-cn-demo-server
)

# REM pull Web App
(
cd fineract-cn-fims-web-app
git checkout develop
git checkout -- src/main.ts
git checkout -- src/favicon.png
git pull upstream develop
sudo rm -rf node_modules
npm i
git push origin develop
)
