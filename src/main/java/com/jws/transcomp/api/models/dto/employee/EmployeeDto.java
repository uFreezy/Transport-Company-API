package com.jws.transcomp.api.models.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.base.LiscenceType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
public class EmployeeDto implements Serializable {
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String address;

    @NotNull
    private BigDecimal salary;

    private Set<LiscenceType> licenses;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("company_id")
    private Long companyId;
}
