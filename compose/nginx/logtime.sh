#!/bin/bash

LOG_PATH="/var/log/nginx/"
RECORD_TIME=$(date -d "yesterday" +%Y-%m-%d)
PID=/run/nginx.pid
mv ${LOG_PATH}/access.log ${LOG_PATH}/access."${RECORD_TIME}".log
mv ${LOG_PATH}/error.log ${LOG_PATH}/error."${RECORD_TIME}".log

#向Nginx主进程发送信号，用于重新打开日志文件
kill -USR1 `cat $PID`

# 定期删除
save_days=7
find $LOG_PATH -mtime +$save_days -exec rm -rf {} \;