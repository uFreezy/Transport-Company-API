package com.jws.transcomp.api.models.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.base.LiscenceType;
import com.jws.transcomp.api.models.dto.role.RoleDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class EmployeeDto implements Serializable {
    private Long id;
    @NotNull
    private String username;
    private String password;
    @NotNull
    private String address;
    @NotNull
    private BigDecimal salary;
    private Set<LiscenceType> licenses;
    private RoleDto role;

    @JsonProperty("company_id")
    private Long companyId;

    public EmployeeDto() {
    }
}
