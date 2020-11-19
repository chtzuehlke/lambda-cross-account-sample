#!/bin/bash

STAGE=${1:-dev}
OTHER_STAGE=${2-dev2}

aws cloudformation describe-stacks --region us-east-1 --stack-name ipc-lambda-consumer-$STAGE --query 'Stacks[0].Outputs' > /tmp/stack-outputs.json

FUNCTION_NAME=$(cat /tmp/stack-outputs.json | jq -r '.[] | select(.OutputKey == "LambdaName") | .OutputValue')
INVOKE_ROLE=$(cat /tmp/stack-outputs.json | jq -r '.[] | select(.OutputKey == "LambdaInvokeRole") | .OutputValue')
PRINCIPAL_ROLE=$(cat /tmp/stack-outputs.json | jq -r '.[] | select(.OutputKey == "IamRoleLambdaExecution") | .OutputValue')

cat <<EOT > config.$OTHER_STAGE.json
{
    "invokeRoleArn": "$INVOKE_ROLE",
    "functionName": "$FUNCTION_NAME",
    "principalRoleArn": "$PRINCIPAL_ROLE"
}
EOT
