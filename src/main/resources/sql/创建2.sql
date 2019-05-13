CREATE TABLE `game_base_data` (
  `game_base_data_id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '自增',
  `match_id` bigint(64) NOT NULL COMMENT '比赛id',
  `league_name_simply` varchar(64) DEFAULT NULL COMMENT '联赛名称',
  `host_name` varchar(64) DEFAULT NULL COMMENT '主队',
  `guest_name` varchar(54) DEFAULT NULL COMMENT '客队',
  `match_time` datetime DEFAULT NULL COMMENT '比赛时间',
  `host_goal` int(4) DEFAULT NULL COMMENT '主队得分',
  `guest_goal` int(4) DEFAULT NULL COMMENT '客队得分',
  `game_result` int(4) DEFAULT NULL COMMENT '赛果，1主胜2平局3客胜',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`game_base_data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='比赛基本数据';