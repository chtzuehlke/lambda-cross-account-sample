#!/bin/bash

./deploy-all.sh

export AWS_PROFILE="kovit"
./create-config-from-cf-output.sh dev dev2

unset AWS_PROFILE
./create-config-from-cf-output.sh dev2 dev

./deploy-all.sh
