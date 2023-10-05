package com.jws.transcomp.api.models.dto.role;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class RoleDto implements Serializable {
    private final Long id;
    @NotNull
    private final String name;
}
