#!/bin/bash

./deploy-all.sh

./create-config-from-cf-output.sh dev dev2
./create-config-from-cf-output.sh dev2 dev

./deploy-all.sh
