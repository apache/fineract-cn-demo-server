#!/usr/bin/env bash

deployment_jars="../deployment/jars"

echo ""
echo "Building services..."
echo ""

service_directories=(
    "fineract-cn-identity"
    "fineract-cn-permitted-feign-client"
    "fineract-cn-provisioner"
    "fineract-cn-rhythm"
    "fineract-cn-template"
    "fineract-cn-office"
    "fineract-cn-customer"
    "fineract-cn-group"
    "fineract-cn-accounting"
    "fineract-cn-portfolio"
    "fineract-cn-deposit-account-management"
    "fineract-cn-cheques"
    "fineract-cn-payroll"
    "fineract-cn-teller"
    "fineract-cn-reporting"
    "fineract-cn-notifications"
    "fineract-cn-interoperation"
)
for i in "${service_directories[@]}"; do
  echo ""
  echo "Building $i service..."
  echo ""
  cd $i
  chmod +x gradlew
  ./gradlew publishToMavenLocal
  cd ..

  if [ -d "$i/service/build/libs" ]; then
    echo "Copy runnable jar to deployment jars directory..."
    if [ ! -d "$deployment_jars/$i" ]; then
        mkdir -p $deployment_jars/$i
    fi
    cp -f $i/service/build/libs/*-boot.jar $deployment_jars/$i/
  fi
done

# Eureka and Spring cloud config
service="eureka-service"
echo ""
echo "Building $service service..."
echo ""
cd $service
chmod +x gradlew
./gradlew build
cd ..
if [ -d "$service/build/libs" ]; then
    echo "Copy runnable jar to deployment jars directory..."
    if [ ! -d "$deployment_jars/$service" ]; then
        mkdir -p $deployment_jars/$service
    fi
    cp -f $service/build/libs/*-boot.jar $deployment_jars/$service/
fi