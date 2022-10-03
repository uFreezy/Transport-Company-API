package com.jws.transcomp.api.models.dto.company;

import com.jws.transcomp.api.models.Company;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CompanyCreateDto {
    @NotNull
    @Size(min = 3, max = 10)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company toEntityObject() {
        return new Company(this.name);
    }
}
