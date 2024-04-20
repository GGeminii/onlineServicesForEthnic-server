create database onlineServicesForEthnic;
use onlineServicesForEthnic;

drop table if exists person;
CREATE TABLE IF NOT EXISTS `person` (
                                        `id` BIGINT primary key auto_increment COMMENT '人员的唯一标识',
                                        `username` VARCHAR(255) NOT NULL unique COMMENT '人员的登录名称',
    `ethnic`  varchar(255) not null comment '民族',
    `avatar` varchar(255) not null comment '头像',
    `real_name` varchar(255) not null comment '真实姓名',
    `gender` int default 0 comment '性别：1.男;2.女；0:未知',
    `role` varchar(225) not null comment '角色:admin；user',
    `work_at` varchar(225) comment '学校/行业',
    `student_id` varchar(225) comment '学号',
    `state` int default 1 COMMENT '1.正常；2.锁定',
    `email` varchar(20) unique COMMENT '用户邮箱',
    `phone_num` varchar(20) unique COMMENT '用户的手机号码',
    `password` VARCHAR(255) NOT NULL COMMENT '用户的登录密码',
    `is_delete` int default 0 COMMENT '1：已删除 ； 0：未删除',
    `created_at` DATETIME NOT NULL comment '创建日期',
    `updated_at` DATETIME NOT NULL comment '更新日期'
    ) ENGINE=InnoDB,comment '用户表';