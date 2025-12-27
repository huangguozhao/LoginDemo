# Session-Based认证登录系统

基于Spring Boot开发的Session认证登录系统，支持完整的用户管理和权限控制。

## 技术栈

- **后端框架**: Spring Boot 3.5.9
- **数据库**: MySQL 8.0+
- **安全框架**: Spring Security
- **模板引擎**: Thymeleaf
- **构建工具**: Maven
- **Java版本**: 17+

## 功能特性

### 用户认证
- ✅ 用户注册和登录
- ✅ Session管理
- ✅ 密码加密存储
- ✅ 登录失败次数限制
- ✅ 账户锁定机制

### 用户管理
- ✅ 用户信息管理
- ✅ 用户状态控制
- ✅ 邮箱和手机号验证支持

### 权限管理
- ✅ 基于角色的访问控制 (RBAC)
- ✅ 灵活的权限配置
- ✅ 用户角色分配

### 会话管理
- ✅ Session生命周期管理
- ✅ 并发会话控制
- ✅ 会话过期处理

### 安全特性
- ✅ CSRF防护
- ✅ SQL注入防护
- ✅ XSS防护
- ✅ 密码重置功能
- ✅ 登录历史记录

## 快速开始

### 1. 环境准备

确保已安装以下环境：
- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 2. 数据库设置

1. **创建数据库和用户**
   ```bash
   # 使用root用户登录MySQL
   mysql -u root -p

   # 执行数据库创建脚本
   source database/create_user.sql
   ```

2. **初始化数据库表**
   ```bash
   # 执行数据库初始化脚本
   source database/init.sql
   ```

### 3. 应用配置

编辑 `src/main/resources/application.yaml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/login_demo?useSSL=false&serverTimezone=Asia/Shanghai
    username: session_auth_user
    password: session_auth_pass123
```

### 4. 运行应用

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动。

## 默认用户

系统会自动创建以下测试用户：

| 用户名 | 密码 | 角色 | 权限 |
|--------|------|------|------|
| superadmin | super123 | 超级管理员 | 所有权限 |
| admin | admin123 | 管理员 | 用户管理、会话管理、查看统计 |
| testuser | password123 | 普通用户 | 查看统计 |

## 项目结构

```
src/main/java/com/victor/logindemo/
├── config/                 # 配置类
│   ├── DataInitializer.java    # 数据初始化
│   └── SecurityConfig.java     # 安全配置
├── controller/             # 控制器
│   ├── AuthController.java     # 认证控制器
│   └── DashboardController.java # 仪表板控制器
├── dto/                    # 数据传输对象
│   ├── LoginRequest.java       # 登录请求
│   ├── LoginResponse.java      # 登录响应
│   └── RegisterRequest.java    # 注册请求
├── entity/                 # 实体类
│   ├── User.java               # 用户实体
│   ├── Role.java               # 角色实体
│   └── Permission.java         # 权限实体
├── repository/             # 数据访问层
│   ├── UserRepository.java     # 用户仓库
│   ├── RoleRepository.java     # 角色仓库
│   └── PermissionRepository.java # 权限仓库
└── service/                # 业务逻辑层
    ├── AuthService.java        # 认证服务
    └── CustomUserDetailsService.java # 用户详情服务
```

## 数据库设计

### 核心表
- `users` - 用户表
- `roles` - 角色表
- `permissions` - 权限表
- `user_roles` - 用户角色关联
- `role_permissions` - 角色权限关联

### 会话和安全表
- `user_sessions` - 用户会话表
- `user_login_history` - 登录历史表
- `password_reset_tokens` - 密码重置令牌表

### 配置表
- `system_config` - 系统配置表

详细的数据库设计请参考 `database/README.md`。

## API接口

### 认证接口
- `GET /auth/login` - 登录页面
- `POST /auth/login` - 执行登录
- `GET /auth/register` - 注册页面
- `POST /auth/register` - 执行注册
- `POST /auth/logout` - 退出登录

### 应用接口
- `GET /` - 首页（重定向到登录或仪表板）
- `GET /dashboard` - 用户仪表板

## 安全配置

### 会话配置
- 会话超时时间：30分钟
- 最大并发会话数：1个用户1个会话
- 新登录会使旧会话失效

### 密码策略
- 最小长度：6位
- 使用BCrypt加密

### 账户安全
- 最大登录失败次数：5次
- 账户锁定时间：15分钟

## 开发指南

### 添加新权限
1. 在数据库中添加权限记录
2. 为相应角色分配权限
3. 在代码中使用 `@PreAuthorize` 注解控制访问

### 添加新角色
1. 在数据库中创建角色
2. 分配适当的权限
3. 为用户分配角色

### 自定义认证逻辑
修改 `AuthService` 和 `CustomUserDetailsService` 类。

## 部署说明

### 生产环境配置
1. 修改数据库密码
2. 启用SSL连接
3. 配置防火墙
4. 设置日志级别
5. 配置备份策略

### Docker部署
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 许可证

本项目采用 MIT 许可证。

## 贡献

欢迎提交 Issue 和 Pull Request！

## 联系方式

如有问题，请通过以下方式联系：
- 邮箱：your-email@example.com
- 项目地址：https://github.com/your-username/session-auth-demo
