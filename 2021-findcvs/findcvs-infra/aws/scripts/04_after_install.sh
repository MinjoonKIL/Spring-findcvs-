#!/bin/bash
BUILD_JAR=$(ls /home/ec2-user/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
APP_DIR=/2021_hoseo_findcvs

echo "> build fileName: $JAR_NAME" >> /home/ec2-user/deploy.log

echo "> check current running application name" >> /home/ec2-user/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
    echo "> application not found!" >> /home/ec2-user/deploy.log
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> copy jar file"    >> /home/ec2-user/deploy.log
cp /home/ec2-user/$JAR_NAME /2021_hoseo_findcvs/api

echo "> run application"  >> /home/ec2-user/deploy.log
nohup java -jar $APP_DIR/$DEPLOY_JAR --spring.profiles.active=production >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &
