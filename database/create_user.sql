-- 创建数据库用户脚本
-- 注意：需要使用root用户或具有CREATE USER权限的用户执行

-- 创建数据库
CREATE DATABASE IF NOT EXISTS login_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建数据库用户
CREATE USER IF NOT EXISTS 'session_auth_user'@'localhost' IDENTIFIED BY 'session_auth_pass123';

-- 授予权限
GRANT ALL PRIVILEGES ON login_demo.* TO 'session_auth_user'@'localhost';

-- 刷新权限
FLUSH PRIVILEGES;

-- 显示创建结果
SELECT User, Host FROM mysql.user WHERE User = 'session_auth_user';
