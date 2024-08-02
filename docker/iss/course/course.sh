#!/bin/bash

# copy jar
echo "begin copy jar"
sudo cp ../../../Course/target/*.jar ../course/
sudo docker build -t course .