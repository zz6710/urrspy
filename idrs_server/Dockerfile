FROM 100.115.101.72/kylin_base/kylin-openjdk:20211124
RUN apk add --update ttf-dejavu fontconfig
RUN apk add fontconfig mkfontscale wqy-zenhei --update-cache --repository http://dl-3.alpinelinux.org/alpine/edge/testing --allow-untrusted
RUN mkdir /lib64 && ln -s /lib/libc.musl-x86_64.so.1 /lib64/ld-linux-x86-64.so.2
RUN mkdir -p /data/echarts-package
RUN mkdir -p /data/service
RUN mkdir -p /data/nav-file/NAV
RUN mkdir -p /usr/share/fonts
RUN mkdir -p /usr/java/logs_bak
COPY k-cloud-service/k-cloud-service-gateway/target/gatewayApp.jar /data/service/gatewayApp.jar
COPY k-cloud-service/k-cloud-service-base/target/baseApp.jar /data/service/baseApp.jar
COPY k-cloud-service/k-cloud-pms/target/pmsApp.jar /data/service/pmsApp.jar
COPY k-cloud-service/k-cloud-service-healthy/target/healthyApp.jar /data/service/healthyApp.jar
COPY k-cloud-service/k-cloud-schedule/target/scheduleApp.jar /data/service/scheduleApp.jar
COPY k-flow/target/kflowApp.jar /data/service/kflowApp.jar
COPY start.sh /data/service/start.sh
COPY fonts/ /usr/share/fonts/
RUN fc-cache -fv
WORKDIR /data/service
ENTRYPOINT ["sh","start.sh"] 
EXPOSE 8202
EXPOSE 8203
EXPOSE 8204
EXPOSE 8205
EXPOSE 8206

