package com.victor.logindemo.config;

import com.victor.logindemo.entity.Permission;
import com.victor.logindemo.entity.Role;
import com.victor.logindemo.entity.User;
import com.victor.logindemo.repository.PermissionRepository;
import com.victor.logindemo.repository.RoleRepository;
import com.victor.logindemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 创建权限
        createPermissions();

        // 创建角色
        createRoles();

        // 创建测试用户
        createTestUsers();
    }

    private void createPermissions() {
        createPermissionIfNotExists("USER_MANAGE", "用户管理", "USER", "MANAGE", "管理用户账户");
        createPermissionIfNotExists("ROLE_MANAGE", "角色管理", "ROLE", "MANAGE", "管理角色和权限");
        createPermissionIfNotExists("SESSION_MANAGE", "会话管理", "SESSION", "MANAGE", "管理用户会话");
        createPermissionIfNotExists("SYSTEM_CONFIG", "系统配置", "SYSTEM", "CONFIG", "系统配置管理");
        createPermissionIfNotExists("VIEW_STATS", "查看统计", "STATS", "VIEW", "查看系统统计信息");
    }

    private void createPermissionIfNotExists(String code, String name, String resourceType, String action, String description) {
        if (!permissionRepository.existsByPermissionCode(code)) {
            Permission permission = new Permission();
            permission.setPermissionCode(code);
            permission.setPermissionName(name);
            permission.setResourceType(resourceType);
            permission.setAction(action);
            permission.setDescription(description);
            permissionRepository.save(permission);
            System.out.println("创建了权限: " + name);
        }
    }

    private void createRoles() {
        // 创建超级管理员角色
        Role superAdminRole = createRoleIfNotExists("SUPER_ADMIN", "超级管理员", "系统超级管理员，拥有所有权限");
        if (superAdminRole != null) {
            assignAllPermissionsToRole(superAdminRole);
        }

        // 创建管理员角色
        Role adminRole = createRoleIfNotExists("ADMIN", "管理员", "系统管理员");
        if (adminRole != null) {
            assignPermissionsToRole(adminRole, "USER_MANAGE", "SESSION_MANAGE", "VIEW_STATS");
        }

        // 创建普通用户角色
        createRoleIfNotExists("USER", "普通用户", "普通注册用户");
    }

    private Role createRoleIfNotExists(String code, String name, String description) {
        if (!roleRepository.existsByRoleCode(code)) {
            Role role = new Role();
            role.setRoleCode(code);
            role.setRoleName(name);
            role.setDescription(description);
            role.setStatus(1);
            Role savedRole = roleRepository.save(role);
            System.out.println("创建了角色: " + name);
            return savedRole;
        }
        return roleRepository.findByRoleCode(code).orElse(null);
    }

    private void assignAllPermissionsToRole(Role role) {
        Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());
        role.setPermissions(allPermissions);
        roleRepository.save(role);
    }

    private void assignPermissionsToRole(Role role, String... permissionCodes) {
        Set<Permission> permissions = permissionRepository.findByPermissionCodeIn(Set.of(permissionCodes));
        role.setPermissions(permissions);
        roleRepository.save(role);
    }

    private void createTestUsers() {
        // 创建超级管理员用户
        if (userRepository.findByUsername("superadmin").isEmpty()) {
            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("super123"));
            superAdmin.setEmail("superadmin@example.com");
            superAdmin.setStatus(1);

            Role superAdminRole = roleRepository.findByRoleCode("SUPER_ADMIN").orElse(null);
            if (superAdminRole != null) {
                superAdmin.setRoles(Set.of(superAdminRole));
            }

            userRepository.save(superAdmin);
            System.out.println("创建了超级管理员用户: superadmin/super123");
        }

        // 创建管理员用户
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setStatus(1);

            Role adminRole = roleRepository.findByRoleCode("ADMIN").orElse(null);
            if (adminRole != null) {
                admin.setRoles(Set.of(adminRole));
            }

            userRepository.save(admin);
            System.out.println("创建了管理员用户: admin/admin123");
        }

        // 创建普通用户
        if (userRepository.findByUsername("testuser").isEmpty()) {
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setPassword(passwordEncoder.encode("password123"));
            testUser.setEmail("test@example.com");
            testUser.setStatus(1);

            Role userRole = roleRepository.findByRoleCode("USER").orElse(null);
            if (userRole != null) {
                testUser.setRoles(Set.of(userRole));
            }

            userRepository.save(testUser);
            System.out.println("创建了测试用户: testuser/password123");
        }
    }
}
