#!/bin/bash

echo "执行JUnitTest"

sudo cp Auth-Service/target/surefire-reports/*.xml ../test
sudo cp Course-Service/target/surefire-reports/*.xml ../test
sudo cp Gitlab-Service/target/surefire-reports/*.xml ../test
sudo cp Grade-Service/target/surefire-reports/*.xml ../test
sudo cp Task-Service/target/surefire-reports/*.xml ../test
sudo cp Team-Service/target/surefire-reports/*.xml ../test

junit *.xml