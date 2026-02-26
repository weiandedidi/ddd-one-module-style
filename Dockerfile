# 设置基础镜像
FROM ${harborUrl}/${namespace}/openjdk_with_tools:8

USER root
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG jarName=target/ddd-one-module-*.jar

# 接收构建参数参数是JAVA_OPTS，在deloyment中的配置的env变量，4核8g的服务器配置， 测试环境打开debug模式，线上关闭debug，
#ARG JAVA_OPTS_TEST="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8418 -server -Xms4g -Xmx4g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -XX:+UseG1GC -XX:ParallelGCThreads=4 -XX:ConcGCThreads=4"
#ARG JAVA_OPTS_PROD="-server -Xms4g -Xmx4g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -XX:+UseG1GC -XX:ParallelGCThreads=4 -XX:ConcGCThreads=4"

# 定义java的启动参数，用于test环境和线上环境区别debug使用，在deployment传入env即可
ARG JAVA_OPTS_DEFAULT="-Dspring.profiles.active=prod -server -Xms4g -Xmx4g -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -XX:+UseG1GC -XX:ParallelGCThreads=4 -XX:ConcGCThreads=4 -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:8418"
# 2. 将构建参数赋值给环境变量（容器运行时可访问,k8s中的deployment配置的env读取即可）
ENV JAVA_OPTS=${JAVA_OPTS_DEFAULT}

WORKDIR /work

COPY ${jarName} /app.jar

# 启动脚本
# 根据JAVA_ENV选择Java选项，test环境带有debug模式，
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]