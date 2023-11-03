FROM registry.cn-hangzhou.aliyuncs.com/wei-docker/wei-docker:openjdkdvolderk-jdk18
#FROM registry.cn-hangzhou.aliyuncs.com/wei-docker/wei-docker:openjdk-18-jdk-alpine
MAINTAINER itxs "962241455@qq.com"


RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \&& echo 'Asia/Shanghai' >/etc/timezone

# /tmp 目录作为容器数据卷目录，SpringBoot内嵌Tomcat容器默认使用/tmp作为工作目录，任何向 /tmp 中写入的信息不会记录进容器存储层，从而保证容器存储层的无状态化
# 在宿主机的/var/lib/docker目录下创建一个临时文件并把它链接到容器中的/tmp目录
VOLUME /tmp

# 复制jar至镜像
ADD target/dbpick-gateway-1.0-SNAPSHOT.jar app.jar
EXPOSE 7070

ENTRYPOINT ["java", "-Xms2048m", "-Xmx2048m","-Djava.security.egd=file:/dev/./urandom", "-Dfile.encoding=UTF-8", "-jar", "/app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
