package com.victor.logindemo.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        // 根据用户名设置角色和权限
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        if ("admin".equals(username)) {
            roles.add("admin");
            roles.add("user");
            permissions.add("user:read");
            permissions.add("user:write");
            permissions.add("admin:manage");
        } else if ("user".equals(username)) {
            roles.add("user");
            permissions.add("user:read");
        }

        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        // 模拟数据库查询用户
        if ("admin".equals(username)) {
            return new SimpleAuthenticationInfo(username, "admin123", getName());
        } else if ("user".equals(username)) {
            return new SimpleAuthenticationInfo(username, "user123", getName());
        }

        throw new UnknownAccountException("用户不存在");
    }
}
