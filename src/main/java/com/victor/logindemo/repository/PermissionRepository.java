package com.victor.logindemo.repository;

import com.victor.logindemo.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionCode(String permissionCode);

    Set<Permission> findByPermissionCodeIn(Set<String> permissionCodes);

    boolean existsByPermissionCode(String permissionCode);
}
