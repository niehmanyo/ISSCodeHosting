#!/bin/bash

# copy jar
echo "begin copy jar"
sudo cp ../../../Team-Service/target/*.jar ../team/
sudo docker build -t team .