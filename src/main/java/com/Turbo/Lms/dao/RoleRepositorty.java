package com.Turbo.Lms.dao;

import com.Turbo.Lms.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepositorty extends JpaRepository<Role,Long> {
    Optional<Role> findRoleByName(String name);
}
