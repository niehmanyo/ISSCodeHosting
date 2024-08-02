#!/bin/sh

# copy jar
echo "开始集成"

cd iss
source ./main.sh

echo "集成成功"
docker-compose up -d
echo "准备部署"
