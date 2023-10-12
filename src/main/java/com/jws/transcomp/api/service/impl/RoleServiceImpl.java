package com.jws.transcomp.api.service.impl;

import com.jws.transcomp.api.models.Role;
import com.jws.transcomp.api.repository.RoleRepository;
import com.jws.transcomp.api.service.base.RoleService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public boolean any() {
        return this.roleRepository.count() > 0;
    }

    @Override
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("No role exists with name: " + roleName));
    }

    @Override
    public Role findById(Long id) {
        return this.roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invalid role id."));
    }

    @Override
    public List<Role> getAll() {
        return this.roleRepository.findAll();
    }
}
