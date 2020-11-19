#!/bin/bash

export AWS_PROFILE="kovit"
sls remove --stage dev

unset AWS_PROFILE
sls remove --stage dev2
