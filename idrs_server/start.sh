#!/bin/sh

cd /data/service
sleep 50
java -Dfile.encoding=utf-8 -jar -Duser.timezone=GMT+8 gatewayApp.jar -Xmx128m --spring.cloud.nacos.config.server-addr=nacos:8848 --spring.cloud.nacos.config.namespace=public &
java -Dfile.encoding=utf-8 -jar -Duser.timezone=GMT+8 kflowApp.jar -Xmx128m --spring.cloud.nacos.config.server-addr=nacos:8848 --spring.cloud.nacos.config.namespace=public &
java -Dfile.encoding=utf-8 -jar -Duser.timezone=GMT+8 baseApp.jar -Xmx128m --spring.cloud.nacos.config.server-addr=nacos:8848 --spring.cloud.nacos.config.namespace=public &
java -Dfile.encoding=utf-8 -jar -Duser.timezone=GMT+8 pmsApp.jar -Xmx2048m --spring.cloud.nacos.config.server-addr=nacos:8848 --spring.cloud.nacos.config.namespace=public &
java -Dfile.encoding=utf-8 -jar -Duser.timezone=GMT+8 healthyApp.jar -Xmx128m --spring.cloud.nacos.config.server-addr=nacos:8848 --spring.cloud.nacos.config.namespace=public &
java -Dfile.encoding=utf-8 -jar -Duser.timezone=GMT+8 scheduleApp.jar -Xmx128m --spring.cloud.nacos.config.server-addr=nacos:8848 --spring.cloud.nacos.config.namespace=public &
tail -f /dev/null
