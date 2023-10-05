package com.jws.transcomp.api.models.dto.company;

import com.jws.transcomp.api.models.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEditDto {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 3, max = 10)
    private String name;

    public Company toEntityObject() {
        Company company = new Company(this.name);
        company.setId(this.id);

        return company;
    }
}
