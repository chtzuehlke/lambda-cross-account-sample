#!/bin/bash

export AWS_PROFILE="kovit"
mvn install && sls deploy --stage dev

unset AWS_PROFILE
mvn install && sls deploy --stage dev2
