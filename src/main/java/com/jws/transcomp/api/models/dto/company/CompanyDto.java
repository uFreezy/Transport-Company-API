package com.jws.transcomp.api.models.dto.company;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CompanyDto implements Serializable {
    private Long id;
    @NotNull
    private String name;

    private int employeeCount;

    private int vehicleCount;

    private int clientCount;

    private BigDecimal revenue;

    public CompanyDto() {
        // noargs constructor
    }
}
