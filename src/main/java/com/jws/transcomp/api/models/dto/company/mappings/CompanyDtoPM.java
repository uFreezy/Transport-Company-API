package com.jws.transcomp.api.models.dto.company.mappings;

import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.dto.company.CompanyDto;
import org.modelmapper.PropertyMap;

public class CompanyDtoPM extends PropertyMap<Company, CompanyDto> {
    @Override
    protected void configure() {
        map().setEmployeeCount(source.getEmployees().size());
        map().setClientCount(source.getClients().size());
        map().setVehicleCount(source.getVehicles().size());
    }
}