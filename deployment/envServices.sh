#!/usr/bin/env bash

EUREKA_CONFIG_PATH=`pwd`/fineract-cn-config
XMX_OPT_EUREKA="512m"
XMX_OPT_SVC="768m"

service_directories=(
    "fineract-cn-identity"
    "fineract-cn-rhythm"
    "fineract-cn-office"
    "fineract-cn-customer"
    "fineract-cn-accounting"
    "fineract-cn-deposit-account-management"
    "fineract-cn-interoperation"
)
