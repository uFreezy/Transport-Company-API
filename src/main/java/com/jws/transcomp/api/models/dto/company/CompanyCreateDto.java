package com.jws.transcomp.api.models.dto.company;

import com.jws.transcomp.api.models.Company;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CompanyCreateDto {
    @NotNull
    @Size(min = 3, max = 10)
    private String name;

    public Company toEntityObject() {
        return new Company(this.name);
    }
}
