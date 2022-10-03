package com.jws.transcomp.api.models.dto.company;

import com.jws.transcomp.api.models.Company;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CompanyEditDto {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 3, max = 10)
    private String name;

    public CompanyEditDto() {
    }

    public CompanyEditDto(Long id, String name) {
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

    public Company toEntityObject() {
        Company company = new Company(this.name);
        company.setId(this.id);

        return company;
    }
}
