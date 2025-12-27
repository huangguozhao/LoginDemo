package com.victor.logindemo.repository;

import com.victor.logindemo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleCode(String roleCode);

    Set<Role> findByRoleCodeIn(Set<String> roleCodes);

    boolean existsByRoleCode(String roleCode);
}
