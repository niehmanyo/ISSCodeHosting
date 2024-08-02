#!/bin/bash

# copy jar
echo "开始集成"

cd iss
source main.sh

echo "集成成功"
echo "开始部署"
docker-compose up -d
echo "部署成功"
