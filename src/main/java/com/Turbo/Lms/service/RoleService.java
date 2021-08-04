package com.Turbo.Lms.service;

import com.Turbo.Lms.dao.RoleRepositorty;
import com.Turbo.Lms.domain.Role;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService {
private final RoleRepositorty roleRepositorty;

    public RoleService(RoleRepositorty roleRepositorty) {
        this.roleRepositorty = roleRepositorty;
    }

    public List<Role> findAll() {
        return roleRepositorty.findAll();
    }
}
