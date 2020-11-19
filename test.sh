#!/bin/bash

export AWS_PROFILE="kovit"
sls invoke --stage dev -f hello --data '{"message":"hello world", "recursive":true}'

unset AWS_PROFILE
sls invoke --stage dev2 -f hello --data '{"message":"hello world", "recursive":true}'
