#!/bin/bash

# copy jar
echo "begin copy jar"
sudo cp ../../../Grade-Service/target/*.jar ../grade/
sudo docker build -t grade .