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
#Ensure that you have forked Fineract CN repositories from your githubAccount
githubAccount=$1

# REM create core folder
mkdir -p core
cd core

get_modules() {
  for module in $@
  do
    git clone https://github.com/$githubAccount/$module.git
    cd $module
    git remote add upstream https://github.com/apache/$module.git
    # For some reason permission gets denied
    chmod +x gradlew
    ./gradlew publishToMavenLocal
    cd ..
  done
}

get_modules fineract-cn-lang fineract-cn-api fineract-cn-async fineract-cn-cassandra fineract-cn-mariadb fineract-cn-data-jpa fineract-cn-command fineract-cn-test

# Return to start folder
cd ..

# REM create tools folder
mkdir tools
cd tools

# REM initialize javamoney
git clone https://github.com/JavaMoney/javamoney-lib.git
cd javamoney-lib
mvn install -Dmaven.test.skip=true

cd ..

# REM initialize fineract-cn-crypto
git clone https://github.com/$githubAccount/fineract-cn-crypto.git
cd fineract-cn-crypto
git remote add upstream https://github.com/apache/fineract-cn-crypto.git
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# exit tools directory
cd ..

get_modules fineract-cn-anubis fineract-cn-identity fineract-cn-permitted-feign-client fineract-cn-provisioner fineract-cn-rhythm fineract-cn-template fineract-cn-office fineract-cn-customer fineract-cn-group fineract-cn-accounting fineract-cn-portfolio fineract-cn-deposit-account-management fineract-cn-cheques fineract-cn-payroll fineract-cn-teller fineract-cn-reporting

mkdir integration-tests
cd integration-tests

get_modules fineract-cn-service-starter fineract-cn-default-setup fineract-cn-demo-server

# REM initialize Web App
git clone https://github.com/$githubAccount/fineract-cn-fims-web-app.git
cd fineract-cn-fims-web-app
git remote add upstream https://github.com/apache/fineract-cn-fims-web-app.git
npm i

cd ..
