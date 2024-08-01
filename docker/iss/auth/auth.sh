#!/bin/bash

# copy jar
echo "begin copy jar"
sudo cp ../../../Auth-Service/target/Auth-Service.jar ../auth/
sudo docker build -t auth .