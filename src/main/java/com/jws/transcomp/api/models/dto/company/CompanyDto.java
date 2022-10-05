package com.jws.transcomp.api.models.dto.company;

import com.jws.transcomp.api.models.Company;
import lombok.Data;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CompanyDto implements Serializable {
    public static final Converter<Company, CompanyDto> converter = context -> {
        Company source = context.getSource();
        CompanyDto destination = new ModelMapper().map(source, CompanyDto.class);


        destination.setEmployeeCount(source.getEmployees().size());
        destination.setClientCount(source.getClients().size());
        destination.setVehicleCount(source.getVehicles().size());


        return destination;
    };
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
