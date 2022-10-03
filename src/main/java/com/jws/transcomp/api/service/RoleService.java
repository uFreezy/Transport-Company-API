package com.jws.transcomp.api.service;


import com.jws.transcomp.api.models.Role;

import java.util.List;

public interface RoleService {
    void save(Role role);

    boolean any();

    Role findByName(String roleName);

    Role findById(Long id);

    List<Role> getAll();
}
