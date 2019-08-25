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
githubAccount=ebenezergraham

# REM create core folder
mkdir -p core
cd core

get_modules() {
  for module in $@
  do
    git clone https://github.com/apache/$module.git
    cd $module
    ./gradlew publishToMavenLocal
    cd ..
  done
}

get_modules_with_fincn_2() {
  for module in $@
  do
    git clone -b FINCN-2 https://github.com/$githubAccount/$module.git
    cd $module
    ./gradlew publishToMavenLocal
    cd ..
  done
}

get_modules fineract-cn-lang fineract-cn-api fineract-cn-async fineract-cn-cassandra fineract-cn-data-jpa fineract-cn-command

get_modules_with_fincn_2 fineract-cn-postgresql fineract-cn-test
# Return to start folder
cd ..

# REM create tools folder
mkdir tools
cd tools

# REM initialize fineract-cn-crypto
git clone https://github.com/$githubAccount/fineract-cn-crypto.git
cd fineract-cn-crypto
git remote add upstream https://github.com/apache/fineract-cn-crypto.git
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# exit tools directory
cd ..
get_modules fineract-cn-anubis fineract-cn-permitted-feign-client

get_modules_with_fincn_2 fineract-cn-identity fineract-cn-provisioner fineract-cn-office

# REM clone fineract-cn-rhythm FINCN-115
git clone https://github.com/ebenezergraham/fineract-cn-rhythm.git -b FINCN-115
cd fineract-cn-rhythm
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# REM clone fineract-cn-customer FINCN-116
git clone https://github.com/ebenezergraham/fineract-cn-customer.git -b FINCN-116
cd fineract-cn-customer
git remote add upstream https://github.com/apache/fineract-cn-customer.git
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# REM clone fineract-cn-group FINCN-118
git clone https://github.com/izakey/fineract-cn-group.git -b FINCN-118
cd fineract-cn-group
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

get_modules_with_fincn_2 fineract-cn-accounting fineract-cn-portfolio

# REM clone fineract-cn-deposit-account-management FINCN-122
git clone https://github.com/izakey/fineract-cn-deposit-account-management.git -b FINCN-122
cd fineract-cn-deposit-account-management
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# REM clone fineract-cn-cheques FINCN-125
git clone https://github.com/izakey/fineract-cn-cheques.git -b FINCN-125
cd fineract-cn-cheques
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# REM clone fineract-cn-teller FINCN-126
git clone https://github.com/Izakey/fineract-cn-teller.git -b FINCN-126
cd fineract-cn-teller
git remote add upstream https://github.com/Izakey/fineract-cn-teller.git
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# REM clone fineract-cn-payroll FINCN-123
git clone https://github.com/izakey/fineract-cn-payroll.git -b FINCN-123
cd fineract-cn-payroll
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

get_modules_with_fincn_2 fineract-cn-template


# REM clone fineract-cn-notifications FINCN-127
git clone https://github.com/ebenezergraham/fineract-cn-notifications.git -b FINCN-127
cd fineract-cn-notifications
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# REM clone fineract-cn-reporting FINCN-119
git clone https://github.com/izakey/fineract-cn-reporting.git -b FINCN-119
cd fineract-cn-reporting
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

mkdir integration-tests
cd integration-tests

# REM clone fineract-cn-default-setup FINCN-155
git clone https://github.com/ebenezergraham/fineract-cn-default-setup.git -b FINCN-155
cd fineract-cn-default-setup
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# REM clone fineract-cn-service-starter FINCN-140
git clone https://github.com/izakey/fineract-cn-service-starter.git -b FINCN-140
cd fineract-cn-service-starter
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# REM clone fineract-cn-demo-server FINCN-141
git clone https://github.com/ebenezergraham/fineract-cn-demo-server.git -b FINCN-141
cd fineract-cn-demo-server
chmod +x gradlew
./gradlew publishToMavenLocal
cd ..

# REM initialize Web App
git clone https://github.com/apache/fineract-cn-fims-web-app.git
cd fineract-cn-fims-web-app
git remote add upstream https://github.com/apache/fineract-cn-fims-web-app.git
npm i

cd ..
