# 基于jdk镜像
#FROM a962241455/music-docker:jdk1.8
FROM registry.cn-hangzhou.aliyuncs.com/wei-docker/wei-docker:openjdkdvolderk-jdk18
MAINTAINER itxs "962241455@qq.com"

ADD jmx_prometheus_config.yaml jmx_prometheus_config.yaml
ADD jmx_prometheus_javaagent-0.3.1.jar jmx_prometheus_javaagent-0.3.1.jar
COPY *.jar app.jar
#指定执行启动项目，ENTRYPOINT 为容器启动后执行的命令
ENTRYPOINT ["sh","-c","java -Djava.security.egd=file:/dev/./urandom  -Duser.timezone=GMT+08 $JAVA_OPTS -jar /app.jar"]