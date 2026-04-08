-- 创建数据库
CREATE DATABASE IF NOT EXISTS finance_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE finance_db;

-- 用户表
CREATE TABLE `user` (
                        `id` bigint PRIMARY KEY AUTO_INCREMENT,
                        `username` varchar(50) NOT NULL UNIQUE,
                        `password` varchar(255) NOT NULL,
                        `email` varchar(100),
                        `phone` varchar(20),
                        `avatar` varchar(255),
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP
);

-- 分类表
CREATE TABLE `category` (
                            `id` bigint PRIMARY KEY AUTO_INCREMENT,
                            `user_id` bigint NOT NULL,
                            `name` varchar(50) NOT NULL,
                            `type` tinyint NOT NULL COMMENT '0=支出 1=收入',
                            `parent_id` bigint DEFAULT 0,
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

-- 收支记录表
CREATE TABLE `transaction` (
                               `id` bigint PRIMARY KEY AUTO_INCREMENT,
                               `user_id` bigint NOT NULL,
                               `category_id` bigint NOT NULL,
                               `amount` decimal(12,2) NOT NULL,
                               `type` tinyint NOT NULL COMMENT '0=支出 1=收入',
                               `transaction_date` date NOT NULL,
                               `note` varchar(255),
                               `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
                               FOREIGN KEY (`category_id`) REFERENCES `category`(`id`)
);

-- 预算表
CREATE TABLE `budget` (
                          `id` bigint PRIMARY KEY AUTO_INCREMENT,
                          `user_id` bigint NOT NULL,
                          `category_id` bigint DEFAULT NULL COMMENT 'NULL表示总预算',
                          `month` varchar(7) NOT NULL COMMENT '格式 YYYY-MM',
                          `amount` decimal(12,2) NOT NULL,
                          `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
                          FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE CASCADE
);

-- 储蓄目标表
CREATE TABLE `saving_goal` (
                               `id` bigint PRIMARY KEY AUTO_INCREMENT,
                               `user_id` bigint NOT NULL,
                               `goal_name` varchar(100) NOT NULL,
                               `target_amount` decimal(12,2) NOT NULL,
                               `saved_amount` decimal(12,2) DEFAULT 0,
                               `deadline` date NOT NULL,
                               `status` tinyint DEFAULT 0 COMMENT '0=进行中 1=已完成',
                               `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

-- 索引优化
CREATE INDEX idx_trans_user_date ON `transaction`(user_id, transaction_date);
CREATE INDEX idx_trans_user_type ON `transaction`(user_id, type);
CREATE INDEX idx_budget_user_month ON `budget`(user_id, month);