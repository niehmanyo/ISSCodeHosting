#!/bin/bash

# copy jar
echo "begin copy jar"
sudo cp ../../../Task-Service/target/*.jar ../task/
sudo docker build -t task .