package com.jws.transcomp.api.models.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.base.LiscenceType;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
public class EmployeeCreateDto {
    @NotNull(message = "Username is mandatory.")
    @Size(min = 4, max = 16)
    private String username;

    @NotNull(message = "Address is mandatory.")
    @Size(min = 4)
    private String address;

    @NotNull(message = "Salary is mandatory.")
    private BigDecimal salary;

    private Set<LiscenceType> licenses;
    @NotNull(message = "Role is mandatory.")
    @JsonProperty("role_id")
    private Long roleId;
    private Company company;
}
