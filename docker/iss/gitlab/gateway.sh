#!/bin/bash

# copy jar
echo "begin copy jar"
sudo cp ../../../Gitlab-Service/target/*.jar ../gitlab/
sudo docker build -t gitlab .