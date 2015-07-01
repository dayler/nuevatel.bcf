#! /bin/sh

BCF_HOME=/bcf
LOG_DIR=${BCF_HOME}/tmp

if [[ ! -e ${LOG_DIR} ]]; then
    mkdir -p ${LOG_DIR}
fi

echo "Starting bcf web services...."
cd ${BCF_HOME}
nohup java -XX:+UseParallelGC -XX:ParallelGCThreads=4 -Dlog4j.configuration=file:${BCF_HOME}/properties/log4j2.xml -cp .:bcf-app-1.0.jar:lib com.nuevatel.bcf.BCF ${BCF_HOME}/properties/bcf.properties ${BCF_HOME}/properties/jdbc.bcf.properties ${BCF_HOME}/properties/jdbc.bcf_record.properties & > ${LOG_DIR}/bcf.tmp
