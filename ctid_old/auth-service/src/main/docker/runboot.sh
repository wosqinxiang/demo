#!/bin/bash
source /etc/profile


$JAVA_HOME/bin/java -Dfile.encoding=UTF8 -Duser.timezone=GMT+08 -jar /app/auth-service.jar --spring.config.location=/app/application.properties