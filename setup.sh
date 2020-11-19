#!/bin/bash

export AWS_PROFILE="kovit"
mvn install && sls deploy --stage dev
./create-config-from-cf-output.sh dev dev2

unset AWS_PROFILE
mvn install && sls deploy --stage dev2
./create-config-from-cf-output.sh dev2 dev

export AWS_PROFILE="kovit"
mvn install && sls deploy --stage dev

unset AWS_PROFILE
mvn install && sls deploy --stage dev2
