package com.jws.transcomp.api.models.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.base.LiscenceType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

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

    public EmployeeCreateDto(String username, String address, BigDecimal salary, Set<LiscenceType> licenses, Long roleId, Company company) {
        this.username = username;
        this.address = address;
        this.salary = salary;
        this.licenses = licenses;
        this.roleId = roleId;
        this.company = company;
    }

    public EmployeeCreateDto() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

}
