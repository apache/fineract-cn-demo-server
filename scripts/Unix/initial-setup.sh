#!/bin/bash
githubAccount=$1

exec 5>&1
# REM create core folder
mkdir -p core
cd core
ERRORS=""
FAILEDMODULES=""
get_modules() {
  for module in $@
  do
    git clone https://github.com/$githubAccount/$module.git
    pushd $module
    git remote add upstream https://github.com/mifosio/$module.git
    # For some reason permission is denied
    chmod +x gradlew
    THISBUILD="\nBUILDING $module\n"
    THISBUILD+=$(./gradlew publishToMavenLocal |& tee >(cat - >&5) 
    	if [ ${PIPESTATUS[0]} -ne 0 ]; then exit 1; fi )
    if [ $? -ne 0 ]; then 
        ERRORS+="$(echo -e $THISBUILD | tail)\n"
	FAILEDMODULES+="$module "
    fi
    popd
  done
}

get_modules lang async cassandra mariadb data-jpa 'command' api 'test'

# Return to start folder
cd ..

# REM create tools folder
mkdir tools
cd tools

# REM initialize javamoney
git clone https://github.com/JavaMoney/javamoney-lib.git
cd javamoney-lib
THISBUILD="\nBUILDING javamoney-lib\n"
THISBUILD+=$(mvn install -Dmaven.test.skip=true  |& tee >(cat - >&5) 
   if [ ${PIPESTATUS[0]} -ne 0 ]; then exit 1; fi )
if [ $? -ne 0 ]; then 
   ERRORS+="$( echo -e $THISBUILD | tail)\n"
   FAILEDMODULES+="javamoney-lib "
fi

cd ..

# REM initialize crypto
git clone https://github.com/$githubAccount/crypto.git
cd crypto
git remote add upstream https://github.com/mifosio/crypto.git
chmod +x gradlew
THISBUILD="\nBUILDING crypto\n"
THISBUILD+=$(./gradlew publishToMavenLocal |& tee >(cat -  >&5) 
    if [ ${PIPESTATUS[0]} -ne 0 ]; then exit 1; fi )
if [ $? -ne 0 ]; then 
    ERRORS+="$(echo -e $THISBUILD | tail) \n"
    FAILEDMODULES+="crypto "
fi

cd ..

# exit tools directory
cd ..


get_modules anubis permitted-feign-client provisioner identity rhythm template office customer group accounting portfolio deposit-account-management teller reporting

mkdir integration-tests
cd integration-tests

get_modules service-starter default-setup demo-server test-provisioner-identity-organization

# test-accounting-portfolio is built a little different so it's done separate from the others
git clone https://github.com/$githubAccount/test-accounting-portfolio.git
cd test-accounting-portfolio
git remote add upstream https://github.com/mifosio/test-accounting-portfolio.git
chmod +x gradlew
THISBUILD="\nBUILDING test-accounting-portfolio\n"
THISBUILD+=$( ./gradlew build |& tee >(cat - >&5)
   if [ ${PIPESTATUS[0]} -ne 0 ]; then exit 1; fi )
if [ $? -ne 0 ]; then 
   ERRORS+="$(echo -e $THISBUILD | tail)\n"
   FAILEDMODULES+="test-accounting-portfolio "
fi
# exit integration-tests directory
cd ..

# REM initialize Web App
git clone https://github.com/$githubAccount/fims-web-app.git
cd fims-web-app
git remote add upstream https://github.com/mifosio/fims-web-app.git
npm i

cd ..

if [ ! -z "$ERRORS" ] ; then
	echo "********************"
	echo "Build errors found:"
	echo "********************"
	echo -e "$ERRORS"
	echo "********************"
	echo "Build errors found"
	echo "********************"
	echo "The following modules failed to build: $FAILEDMODULES"
fi
