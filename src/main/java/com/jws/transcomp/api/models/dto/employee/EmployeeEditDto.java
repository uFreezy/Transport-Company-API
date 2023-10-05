package com.jws.transcomp.api.models.dto.employee;

import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Role;
import com.jws.transcomp.api.models.base.LiscenceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEditDto {
    @NotNull(message = "Employee ID is mandatory")
    private Long id;
    @Size(min = 4, max = 16)
    private String username;
    @Size(min = 4)
    private String address;
    private BigDecimal salary;
    private Set<LiscenceType> licenses;
    private Role role;


    public void mapToEntity(Employee emp) {
        emp.setUsername((this.username != null) ? this.username : emp.getUsername());
        emp.setAddress((this.address != null) ? this.address : emp.getAddress());
        emp.setSalary((this.salary != null) ? this.salary : emp.getSalary());
        emp.setLicenses((this.licenses != null) ? this.licenses : emp.getLicenses());
        emp.setRole((this.role != null) ? this.role : emp.getRole());
    }
}
