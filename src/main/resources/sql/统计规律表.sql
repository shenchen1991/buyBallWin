CREATE TABLE `big_small_modulus` (
  `big_small_data_id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `league_name_simply` varchar(64) DEFAULT NULL COMMENT '联赛名称',
  `host_get` decimal(8,2) DEFAULT NULL COMMENT '主队进球指数',
  `host_lost` decimal(8,2) DEFAULT NULL COMMENT '主队失球指数',
  `guest_get` decimal(8,2) DEFAULT NULL COMMENT '客队进球指数',
  `guest_lost` decimal(8,2) DEFAULT NULL COMMENT '客队失球指数',
	`reverse` int(8) DEFAULT NULL COMMENT '正买=1，反买=2',
	`total_count` int(8) DEFAULT NULL COMMENT '历史总场次',
	`win_count` int(8) DEFAULT NULL COMMENT '指数胜场次',
	`lost_count` int(8) DEFAULT NULL COMMENT '指数亏场次',
	`buy_count` int(8) DEFAULT NULL COMMENT '指数购买',
  `sum` decimal(8,2) DEFAULT NULL COMMENT '购买一元盈利数',
  `rate` decimal(8,2) DEFAULT NULL COMMENT '收益率',
  `win_rate` decimal(8,2) DEFAULT NULL COMMENT '胜率',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`big_small_data_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='大小球判断系数';