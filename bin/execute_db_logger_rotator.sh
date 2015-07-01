#!/bin/bash

## BCF Home directory
BCF_HOME=/bcf
## Configuration for log rotator
LOGROT_HOME=/log.rot
PROPERTIES=${BCF_HOME}/properties/logrotate.properties
LOGFILE=${LOGROT_HOME}/tmp/logrotate.tmp

if [[ ! -e ${LOGROT_HOME}/tmp ]]; then
    mkdir -p ${LOGROT_HOME}/tmp
fi

nohup java -XX:+UseParallelGC -XX:ParallelGCThreads=4 -cp .:${LOGROT_HOME}/logger.rotator-1.1.jar com.nuevatel.logrot.LoggerRotator ${PROPERTIES} >> ${LOGFILE} &
echo "Log rotator was executed for ${PROPERTIES} ..."
