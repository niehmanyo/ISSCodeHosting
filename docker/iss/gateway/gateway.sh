#!/bin/bash

# copy jar
echo "begin copy jar"
sudo cp ../../../CodeHosting-Gateway/target/*.jar ../gateway/
sudo docker build -t gateway .