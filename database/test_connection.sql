-- 测试数据库连接和数据脚本

-- 连接测试
SELECT '数据库连接成功！' as status, VERSION() as mysql_version;

-- 查看数据库
SHOW DATABASES LIKE 'login_demo';

-- 切换到目标数据库
USE login_demo;

-- 查看所有表
SHOW TABLES;

-- 查看用户表数据
SELECT '用户表数据:' as info;
SELECT id, username, email, status, created_at FROM users ORDER BY created_at DESC;

-- 查看角色表数据
SELECT '角色表数据:' as info;
SELECT id, role_name, role_code, status FROM roles ORDER BY id;

-- 查看权限表数据
SELECT '权限表数据:' as info;
SELECT id, permission_name, permission_code FROM permissions ORDER BY id;

-- 查看用户角色关联
SELECT '用户角色关联:' as info;
SELECT
    u.username,
    r.role_name,
    ur.assigned_at
FROM user_roles ur
JOIN users u ON ur.user_id = u.id
JOIN roles r ON ur.role_id = r.id
ORDER BY u.username;

-- 查看角色权限关联
SELECT '角色权限关联:' as info;
SELECT
    r.role_name,
    p.permission_name,
    rp.created_at
FROM role_permissions rp
JOIN roles r ON rp.role_id = r.id
JOIN permissions p ON rp.permission_id = p.id
ORDER BY r.role_name;

-- 查看系统配置
SELECT '系统配置:' as info;
SELECT config_key, config_value, description FROM system_config ORDER BY config_key;

-- 统计信息
SELECT '统计信息:' as info;
SELECT
    (SELECT COUNT(*) FROM users WHERE status = 1) as active_users,
    (SELECT COUNT(*) FROM roles WHERE status = 1) as active_roles,
    (SELECT COUNT(*) FROM permissions) as total_permissions,
    (SELECT COUNT(*) FROM user_roles) as user_role_assignments;
