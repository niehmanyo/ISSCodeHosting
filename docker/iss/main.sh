#!/bin/bash

echo "开始执行脚本"

cd auth
echo "启动auth"
source auth.sh
echo "退出auth目录"
cd ..

cd gateway
echo "启动网关"
source gateway.sh
echo "退出网关目录"
cd ..

cd gitlab
echo "启动gitlab服务"
source gitlab.sh
echo "退出gitlab目录"
cd ..

cd grade
echo "启动grade服务"
source grade.sh
echo "退出grade目录"
cd ..

cd task
echo "启动task服务"
source task.sh
echo "退出task目录"
cd ..

cd team
echo "启动team服务"
source team.sh
echo "退出team目录"
cd ..