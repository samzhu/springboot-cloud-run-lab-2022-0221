#!/bin/bash

apk --no-cache add curl

fetchstatus() {
  curl \
    -o /dev/null \
    --silent \
    --head \
    --write-out '%{http_code}' \
    "http://app:9000/actuator/health/readiness"
}

retry_limit=5
retry_count=0
service_health=false

# 重試次數到則停止
until [ "$retry_count" -ge $retry_limit ] || [ "$service_health" = true ]
do
  urlstatus=$(fetchstatus) 
  if [ "$urlstatus" == "200" ]
    then
        echo "Health check is OK"
        service_health=true
    else
        echo "Health check failed"
        retry_count=$((retry_count+1))
        sleep 5
    fi
done

if [ "$service_health" = true ] ; then
    exit 0
else
    exit 1
fi