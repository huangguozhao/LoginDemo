# Session认证数据库设置指南

## 概述
本项目使用MySQL数据库来存储Session-Based认证系统的所有数据。

## 数据库信息
- **数据库名**: `login_demo`
- **数据库用户**: `session_auth_user`
- **密码**: `session_auth_pass123`
- **字符集**: `utf8mb4`

## 数据库表结构

### 核心表
1. **users** - 用户表
   - 存储用户基本信息、认证凭据等

2. **user_sessions** - 用户会话表
   - 跟踪所有活跃的用户会话
   - 支持会话管理功能

3. **user_login_history** - 用户登录历史表
   - 记录所有登录尝试（成功和失败）
   - 支持安全审计

### 权限管理表
4. **roles** - 角色表
   - 定义系统角色

5. **permissions** - 权限表
   - 定义系统权限

6. **user_roles** - 用户角色关联表
   - 用户和角色的多对多关系

7. **role_permissions** - 角色权限关联表
   - 角色和权限的多对多关系

### 其他功能表
8. **password_reset_tokens** - 密码重置令牌表
   - 支持密码重置功能

9. **system_config** - 系统配置表
   - 存储系统配置参数

## 安装步骤

### 1. 安装MySQL
确保MySQL服务器已安装并运行。

### 2. 创建数据库和用户
使用root用户登录MySQL，然后执行：
```sql
source database/create_user.sql
```

### 3. 初始化数据库表
```sql
source database/init.sql
```

### 4. 验证安装
```sql
USE login_demo;
SHOW TABLES;
SELECT COUNT(*) as user_count FROM users;
SELECT COUNT(*) as role_count FROM roles;
```

## 默认数据

### 预创建角色
- **SUPER_ADMIN**: 超级管理员，拥有所有权限
- **ADMIN**: 管理员，拥有用户管理和会话管理权限
- **USER**: 普通用户，拥有基本查看权限

### 系统配置
- 会话超时时间：30分钟
- 最大登录尝试次数：5次
- 账户锁定时间：15分钟
- 密码最小长度：6位

## 备份建议

### 定期备份
```bash
# 每日备份
mysqldump -u session_auth_user -psession_auth_pass123 login_demo > backup_$(date +%Y%m%d).sql

# 恢复备份
mysql -u session_auth_user -psession_auth_pass123 login_demo < backup_file.sql
```

### 备份策略
- 每日自动备份用户表和登录历史
- 每周完整备份
- 保留30天的备份文件

## 性能优化

### 索引说明
数据库中已创建必要的索引来优化查询性能：
- 用户名、邮箱的唯一索引
- 会话过期时间和状态的复合索引
- 登录历史的复合索引

### 监控建议
- 监控连接数和慢查询
- 定期清理过期会话和登录历史
- 监控表大小增长

## 安全注意事项

1. **生产环境配置**
   - 修改默认密码
   - 启用SSL连接
   - 配置防火墙规则

2. **数据脱敏**
   - 不要在日志中记录密码
   - 定期轮换数据库密码

3. **访问控制**
   - 遵循最小权限原则
   - 定期审计用户权限
