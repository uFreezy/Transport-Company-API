package com.jws.transcomp.api.models.dto;

import com.jws.transcomp.api.models.Role;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RoleInfoDto {
    @NotNull
    private Long id;
    private String name;

    public RoleInfoDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role toRoleEntity() {
        Role role = new Role();
        role.setId(this.id);
        role.setName(this.name);

        return role;
    }
}
