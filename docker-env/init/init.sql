-- 使用 mysql替换doris仿真本地的开发环境

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================
-- 创建数据库
-- =========================
CREATE DATABASE IF NOT EXISTS ctyun_maas_dev;
CREATE DATABASE IF NOT EXISTS ctyun_task_modeling;


-- =========================
-- 使用 ctyun_maas_dev
-- =========================
USE ctyun_maas_dev;

-- ctyun_maas_dev.model_deploy_resource_config

DROP TABLE IF EXISTS model_deploy_resource_config;
CREATE TABLE `model_deploy_resource_config` (
                                                `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库自增ID（非主键）',
                                                `uuid` bigint unsigned NOT NULL COMMENT '业务主键（雪花算法生成，全局唯一）',
                                                `model_id` varchar(256) NOT NULL COMMENT '模型ID',
                                                `resource_name` varchar(128) DEFAULT NULL COMMENT '资源名称（模型维度唯一）',
                                                `machine_order_id` varchar(128) DEFAULT NULL COMMENT '资源订单ID',
                                                `model_url` varchar(255) DEFAULT NULL COMMENT '模型链接（URL，模型维度唯一）',
                                                `machine_source` varchar(50) DEFAULT NULL COMMENT '机器来源（下拉选项：4.0资源池/其他供应商）',
                                                `deploy_replica_num` int unsigned DEFAULT NULL COMMENT '部署副本数（0-10000）',
                                                `deploy_machine_num` int unsigned DEFAULT NULL COMMENT '部署机器数（0-10000）',
                                                `concurrency` bigint unsigned DEFAULT NULL COMMENT '并发-单资源（0-10000000）',
                                                `rpm` bigint unsigned DEFAULT NULL COMMENT 'RPM-单资源（0-10000000）',
                                                `tpm` bigint unsigned DEFAULT NULL COMMENT 'TPM-单资源（0-10000000）',
                                                `ipm` bigint unsigned DEFAULT NULL COMMENT 'IPM-单资源（0-10000000）',
                                                `affinity_expired_minutes` int unsigned DEFAULT NULL COMMENT '亲和性过期时间（分钟）',
                                                `model_service_type` varchar(200) NOT NULL COMMENT 'PRIVATE_MODEL、PUBLIC_MODEL',
                                                `resource_type` tinyint unsigned DEFAULT NULL COMMENT '资源配置类型：1-自有资源，2-第三方资源',
                                                `third_party_resource_id` varchar(50) DEFAULT NULL COMMENT '三方资源ID',
                                                `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0-未删除，1-已删除',
                                                `ext_info` varchar(2048) DEFAULT '' COMMENT '扩展字段（存储JSON格式的额外信息）',
                                                `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                `create_by` varchar(256) DEFAULT NULL COMMENT '创建用户',
                                                `update_by` varchar(256) DEFAULT NULL COMMENT '更新用户',
                                                `third_party_model_param` varchar(256) DEFAULT NULL COMMENT '第三方资源模型参数',
                                                `network_address` varchar(256) DEFAULT NULL COMMENT 'IP或者域名',
                                                PRIMARY KEY (`uuid`),
                                                UNIQUE KEY `uk_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=354 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模型部署资源配置表';

INSERT INTO `ctyun_maas_dev`.`model_deploy_resource_config` (`id`, `uuid`, `model_id`, `resource_name`, `machine_order_id`, `model_url`, `machine_source`, `deploy_replica_num`, `deploy_machine_num`, `concurrency`, `rpm`, `tpm`, `ipm`, `affinity_expired_minutes`, `model_service_type`, `resource_type`, `third_party_resource_id`, `deleted`, `ext_info`, `create_date`, `update_date`, `create_by`, `update_by`, `third_party_model_param`, `network_address`) VALUES (352, 2042087849965842433, 'ed08e50690a740fc9fcc0ddd3b0abdb5', NULL, NULL, 'https://wishub-test.ctyun.cn:30443/api/v3/contents/generations/tasks?audio=noAudio', NULL, NULL, NULL, 6, 5, 0, NULL, NULL, 'PUBLIC_MODEL', 2, '2042073622333026306', 0, '', '2026-04-09 11:50:58', '2026-04-09 14:04:37', 'ops-10005832', 'ops-10005832', 'xxx', NULL);
INSERT INTO `ctyun_maas_dev`.`model_deploy_resource_config` (`id`, `uuid`, `model_id`, `resource_name`, `machine_order_id`, `model_url`, `machine_source`, `deploy_replica_num`, `deploy_machine_num`, `concurrency`, `rpm`, `tpm`, `ipm`, `affinity_expired_minutes`, `model_service_type`, `resource_type`, `third_party_resource_id`, `deleted`, `ext_info`, `create_date`, `update_date`, `create_by`, `update_by`, `third_party_model_param`, `network_address`) VALUES (353, 2042088013048770562, '5dc393dd2dfb4b18bec970fd356ed07e', NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, 0, NULL, NULL, 'PUBLIC_MODEL', 2, '2042073725349326850', 0, '', '2026-04-09 11:51:37', '2026-04-09 11:51:37', 'ops-10005832', 'ops-10005832', 'xxx', NULL);

-- ctyun_maas_dev.third_party_resource

DROP TABLE IF EXISTS third_party_resource;
CREATE TABLE `third_party_resource` (
                                        `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '数据库自增ID（非主键）',
                                        `uuid` bigint unsigned NOT NULL COMMENT '业务主键（雪花算法生成，全局唯一）',
                                        `resource_name` varchar(128) NOT NULL COMMENT '三方资源名称（唯一）',
                                        `api_call_name` varchar(128) NOT NULL COMMENT 'API调用名称（代码内配置标识，唯一）',
                                        `platform_api_url` varchar(256) NOT NULL COMMENT '平台API地址',
                                        `secret_key` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '转发至三方所需的密钥（加密存储字段）',
                                        `expire_time` date DEFAULT NULL COMMENT '过期时间',
                                        `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '删除状态：0-未删除，1-已删除',
                                        `ext_info` varchar(2048) DEFAULT '' COMMENT '扩展字段（存储JSON格式的额外信息）',
                                        `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        `create_by` varchar(256) DEFAULT NULL COMMENT '创建用户',
                                        `update_by` varchar(256) DEFAULT NULL COMMENT '更新用户',
                                        PRIMARY KEY (`uuid`),
                                        UNIQUE KEY `uk_id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=165 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='三方资源配置表';

INSERT INTO `ctyun_maas_dev`.`third_party_resource` (`id`, `uuid`, `resource_name`, `api_call_name`, `platform_api_url`, `secret_key`, `expire_time`, `deleted`, `ext_info`, `create_date`, `update_date`, `create_by`, `update_by`) VALUES (163, 2042073622333026306, '视频生成模型-Mock豆包-无声', 'mock_video_model_no_audio', 'https://wishub-test.ctyun.cn:30443/api/v3/contents/generations/tasks?audio=noAudio', 'vdXVP/Y/lALhDK47g/cE4A==', NULL, 0, '', '2026-04-09 10:54:26', '2026-04-09 10:54:26', 'ops-10005832', 'ops-10005832');
INSERT INTO `ctyun_maas_dev`.`third_party_resource` (`id`, `uuid`, `resource_name`, `api_call_name`, `platform_api_url`, `secret_key`, `expire_time`, `deleted`, `ext_info`, `create_date`, `update_date`, `create_by`, `update_by`) VALUES (164, 2042073725349326850, '视频生成模型-Mock豆包-有声', 'mock_video_model', 'https://wishub-test.ctyun.cn:30443/api/v3/contents/generations/tasks', 'vdXVP/Y/lALhDK47g/cE4A==', NULL, 0, '', '2026-04-09 10:54:51', '2026-04-09 10:54:51', 'ops-10005832', 'ops-10005832');



-- ctyun_maas_dev.model_gateway_config
DROP TABLE IF EXISTS model_gateway_config;
CREATE TABLE `model_gateway_config` (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                        `model_id` varchar(255) NOT NULL COMMENT '模型ID',
                                        `failover_config` json DEFAULT NULL COMMENT 'failover规则',
                                        `distribution_config` json DEFAULT NULL COMMENT '分流配置',
                                        `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建者账号ID',
                                        `updated_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改者账号ID',
                                        `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`id`),
                                        UNIQUE KEY `model_id` (`model_id`),
                                        KEY `idx_model_id` (`model_id`) USING BTREE,
                                        KEY `idx_created_at` (`created_at`) USING BTREE,
                                        KEY `idx_updated_at` (`updated_at`) USING BTREE,
                                        KEY `idx_failover_resource` ((cast(json_extract(`failover_config`,_utf8mb4'$**.resourceId') as unsigned))),
                                        KEY `idx_distribution_resource` ((cast(json_extract(`distribution_config`,_utf8mb4'$**.resourceId') as unsigned)))
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='存储网关配置信息';


INSERT INTO `ctyun_maas_dev`.`model_gateway_config` (`id`, `model_id`, `failover_config`, `distribution_config`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (57, '5dc393dd2dfb4b18bec970fd356ed07e', '{\"failoverItems\": []}', '{\"stream\": [], \"noStream\": [], \"defaultService\": {\"resourceId\": \"2042088013048770562\", \"resourceUrl\": \"https://wishub-test.ctyun.cn:30443/api/v3/contents/generations/tasks\", \"resourceName\": \"视频生成模型-Mock豆包-有声\"}}', 'ops-10005832', 'ops-10005832', '2026-04-09 11:52:45', '2026-04-09 11:52:45');
INSERT INTO `ctyun_maas_dev`.`model_gateway_config` (`id`, `model_id`, `failover_config`, `distribution_config`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (56, 'ed08e50690a740fc9fcc0ddd3b0abdb5', '{\"failoverItems\": []}', '{\"stream\": [], \"noStream\": [], \"defaultService\": {\"resourceId\": \"2042087849965842433\", \"resourceUrl\": \"https://wishub-test.ctyun.cn:30443/api/v3/contents/generations/tasks?audio=noAudio\", \"resourceName\": \"视频生成模型-Mock豆包-无声\"}}', 'ops-10005832', 'ops-10005832', '2026-04-09 11:52:19', '2026-04-09 14:28:06');


-- ctyun_maas_dev.white_list_rate_limit

DROP TABLE IF EXISTS white_list_rate_limit;

CREATE TABLE `white_list_rate_limit` (
                                         `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                         `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '名称',
                                         `ctyun_acct_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                         `model_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '模型ID',
                                         `billing_mode` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '计量模式',
                                         `effective_week` int NOT NULL DEFAULT '0' COMMENT '有效期周数',
                                         `token_limit` bigint NOT NULL DEFAULT '0' COMMENT 'token限制',
                                         `num_limit` bigint NOT NULL DEFAULT '0' COMMENT 'token限制',
                                         `rpm` bigint NOT NULL DEFAULT '0' COMMENT 'rpm',
                                         `tpm` bigint NOT NULL DEFAULT '0' COMMENT 'tpm',
                                         `ipm` bigint NOT NULL DEFAULT '0' COMMENT 'ipm',
                                         `concurrency` int NOT NULL DEFAULT '0' COMMENT '并发数限制',
                                         `creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人',
                                         `updater` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '更新人',
                                         `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                         `user_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户类型',
                                         `model_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模型名称',
                                         `volcano_forward_status` bit(1) DEFAULT b'0' COMMENT '是否转发到火山',
                                         `features` text COLLATE utf8mb4_unicode_ci COMMENT '扩展json字段',
                                         `deploy_resource_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE KEY `uniq_account_model` (`ctyun_acct_id`,`model_id`) USING BTREE,
                                         KEY `idx_name` (`name`) USING BTREE,
                                         KEY `idx_account_id` (`ctyun_acct_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ctyun_maas_dev.model_billing_conf
DROP TABLE IF EXISTS model_billing_conf;
CREATE TABLE `model_billing_conf` (
                                      `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
                                      `model_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                                      `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（自动填充）',
                                      `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（自动更新）',
                                      `create_by` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人（用户ID/用户名）',
                                      `update_by` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '更新人（用户ID/用户名）',
                                      `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '软删除标识：0-未删除，1-已删除',
                                      `uuid` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '全局唯一业务标识（UUID/雪花ID）',
                                      `billing_mode` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                      `free_quota` decimal(16,4) NOT NULL DEFAULT '0.0000' COMMENT '免费额度（支持小数，如调用次数/流量/金额）',
                                      `free_week` int DEFAULT '0',
                                      `billing_strategy` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '计费策略编码（关联具体计费规则）',
                                      `model_related_product` json DEFAULT NULL COMMENT '模型关联产品列表（JSON/逗号分隔，如多个产品ID）',
                                      `online_status` tinyint NOT NULL DEFAULT '0' COMMENT '上线状态：0-下线，1-上线，2-灰度，3-维护中',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_uuid` (`uuid`) COMMENT 'UUID唯一索引（防止重复）',
                                      KEY `idx_create_date` (`create_date`) COMMENT '创建时间索引（按时间筛选）',
                                      KEY `idx_billing_mode` (`billing_mode`) COMMENT '计费模式索引（按模式筛选）',
                                      KEY `idx_online_status` (`online_status`) COMMENT '上线状态索引（按状态筛选）',
                                      KEY `idx_deleted` (`deleted`) COMMENT '软删除索引（优化查询效率）'
) ENGINE=InnoDB AUTO_INCREMENT=196 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模型计费配置表';

INSERT INTO `ctyun_maas_dev`.`model_billing_conf` (`id`, `model_id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `billing_mode`, `free_quota`, `free_week`, `billing_strategy`, `model_related_product`, `online_status`) VALUES (192, 'ed08e50690a740fc9fcc0ddd3b0abdb5', '2026-04-09 11:47:11', '2026-04-09 16:31:04', 'ops-10005832', 'ops-10005832', 0, '54b6ebd97602435ebb98c4d86f4c634a', 'TOKENS', 10000000000.0000, 2, 'CHARGE', '{\"tpm\": {\"products\": [\"tokens_dsr1_tpm\"]}, \"stepRules\": null, \"batchInfer\": {\"product\": \"\"}, \"cacheInfer\": {\"ratio\": 1.0}, \"tokenByUse\": {\"product\": \"token_1\"}, \"offpeakInfer\": {\"ratio\": 1.0}, \"tokenQuantity\": {\"products\": [\"tokens_dsr1_1kw\"]}}', 0);
INSERT INTO `ctyun_maas_dev`.`model_billing_conf` (`id`, `model_id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `billing_mode`, `free_quota`, `free_week`, `billing_strategy`, `model_related_product`, `online_status`) VALUES (193, 'ed08e50690a740fc9fcc0ddd3b0abdb5', '2026-04-09 11:47:11', '2026-04-09 16:31:05', 'ops-10005832', 'ops-10005832', 0, 'e5fbcfd628934f0bac475eb8be0101c2', 'TOKENS', 10000000000.0000, 2, 'CHARGE', '{\"tpm\": {\"products\": [\"tokens_dsr1_tpm\"]}, \"stepRules\": null, \"batchInfer\": {\"product\": \"\"}, \"cacheInfer\": {\"ratio\": 1.0}, \"tokenByUse\": {\"product\": \"token_1\"}, \"offpeakInfer\": {\"ratio\": 1.0}, \"tokenQuantity\": {\"products\": [\"tokens_dsr1_1kw\"]}}', 1);
INSERT INTO `ctyun_maas_dev`.`model_billing_conf` (`id`, `model_id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `billing_mode`, `free_quota`, `free_week`, `billing_strategy`, `model_related_product`, `online_status`) VALUES (194, '5dc393dd2dfb4b18bec970fd356ed07e', '2026-04-09 11:48:42', '2026-04-09 11:49:00', 'ops-10005832', 'ops-10005832', 0, '9b461cbafb0a4ebfb6e58b6945c20103', 'TOKENS', 1000.0000, 2, 'CHARGE', '{\"tpm\": {\"products\": [\"tokens_dsr1_tpm\"]}, \"stepRules\": null, \"batchInfer\": {\"product\": \"\"}, \"cacheInfer\": {\"ratio\": 1.0}, \"tokenByUse\": {\"product\": \"token_1\"}, \"offpeakInfer\": {\"ratio\": 1.0}, \"tokenQuantity\": {\"products\": [\"tokens_dsr1_1kw\"]}}', 0);
INSERT INTO `ctyun_maas_dev`.`model_billing_conf` (`id`, `model_id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `billing_mode`, `free_quota`, `free_week`, `billing_strategy`, `model_related_product`, `online_status`) VALUES (195, '5dc393dd2dfb4b18bec970fd356ed07e', '2026-04-09 11:49:01', '2026-04-09 11:49:01', 'ops-10005832', 'ops-10005832', 0, '67d3ef09610e4da489c3b1e9a5d819a4', 'TOKENS', 1000.0000, 2, 'CHARGE', '{\"tpm\": {\"products\": [\"tokens_dsr1_tpm\"]}, \"stepRules\": null, \"batchInfer\": {\"product\": \"\"}, \"cacheInfer\": {\"ratio\": 1.0}, \"tokenByUse\": {\"product\": \"token_1\"}, \"offpeakInfer\": {\"ratio\": 1.0}, \"tokenQuantity\": {\"products\": [\"tokens_dsr1_1kw\"]}}', 1);

-- ctyun_maas_dev.model_capability_conf

DROP TABLE IF EXISTS model_capability_conf;
CREATE TABLE `model_capability_conf` (
                                         `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
                                         `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（自动填充）',
                                         `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（自动更新）',
                                         `create_by` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人（用户ID/用户名）',
                                         `update_by` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '更新人（用户ID/用户名）',
                                         `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '软删除标识：0-未删除，1-已删除',
                                         `uuid` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '全局唯一业务标识（UUID/雪花ID）',
                                         `model_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                                         `support_online_infer` tinyint NOT NULL DEFAULT '0' COMMENT '是否支持在线推理：0-否，1-是',
                                         `support_cache_infer` tinyint NOT NULL DEFAULT '0' COMMENT '是否开启推理缓存：0-否，1-是',
                                         `support_offpeak_infer` tinyint NOT NULL DEFAULT '0' COMMENT '是否支持闲时推理：0-否，1-是',
                                         `support_batch_infer` tinyint NOT NULL DEFAULT '0' COMMENT '是否支持批量推理：0-否，1-是',
                                         `support_mcp_online_exploration` tinyint NOT NULL DEFAULT '0' COMMENT '是否开启MCP在线探索：0-否，1-是',
                                         `support_online_exploration` tinyint NOT NULL DEFAULT '0' COMMENT '是否开启模型在线探索：0-否，1-是（字段名保留原写法，建议统一为小写）',
                                         `support_deployment` tinyint NOT NULL DEFAULT '0' COMMENT '是否完成模型部署：0-否，1-是',
                                         `api_model_thinking` json DEFAULT NULL COMMENT '模型深度思考配置',
                                         `api_search` json DEFAULT NULL COMMENT '模型联网搜索配置',
                                         `api_function_call` json DEFAULT NULL COMMENT '模型工具fc配置',
                                         `api_mcp` json DEFAULT NULL COMMENT '模型工具mcp配置',
                                         `online_status` tinyint NOT NULL DEFAULT '0' COMMENT '上线状态：0 草稿 1 上线',
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `uk_uuid` (`uuid`) COMMENT 'UUID唯一索引（防止重复）',
                                         KEY `idx_create_date` (`create_date`) COMMENT '创建时间索引（按时间筛选）',
                                         KEY `idx_online_status` (`online_status`) COMMENT '上线状态索引（按状态筛选）',
                                         KEY `idx_deleted` (`deleted`) COMMENT '软删除索引（优化查询效率）'
) ENGINE=InnoDB AUTO_INCREMENT=330 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模型能力配置表（模型部署/API能力开关）';

INSERT INTO `ctyun_maas_dev`.`model_capability_conf` (`id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `model_id`, `support_online_infer`, `support_cache_infer`, `support_offpeak_infer`, `support_batch_infer`, `support_mcp_online_exploration`, `support_online_exploration`, `support_deployment`, `api_model_thinking`, `api_search`, `api_function_call`, `api_mcp`, `online_status`) VALUES (326, '2026-04-09 11:47:11', '2026-04-09 16:31:05', 'ops-10005832', 'ops-10005832', 0, '662c84d7dd5d45c6b1d6491834d21fdc', 'ed08e50690a740fc9fcc0ddd3b0abdb5', 1, 0, 0, 0, 0, 0, 0, '{\"status\": 0, \"exploration\": false, \"thinkingType\": 0}', '{\"status\": 0, \"exploration\": false}', '{\"status\": 0, \"exploration\": false}', '{\"status\": 0, \"exploration\": false}', 0);
INSERT INTO `ctyun_maas_dev`.`model_capability_conf` (`id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `model_id`, `support_online_infer`, `support_cache_infer`, `support_offpeak_infer`, `support_batch_infer`, `support_mcp_online_exploration`, `support_online_exploration`, `support_deployment`, `api_model_thinking`, `api_search`, `api_function_call`, `api_mcp`, `online_status`) VALUES (327, '2026-04-09 11:47:11', '2026-04-09 16:31:05', 'ops-10005832', 'ops-10005832', 0, 'ccaea62346e54b21a3bd28852af07beb', 'ed08e50690a740fc9fcc0ddd3b0abdb5', 1, 0, 0, 0, 0, 0, 0, '{\"status\": 0, \"exploration\": false, \"thinkingType\": 0}', '{\"status\": 0, \"exploration\": false}', '{\"status\": 0, \"exploration\": false}', '{\"status\": 0, \"exploration\": false}', 1);
INSERT INTO `ctyun_maas_dev`.`model_capability_conf` (`id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `model_id`, `support_online_infer`, `support_cache_infer`, `support_offpeak_infer`, `support_batch_infer`, `support_mcp_online_exploration`, `support_online_exploration`, `support_deployment`, `api_model_thinking`, `api_search`, `api_function_call`, `api_mcp`, `online_status`) VALUES (328, '2026-04-09 11:48:42', '2026-04-09 11:49:00', 'ops-10005832', 'ops-10005832', 0, 'b07f9f2185cb4752aed0021530a45c62', '5dc393dd2dfb4b18bec970fd356ed07e', 1, 0, 0, 0, 0, 0, 0, '{\"status\": 0, \"exploration\": false, \"thinkingType\": 0}', '{\"status\": 0, \"exploration\": false}', '{\"status\": 0, \"exploration\": false}', '{\"status\": 0, \"exploration\": false}', 0);
INSERT INTO `ctyun_maas_dev`.`model_capability_conf` (`id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `model_id`, `support_online_infer`, `support_cache_infer`, `support_offpeak_infer`, `support_batch_infer`, `support_mcp_online_exploration`, `support_online_exploration`, `support_deployment`, `api_model_thinking`, `api_search`, `api_function_call`, `api_mcp`, `online_status`) VALUES (329, '2026-04-09 11:49:01', '2026-04-09 11:49:01', 'ops-10005832', 'ops-10005832', 0, '8c7cf2c250494de78320980e668109cd', '5dc393dd2dfb4b18bec970fd356ed07e', 1, 0, 0, 0, 0, 0, 0, '{\"status\": 0, \"exploration\": false, \"thinkingType\": 0}', '{\"status\": 0, \"exploration\": false}', '{\"status\": 0, \"exploration\": false}', '{\"status\": 0, \"exploration\": false}', 1);

-- model_display_config

DROP TABLE IF EXISTS model_display_config;
CREATE TABLE `model_display_config` (
                                        `id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                        `model_id` varchar(256) NOT NULL COMMENT '模型id',
                                        `support_maas_display` tinyint(1) NOT NULL COMMENT '是否支持Maas平台展示',
                                        `create_by` varchar(256) NOT NULL DEFAULT '' COMMENT '创建人',
                                        `update_by` varchar(256) NOT NULL DEFAULT '' COMMENT '修改人',
                                        `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除, 0:未删除，1:已删除',
                                        `status` varchar(20) DEFAULT 'WAIT_ONLINE' COMMENT '状态字段',
                                        `allow_history_user` tinyint(1) NOT NULL COMMENT '是否开启历史用户可见',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        KEY `model_id` (`model_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=487349 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='模型展示配置表';

INSERT INTO `ctyun_maas_dev`.`model_display_config` (`id`, `model_id`, `support_maas_display`, `create_by`, `update_by`, `create_date`, `update_date`, `deleted`, `status`, `allow_history_user`) VALUES (487348, '5dc393dd2dfb4b18bec970fd356ed07e', 1, 'ops-10005832', 'ops-10005832', '2026-04-09 11:49:00', '2026-04-09 11:49:00', 0, 'ONLINE', 0);
INSERT INTO `ctyun_maas_dev`.`model_display_config` (`id`, `model_id`, `support_maas_display`, `create_by`, `update_by`, `create_date`, `update_date`, `deleted`, `status`, `allow_history_user`) VALUES (487347, 'ed08e50690a740fc9fcc0ddd3b0abdb5', 1, 'ops-10005832', 'ops-10005832', '2026-04-09 11:47:26', '2026-04-09 11:47:26', 0, 'ONLINE', 0);


-- ctyun_maas_dev.model_rate_limit_conf

DROP TABLE IF EXISTS model_rate_limit_conf;
CREATE TABLE `model_rate_limit_conf` (
                                         `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
                                         `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（自动填充）',
                                         `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间（自动更新）',
                                         `create_by` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人（用户ID/用户名）',
                                         `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新人（用户ID/用户名）',
                                         `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '软删除标识：0-未删除，1-已删除',
                                         `uuid` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '全局唯一业务标识（UUID/雪花ID）',
                                         `model_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                                         `per_user_rpm` bigint unsigned DEFAULT NULL COMMENT '单用户每分钟请求数（RPM）',
                                         `per_user_tpm` bigint unsigned DEFAULT NULL COMMENT '单用户每分钟Token数（TPM）',
                                         `per_user_concurrency` bigint unsigned DEFAULT NULL COMMENT '单用户并发数',
                                         `per_user_ipm` bigint unsigned DEFAULT NULL COMMENT '单用户每分钟推理数（IPM）',
                                         `global_rpm` bigint unsigned DEFAULT NULL COMMENT '模型全局每分钟请求数（RPM）',
                                         `global_tpm` bigint unsigned DEFAULT NULL COMMENT '模型全局每分钟Token数（TPM）',
                                         `global_concurrency` bigint unsigned DEFAULT NULL COMMENT '模型全局并发数',
                                         `global_ipm` bigint unsigned DEFAULT NULL COMMENT '模型全局每分钟推理数（IPM）',
                                         `max_context_tokens` bigint unsigned DEFAULT NULL COMMENT '最大上下文Token数',
                                         `max_input_tokens` bigint unsigned DEFAULT NULL COMMENT '最大输入Token数',
                                         `max_output_tokens` bigint unsigned DEFAULT NULL COMMENT '最大输出Token数',
                                         `max_reasoning_tokens` bigint unsigned DEFAULT NULL COMMENT '最大推理Token数',
                                         `online_status` tinyint NOT NULL DEFAULT '0' COMMENT '上线状态：0-下线，1-上线，2-灰度，3-维护中',
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `uk_uuid` (`uuid`) COMMENT 'UUID唯一索引（防止重复）',
                                         KEY `idx_create_date` (`create_date`) COMMENT '创建时间索引（按时间筛选）',
                                         KEY `idx_online_status` (`online_status`) COMMENT '上线状态索引（按状态筛选）',
                                         KEY `idx_deleted` (`deleted`) COMMENT '软删除索引（优化查询效率）'
) ENGINE=InnoDB AUTO_INCREMENT=264 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模型限流配置表（RPM/TPM/并发/Token数限制）';

INSERT INTO `ctyun_maas_dev`.`model_rate_limit_conf` (`id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `model_id`, `per_user_rpm`, `per_user_tpm`, `per_user_concurrency`, `per_user_ipm`, `global_rpm`, `global_tpm`, `global_concurrency`, `global_ipm`, `max_context_tokens`, `max_input_tokens`, `max_output_tokens`, `max_reasoning_tokens`, `online_status`) VALUES (260, '2026-04-09 11:47:11', '2026-04-09 16:31:05', 'ops-10005832', 'ops-10005832', 0, '9cf3507d1a2640d4ae1e629fd58447b3', 'ed08e50690a740fc9fcc0ddd3b0abdb5', 5, NULL, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `ctyun_maas_dev`.`model_rate_limit_conf` (`id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `model_id`, `per_user_rpm`, `per_user_tpm`, `per_user_concurrency`, `per_user_ipm`, `global_rpm`, `global_tpm`, `global_concurrency`, `global_ipm`, `max_context_tokens`, `max_input_tokens`, `max_output_tokens`, `max_reasoning_tokens`, `online_status`) VALUES (261, '2026-04-09 11:47:11', '2026-04-09 16:31:05', 'ops-10005832', 'ops-10005832', 0, '6490b8a9d10f420cab58e59c4a13e0fd', 'ed08e50690a740fc9fcc0ddd3b0abdb5', 5, NULL, 6, NULL, 5, NULL, 6, NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `ctyun_maas_dev`.`model_rate_limit_conf` (`id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `model_id`, `per_user_rpm`, `per_user_tpm`, `per_user_concurrency`, `per_user_ipm`, `global_rpm`, `global_tpm`, `global_concurrency`, `global_ipm`, `max_context_tokens`, `max_input_tokens`, `max_output_tokens`, `max_reasoning_tokens`, `online_status`) VALUES (262, '2026-04-09 11:48:42', '2026-04-09 11:49:00', 'ops-10005832', 'ops-10005832', 0, '6742dd50994e4d589f6f1b6fa96c2c23', '5dc393dd2dfb4b18bec970fd356ed07e', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `ctyun_maas_dev`.`model_rate_limit_conf` (`id`, `create_date`, `update_date`, `create_by`, `update_by`, `deleted`, `uuid`, `model_id`, `per_user_rpm`, `per_user_tpm`, `per_user_concurrency`, `per_user_ipm`, `global_rpm`, `global_tpm`, `global_concurrency`, `global_ipm`, `max_context_tokens`, `max_input_tokens`, `max_output_tokens`, `max_reasoning_tokens`, `online_status`) VALUES (263, '2026-04-09 11:49:01', '2026-04-09 11:52:45', 'ops-10005832', 'ops-10005832', 0, 'e2ece3eb2be14c9abbd9228c71c34233', '5dc393dd2dfb4b18bec970fd356ed07e', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1);

-- ctyun_maas_dev.model_square_context

DROP TABLE IF EXISTS model_square_context;
CREATE TABLE `model_square_context` (
                                        `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                        `title` varchar(64) NOT NULL DEFAULT '' COMMENT '模型标题',
                                        `model_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                                        `free_quota` bigint DEFAULT NULL COMMENT '免费额度',
                                        `related_product` varchar(256) DEFAULT NULL COMMENT '关联销售品',
                                        `related_products` json DEFAULT NULL,
                                        `free_charge` tinyint(1) DEFAULT NULL COMMENT '是否免费0否1是',
                                        `billing_mode` varchar(256) DEFAULT NULL COMMENT 'TOKENS:按tokens     PER_USE：按次数',
                                        `rpm` bigint DEFAULT NULL COMMENT '每分钟请求数',
                                        `tpm` bigint DEFAULT NULL COMMENT '每分钟Tokens数',
                                        `ipm` bigint DEFAULT NULL COMMENT '每分钟图象数',
                                        `global_rpm` bigint DEFAULT NULL,
                                        `global_tpm` bigint DEFAULT NULL,
                                        `global_ipm` bigint DEFAULT NULL,
                                        `online_status` tinyint(1) DEFAULT '0' COMMENT '模型是否上线0否1是',
                                        `max_concurrency` bigint DEFAULT NULL COMMENT '并发数',
                                        `global_max_concurrency` bigint DEFAULT NULL,
                                        `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                        `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除0否1是',
                                        `private_user_ids` varchar(8192) DEFAULT NULL COMMENT '专属用户列表',
                                        `private_network_access_token` varchar(256) NOT NULL DEFAULT '' COMMENT '专线访问方式的访问token',
                                        `api_network_access_type` varchar(256) NOT NULL DEFAULT '' COMMENT 'PUBLIC=公网,PRIVATE=专线, 支持多种访问方式使用英文逗号分隔',
                                        `effective_week` int NOT NULL DEFAULT '2' COMMENT '免费期限',
                                        PRIMARY KEY (`id`),
                                        KEY `idx_model_id_is_online` (`model_id`,`online_status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1830848301496239993 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ;

INSERT INTO `ctyun_maas_dev`.`model_square_context` (`id`, `title`, `model_id`, `free_quota`, `related_product`, `related_products`, `free_charge`, `billing_mode`, `rpm`, `tpm`, `ipm`, `global_rpm`, `global_tpm`, `global_ipm`, `online_status`, `max_concurrency`, `global_max_concurrency`, `create_date`, `update_date`, `deleted`, `private_user_ids`, `private_network_access_token`, `api_network_access_type`, `effective_week`) VALUES (1830848301496239991, '【视频生成】有声-Mock', '5dc393dd2dfb4b18bec970fd356ed07e', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2026-04-09 11:48:42', '2026-04-09 11:49:00', 0, '', '10296155e5db69fd8996313a4d2c9793', '', 2);
INSERT INTO `ctyun_maas_dev`.`model_square_context` (`id`, `title`, `model_id`, `free_quota`, `related_product`, `related_products`, `free_charge`, `billing_mode`, `rpm`, `tpm`, `ipm`, `global_rpm`, `global_tpm`, `global_ipm`, `online_status`, `max_concurrency`, `global_max_concurrency`, `create_date`, `update_date`, `deleted`, `private_user_ids`, `private_network_access_token`, `api_network_access_type`, `effective_week`) VALUES (1830848301496239992, '【视频生成】有声-Mock', '5dc393dd2dfb4b18bec970fd356ed07e', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2026-04-09 11:49:01', '2026-04-09 11:49:01', 0, '', '10296155e5db69fd8996313a4d2c9793', '', 2);
INSERT INTO `ctyun_maas_dev`.`model_square_context` (`id`, `title`, `model_id`, `free_quota`, `related_product`, `related_products`, `free_charge`, `billing_mode`, `rpm`, `tpm`, `ipm`, `global_rpm`, `global_tpm`, `global_ipm`, `online_status`, `max_concurrency`, `global_max_concurrency`, `create_date`, `update_date`, `deleted`, `private_user_ids`, `private_network_access_token`, `api_network_access_type`, `effective_week`) VALUES (1830848301496239989, '【视频生成】无声-Mock', 'ed08e50690a740fc9fcc0ddd3b0abdb5', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2026-04-09 11:47:11', '2026-04-09 16:31:05', 0, '', 'c309ca40bbc567139fbd766cac40e0d5', '', 2);
INSERT INTO `ctyun_maas_dev`.`model_square_context` (`id`, `title`, `model_id`, `free_quota`, `related_product`, `related_products`, `free_charge`, `billing_mode`, `rpm`, `tpm`, `ipm`, `global_rpm`, `global_tpm`, `global_ipm`, `online_status`, `max_concurrency`, `global_max_concurrency`, `create_date`, `update_date`, `deleted`, `private_user_ids`, `private_network_access_token`, `api_network_access_type`, `effective_week`) VALUES (1830848301496239990, '【视频生成】无声-Mock', 'ed08e50690a740fc9fcc0ddd3b0abdb5', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2026-04-09 11:47:11', '2026-04-09 16:31:05', 0, '', 'c309ca40bbc567139fbd766cac40e0d5', '', 2);

-- ctyun_maas_dev.programming_plan (目前用不到)

DROP TABLE IF EXISTS programming_plan;
CREATE TABLE `programming_plan` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                    `plan_code` varchar(64) NOT NULL COMMENT '套餐编码（唯一）',
                                    `it_type` varchar(64) NOT NULL COMMENT '销售品名称',
                                    `it_specs` varchar(256) DEFAULT NULL,
                                    `plan_name` varchar(128) NOT NULL COMMENT '套餐名称',
                                    `charge_mode` varchar(32) NOT NULL DEFAULT 'PAID' COMMENT '计费模式：FREE, PAID',
                                    `source_type` varchar(32) NOT NULL DEFAULT 'SELF' COMMENT '模型来源：SELF, ZHIPU',
                                    `cycle_type` varchar(32) NOT NULL DEFAULT 'MONTH' COMMENT '计费周期：MONTH, QUARTER, YEAR',
                                    `plan_tier_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联 plan_tier.id',
                                    `third_plan_code_id` varchar(64) NOT NULL COMMENT '关联三方套餐id',
                                    `valid_from` datetime DEFAULT NULL COMMENT '套餐可购买开始时间（NULL=立即生效）',
                                    `valid_to` datetime DEFAULT NULL COMMENT '套餐可购买结束时间（NULL=永不过期）',
                                    `price_json` json NOT NULL COMMENT '价格信息（单位：分），例如 {"current":9900,"original":12900,"renewal":8900}',
                                    `status` varchar(32) NOT NULL DEFAULT 'ENABLED' COMMENT '状态：ENABLED, DISABLED',
                                    `buy_status` varchar(32) NOT NULL DEFAULT 'AVAILABLE' COMMENT '购买状态：AVAILABLE, EXPIRED, OUT_OF_STOCK',
                                    `ext_info` json DEFAULT NULL COMMENT '业务规则配置，例如 {"quota_weekly":400, "models":["glm-4"]}',
                                    `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `create_by` varchar(256) DEFAULT NULL COMMENT '创建人',
                                    `update_by` varchar(256) DEFAULT NULL COMMENT '更新人',
                                    `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_plan_code` (`plan_code`),
                                    KEY `idx_status_valid_time_deleted` (`status`,`valid_from`,`valid_to`,`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='编程套餐核心信息表（大写枚举 + JSON价格）';


--  ctyun_maas_dev.programming_plan_support_model_info
DROP TABLE IF EXISTS programming_plan_support_model_info;
CREATE TABLE `programming_plan_support_model_info` (
                                                       `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                                       `tier_id` varchar(64) NOT NULL COMMENT '关联 programming_plan_tier.id',
                                                       `model_id` varchar(255) DEFAULT NULL COMMENT '模型Id',
                                                       `model_name` varchar(128) NOT NULL COMMENT '模型名称（如 glm-4, claude-3-opus）',
                                                       `invoke_api_model_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '调用API真实模型名称',
                                                       `model_intro` varchar(512) DEFAULT NULL COMMENT '模型介绍（如 GLM-4：支持多轮对话、代码生成等）',
                                                       `ext_config` json DEFAULT NULL COMMENT '模型专属配置，例如 {"max_tokens":8192, "qps_limit":10}',
                                                       `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                       `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                       `create_by` varchar(256) DEFAULT NULL COMMENT '创建人',
                                                       `update_by` varchar(256) DEFAULT NULL COMMENT '更新人',
                                                       `deleted` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
                                                       PRIMARY KEY (`id`),
                                                       KEY `idx_tier_deleted` (`tier_id`,`deleted`),
                                                       KEY `idx_invoke_api_model_name__deleted` (`model_name`,`deleted`) USING BTREE,
                                                       KEY `idx_model_id_deleted` (`model_id`,`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='套餐档位支持的模型详情表';

-- ctyun_maas_dev.user_plan_appkey

DROP TABLE IF EXISTS user_plan_appkey;
CREATE TABLE `user_plan_appkey` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                    `app_id` varchar(128) NOT NULL,
                                    `account_id` varchar(128) NOT NULL,
                                    `user_id` varchar(128) NOT NULL,
                                    `plan_code` varchar(64) DEFAULT NULL COMMENT '套餐编码（唯一）',
                                    `order_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                    `app_key` varchar(64) NOT NULL,
                                    `app_secret` varchar(128) NOT NULL,
                                    `valid_from` datetime DEFAULT NULL,
                                    `valid_to` datetime DEFAULT NULL,
                                    `third_party_appkey_id` bigint unsigned NOT NULL,
                                    `user_plan_appkey_type` varchar(32) NOT NULL DEFAULT 'SELF_PLATFORM' COMMENT '类型：SELF_PLATFORM:自己；THIRD_PARTY_LATFORM :第三方',
                                    `status` varchar(32) NOT NULL DEFAULT 'USED' COMMENT '状态：USED-使用；DISABLED-停用',
                                    `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    `create_by` varchar(256) DEFAULT NULL,
                                    `update_by` varchar(256) DEFAULT NULL,
                                    `deleted` varchar(32) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
                                    PRIMARY KEY (`id`),
                                    KEY `idx_create_date` (`create_date`),
                                    KEY `idx_user_id_status` (`user_id`,`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户套餐专属AppKey管理表';

-- ----------------------------
-- Table structure for api_log
-- ----------------------------
DROP TABLE IF EXISTS `api_log`;
CREATE TABLE `api_log` (
                           `id` varchar(255) NOT NULL,
                           `user_id` varchar(255) DEFAULT NULL,
                           `model_name` varchar(255) DEFAULT NULL,
                           `model_id` varchar(255) DEFAULT NULL,
                           `type` varchar(255) DEFAULT NULL,
                           `response_time` int(11) DEFAULT '0',
                           `address` varchar(255) DEFAULT NULL,
                           `abnormal` varchar(255) DEFAULT NULL,
                           `token_num` int(11) DEFAULT '0',
                           `input_tokens` int(11) DEFAULT '0',
                           `output_tokens` int(11) DEFAULT '0',
                           `flow_up` double DEFAULT '0',
                           `flow_down` double DEFAULT '0',
                           `created_at` datetime DEFAULT NULL,
                           `updated_at` datetime DEFAULT NULL,
                           `deleted_at` datetime DEFAULT NULL,
                           `app_id` varchar(255) DEFAULT NULL,
                           `public_model_id` varchar(255) DEFAULT NULL,
                           `status_detail` varchar(255) DEFAULT NULL,
                           `status` varchar(255) DEFAULT NULL,
                           `output_image_num` int(11) DEFAULT '0' COMMENT '输出图片数量',
                           `ctyun_acct_id` varchar(255) DEFAULT '' COMMENT '账号ID',
                           `order_id` varchar(255) DEFAULT '' COMMENT '账单ID',
                           `features` varchar(65533) DEFAULT NULL COMMENT '扩展字段(json结构)',
                           `trace_id` varchar(255) DEFAULT NULL COMMENT '链路跟踪ID',
                           `source_from` varchar(255) DEFAULT NULL COMMENT '来源(体验中心:EXP_CENTER;API调用:API)',
                           `stream_req` tinyint(4) DEFAULT NULL COMMENT '1:流式请求;0:非流式请求',
                           `first_token_time` int(11) DEFAULT NULL COMMENT '端侧首响 ms',
                           `time_per_token_time` int(11) DEFAULT NULL COMMENT '非首token时延 ms',
                           `talk_total_time` int(11) DEFAULT NULL COMMENT '整体对话耗时 ms 流式+非流式',
                           `algo_first_token_time` int(11) DEFAULT NULL COMMENT '算法服务首响 ms',
                           `input_audit_time` int(11) DEFAULT NULL COMMENT '输入安全审核耗时 ms',
                           `aspect_prepare_time` int(11) DEFAULT NULL COMMENT '前置数据处理耗时 ms',
                           `per_output_token_time` int(11) DEFAULT NULL COMMENT '非首token时延 ms',
                           `job_id` varchar(255) DEFAULT NULL COMMENT '子任务id',
                           `cache_tokens_num` int(11) DEFAULT NULL COMMENT '缓存命中tokens',
                           `business_type` tinyint(4) DEFAULT NULL COMMENT '业务类型: 1-编程套餐',
                           PRIMARY KEY (`id`),
                           KEY `idx_createdat_at` (`created_at`),
                           KEY `idx_user_id` (`user_id`),
                           KEY `idx_model_id` (`model_id`),
                           KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API调用日志表';

SET FOREIGN_KEY_CHECKS = 1;


-- =========================
-- 切换数据库：ctyun_task_modeling
-- =========================
USE ctyun_task_modeling;

-- ctyun_task_modeling.model_basic_info

DROP TABLE IF EXISTS model_basic_info;
CREATE TABLE `model_basic_info` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                    `title` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '模型标题',
                                    `description` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '模型产品简介',
                                    `cover_image` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '封面图地址',
                                    `frameworks` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT '[]',
                                    `support_framework` int DEFAULT '0',
                                    `support_online_service` tinyint DEFAULT '1',
                                    `model_id` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模型id',
                                    `model_status` tinyint(1) NOT NULL DEFAULT '2' COMMENT '模型状态 1: 上线 2: 下线 ',
                                    `model_order` int NOT NULL DEFAULT '0' COMMENT '排序值',
                                    `current_version` int NOT NULL DEFAULT '1' COMMENT '当前版本',
                                    `model_server_url` varchar(4096) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `preset_model_name` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                    `model_type` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '算法的模型名称类型',
                                    `source` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '来源',
                                    `introduction` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '介绍',
                                    `api_info` longtext COLLATE utf8mb4_unicode_ci COMMENT 'api信息',
                                    `create_by` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建人',
                                    `update_by` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '修改人',
                                    `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除, 0:未删除，1:已删除',
                                    `model_service_type` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PUBLIC_MODEL' COMMENT 'PUBLIC_MODEL=公开模型，PRIVATE_MODEL=私有模型',
                                    PRIMARY KEY (`id`),
                                    KEY `model_id` (`model_id`),
                                    KEY `model_status` (`model_status`),
                                    KEY `model_order` (`model_order`)
) ENGINE=InnoDB AUTO_INCREMENT=1998576117936894022 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='基础模型表';

INSERT INTO `ctyun_task_modeling`.`model_basic_info` (`id`, `title`, `description`, `cover_image`, `frameworks`, `support_framework`, `support_online_service`, `model_id`, `model_status`, `model_order`, `current_version`, `model_server_url`, `preset_model_name`, `model_type`, `source`, `introduction`, `api_info`, `create_by`, `update_by`, `create_date`, `update_date`, `deleted`, `model_service_type`) VALUES (1998576117936894021, '【视频生成】有声-Mock', 'https://wishub-test.ctyun.cn:30443/api/v1/services/aigc/video-generation/video-synthesis', 'MODEL_SQUARE_ICON1', '[]', 0, 1, '5dc393dd2dfb4b18bec970fd356ed07e', 1, 94, 1, NULL, NULL, '', 'PLATFORM', '<p>https://wishub-test.ctyun.cn:30443/api/v1/services/aigc/video-generation/video-synthesis</p>', '<p>https://wishub-test.ctyun.cn:30443/api/v1/services/aigc/video-generation/video-synthesis</p>', 'ops-10005832', 'ops-10005832', '2026-04-09 11:49:01', '2026-04-09 11:49:00', 0, 'PUBLIC_MODEL');
INSERT INTO `ctyun_task_modeling`.`model_basic_info` (`id`, `title`, `description`, `cover_image`, `frameworks`, `support_framework`, `support_online_service`, `model_id`, `model_status`, `model_order`, `current_version`, `model_server_url`, `preset_model_name`, `model_type`, `source`, `introduction`, `api_info`, `create_by`, `update_by`, `create_date`, `update_date`, `deleted`, `model_service_type`) VALUES (1998576117936894020, '【视频生成】无声-Mock', 'https://wishub-test.ctyun.cn:30443/api/v1/services/aigc/video-generation/video-synthesis', 'MODEL_SQUARE_ICON1', '[]', 0, 1, 'ed08e50690a740fc9fcc0ddd3b0abdb5', 1, 93, 4, NULL, NULL, '', 'PLATFORM', '<p> <a href=\"https://wishub-test.ctyun.cn:30443/api/v1/services/aigc/video-generation/video-synthesis\">https://wishub-test.ctyun.cn:30443/api/v1/services/aigc/video-generation/video-synthesis</a> </p>', '<p> <a href=\"https://wishub-test.ctyun.cn:30443/api/v1/services/aigc/video-generation/video-synthesis\" target=\"\">https://wishub-test.ctyun.cn:30443/api/v1/services/aigc/video-generation/video-synthesis</a> </p>', '', 'ops-10005832', '2026-04-09 11:47:26', '2026-04-09 16:31:05', 0, 'PUBLIC_MODEL');

