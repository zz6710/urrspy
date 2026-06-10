-- kcloud_test.flow_busi_config definition

CREATE TABLE `flow_busi_config` (
  `server` varchar(64) NOT NULL COMMENT '服务',
  `process_key` varchar(64) DEFAULT '' COMMENT '流程标识',
  `bus_keys` varchar(64) DEFAULT '' COMMENT '业务主键',
  `status` int(1) DEFAULT '1' COMMENT '状态',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`server`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务流程表';


-- kcloud_test.flow_busi_info definition

CREATE TABLE `flow_busi_info` (
  `busi_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '业务审批表主键',
  `server` varchar(50) NOT NULL COMMENT '服务(微服务为服务名，http请求为ip:port)',
  `url` varchar(255) NOT NULL COMMENT '业务回调地址',
  `content_type` varchar(50) DEFAULT NULL COMMENT 'url数据类型',
  `keys` varchar(128) DEFAULT NULL COMMENT '业务唯一键，逗号隔开(不能存在同样的在途数据)',
  `values` varchar(50) DEFAULT NULL COMMENT '业务唯一值，逗号隔开',
  `process_key` varchar(50) DEFAULT NULL COMMENT '流程设计id',
  `process_definition_id` varchar(255) DEFAULT NULL COMMENT '流程定义id',
  `process_instance_id` varchar(50) DEFAULT NULL COMMENT '流程实例id',
  `process_status` varchar(2) DEFAULT NULL COMMENT '流程状态',
  `bus_status` char(1) DEFAULT NULL COMMENT '业务执行状态',
  `bus_return_msg` varchar(500) DEFAULT NULL COMMENT '业务执行结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `callback_num` int(11) DEFAULT '0' COMMENT '回调次数',
  `validate_id` varchar(50) DEFAULT NULL COMMENT '返回报文校验规则id',
  `label_info` varchar(2000) DEFAULT NULL COMMENT '表单字段显示json',
  `submit_data` text COMMENT '提交的参数',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者id,发起用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`busi_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1643812416252334083 DEFAULT CHARSET=utf8mb4 COMMENT='业务审批表';


-- kcloud_test.flow_copy definition

CREATE TABLE `flow_copy` (
  `copy_id` bigint(20) DEFAULT NULL COMMENT '抄送id',
  `task_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `task_def_key` varchar(64) DEFAULT NULL COMMENT '任务定义key',
  `proc_def_name` varchar(255) DEFAULT NULL COMMENT '流程定义名称',
  `proc_def_version` varchar(10) DEFAULT NULL COMMENT '流程定义版本',
  `proc_ins_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
  `proc_def_id` varchar(64) DEFAULT NULL COMMENT '流程定义id',
  `proc_key` varchar(64) DEFAULT NULL COMMENT '流程key',
  `task_id` varchar(64) DEFAULT NULL COMMENT '任务id',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `launch_copy_user_id` varchar(64) DEFAULT NULL COMMENT '发起抄送用户id',
  `read` varchar(1) DEFAULT NULL COMMENT '是否已阅',
  `create_by` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- kcloud_test.flow_env definition

CREATE TABLE `flow_env` (
  `env_id` bigint(20) NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '英文名',
  `display_name` varchar(100) DEFAULT NULL COMMENT '中文名',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`env_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程参数组';


-- kcloud_test.flow_env_item definition

CREATE TABLE `flow_env_item` (
  `env_id` bigint(20) DEFAULT NULL,
  `item_key` varchar(100) DEFAULT NULL COMMENT '参数名',
  `item_value` text COMMENT '参数值',
  `item_type` varchar(100) DEFAULT NULL COMMENT '参数类型',
  `env_item_id` bigint(20) NOT NULL,
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`env_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程参数项';


-- kcloud_test.flow_form_field definition

CREATE TABLE `flow_form_field` (
  `form_field_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表单字段id',
  `name` varchar(64) DEFAULT '' COMMENT '字段英文名称',
  `display_name` varchar(64) DEFAULT '' COMMENT '字段中文名称',
  `form_type` varchar(100) DEFAULT 'form' COMMENT '表单类型',
  `json` text COMMENT '表单配置',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`form_field_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1585535746940915714 DEFAULT CHARSET=utf8mb4 COMMENT='表单字段配置';


-- kcloud_test.flow_surrogate definition

CREATE TABLE `flow_surrogate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `process_key` varchar(255) DEFAULT NULL COMMENT '流程key',
  `process_name` varchar(255) DEFAULT NULL COMMENT '流程名',
  `start_date` date DEFAULT NULL COMMENT '代理开始日期',
  `end_date` date DEFAULT NULL COMMENT '代理结束日期',
  `creator` varchar(50) DEFAULT NULL COMMENT '授权人',
  `create_name` varchar(50) DEFAULT NULL COMMENT '授权人姓名',
  `surrogate` varchar(50) DEFAULT NULL COMMENT '代理人',
  `surrogate_name` varchar(50) DEFAULT NULL COMMENT '代理人名字',
  `status` varchar(1) DEFAULT '1' COMMENT '状态 1-启用  0-禁用',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `process_key` (`process_key`,`creator`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1578938482025881603 DEFAULT CHARSET=utf8mb4 COMMENT='任务代理';


-- kcloud_test.flow_validate_config definition

CREATE TABLE `flow_validate_config` (
  `id` varchar(32) CHARACTER SET utf8 NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '校验名称',
  `rule` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '校验规则',
  `remark` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回调返回参数校验配置表';
INSERT INTO flow_validate_config
(id, name, rule, remark)
VALUES('3', 'BaseServer', 'result.success', NULL);
