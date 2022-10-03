package com.jws.transcomp.api.models.dto.employee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jws.transcomp.api.models.base.LiscenceType;
import com.jws.transcomp.api.models.dto.company.CompanyDto;
import com.jws.transcomp.api.models.dto.role.RoleDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompany(CompanyDto company) {
        this.companyId = company.getId();
    }

}
