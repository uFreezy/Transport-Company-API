package com.jws.transcomp.api.models.dto.employee;

import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Role;
import com.jws.transcomp.api.models.base.LiscenceType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

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

    public EmployeeEditDto() {
    }

    public EmployeeEditDto(Long id, String username, String address, BigDecimal salary, Set<LiscenceType> licenses, Role role) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.salary = salary;
        this.licenses = licenses;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Set<LiscenceType> getLicenses() {
        return licenses;
    }

    public void setLicenses(Set<LiscenceType> licenses) {
        this.licenses = licenses;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void mapToEntity(Employee emp) {
        emp.setUsername((this.username != null) ? this.username : emp.getUsername());
        emp.setAddress((this.address != null) ? this.address : emp.getAddress());
        emp.setSalary((this.salary != null) ? this.salary : emp.getSalary());
        emp.setLicenses((this.licenses != null) ? this.licenses : emp.getLicenses());
        emp.setRole((this.role != null) ? this.role : emp.getRole());
    }
}
