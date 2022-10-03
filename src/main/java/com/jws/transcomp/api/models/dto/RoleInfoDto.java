package com.jws.transcomp.api.models.dto;

import com.jws.transcomp.api.models.Role;

import javax.validation.constraints.NotNull;

public class RoleInfoDto {
    @NotNull
    private Long id;
    private String name;

    public RoleInfoDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role toRoleEntity() {
        Role role = new Role();
        role.setId(this.id);
        role.setName(this.name);

        return role;
    }
}
