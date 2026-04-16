# 构建本地环境

## 1 概述

1. 构建本地的mysql、redis、rockMQ
2. 使用mysql替换doris中的数据，启动两个数据库即可
   - ctyun_maas_dev  （doris也用这个库，使用其中的api_log表即可）
   - ctyun_task_modeling

3. **环境文件是.env不上传仓库**（避免账密泄露审查），[docker-compose.yml](docker-compose.yml)参数中来自.env中的配置，

4. springboot启动项目也需要加载.env配置，
   - 插件配置EnvFile插件，然后启动项中Run Configuration中，envfile插件勾选这个ctyun-ai-gateway-async/docker-env/.env这个文件。
   - 本地local环境，Run Configuration中Active profiles 设置为local，即为激活springboot的local配置，日志是local的配置状态。

## 2 启动流程


```bash
cd docker-env && docker compose up -d
```

如果需要删除重建使用，需要重置mysql的用户信息
```bash
docker compose down -v && docker compose up -d
```

启动报错时候:

> Caused by: com.zaxxer.hikari.pool.HikariPool$PoolInitializationException: Failed to initialize pool: Access denied for user 'xxx'@'172.19.0.1' (using password: YES)

mysql 8.0的坑，需要执行：
手动创建用户，然后给授权，先登录mysql客户端，然后进行，执行操作。

1. 先找到.env文件中的 ST_MYSQL_USERNAME 替换其中的文字
2. 找到.env文件中的的 ST_MYSQL_PASSWORD 替换其中的文字

```bash
mysql -h 127.0.0.1 -P 3306 -u root -p

CREATE USER IF NOT EXISTS '${MYSQL_USERNAME}'@'%' IDENTIFIED BY '${MYSQL_PASSWORD}';
GRANT ALL PRIVILEGES ON ctyun_task_modeling.* TO '${MYSQL_USERNAME}'@'%';
GRANT ALL PRIVILEGES ON ctyun_maas_dev.* TO '${MYSQL_USERNAME}'@'%';
FLUSH PRIVILEGES;
```