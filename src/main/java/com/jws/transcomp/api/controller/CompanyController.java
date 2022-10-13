package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Trip;
import com.jws.transcomp.api.models.dto.company.CompanyCreateDto;
import com.jws.transcomp.api.models.dto.company.CompanyDto;
import com.jws.transcomp.api.models.dto.company.CompanyEditDto;
import com.jws.transcomp.api.models.dto.company.CompanyReportDto;
import com.jws.transcomp.api.models.dto.employee.DriverDataDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("company")
public class CompanyController extends BaseController {

    CompanyController() {
        this.modelMapper.createTypeMap(Company.class, CompanyDto.class).setConverter(CompanyDto.converter);
    }

    @GetMapping
    public ResponseEntity<Object> getCompany(@RequestParam(required = false) Long id) {

        if (id == null) {
            List<Company> companies = this.companyService.findAll();
            List<CompanyDto> companiesDto = new ArrayList<>();

            // TODO: Match the lists directly.
            companies.forEach(company ->
                    companiesDto.add(modelMapper.map(company, CompanyDto.class)));

            return ResponseEntity.ok(companiesDto);
        } else {
            try {
                Company company = this.companyService.findById(id);
                CompanyDto companyDto = modelMapper.map(company, CompanyDto.class);
                return ResponseEntity.ok(companyDto);
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchCompanies(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "revenue_from", required = false) BigDecimal revenueFrom,
                                                  @RequestParam(value = "revenue_to", required = false) BigDecimal revenueTo,
                                                  @RequestParam(value = "sort_by", required = false) String sortBy,
                                                  Pageable pageable) {

        if (name == null && revenueFrom == null && revenueTo == null) {
            return ResponseEntity.badRequest().body("You need to have at least one parameter to search by.");
        }

        try {
            PaginatedResponse response = companyService.filterCompanies(name, revenueFrom, revenueTo, sortBy, pageable);
            response.setItemList(modelMapper.map(response.getItemList(), new TypeToken<List<CompanyDto>>() {
            }.getType()));

            return ResponseEntity.ok(response);
        } catch (PropertyReferenceException ex) {
            return ResponseEntity.badRequest().body("Invalid sorting columns provided: " + ex.getPropertyName());
        }
    }

    @PostMapping
    public ResponseEntity<Object> addCompany(@Validated @RequestBody CompanyCreateDto companyDto) {
        if (companyService.existsByName(companyDto.getName()))
            return ResponseEntity.badRequest().body("Company with the name " + companyDto.getName() + " already exists.");

        this.companyService.save(companyDto.toEntityObject());

        return ResponseEntity.ok("Successfully created the new company! Name: " + companyDto.getName());
    }

    @PutMapping
    public ResponseEntity<Object> editCompany(@Validated @RequestBody CompanyEditDto companyDto) {
        try {
            Company comp = this.companyService.findById(companyDto.getId());
            modelMapper.map(companyDto, comp);
            this.companyService.save(comp);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        return ResponseEntity.ok("Successfully edited a company! Id: " + companyDto.getId());
    }


    @DeleteMapping
    public ResponseEntity<Object> deleteCompany(@RequestParam Long id) {
        if (!this.companyService.existsById(id))
            return ResponseEntity.badRequest().body(String.format("Company with id %s doesn't exist", id));

        try {
            if (this.companyService.deleteById(id))
                return ResponseEntity.ok("Company deleted successfully!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("Something went wrong: %s", ex.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong while deleting the company");
    }

    @GetMapping("/report")
    public ResponseEntity<Object> getCompanyReport(@RequestParam(name = "date_from", required = false)
                                                   @DateTimeFormat(pattern = "dd/MM/yyyy") Date dateFrom,
                                                   @RequestParam(name = "date_to", required = false)
                                                   @DateTimeFormat(pattern = "dd/MM/yyyy") Date dateTo) {
        if (dateFrom == null) dateFrom = new Date(Long.MIN_VALUE);
        if (dateTo == null) dateTo = new Date(Long.MAX_VALUE);

        if (dateFrom.after(dateTo))
            return ResponseEntity.badRequest().body("Before date cannot be set later than the after date.");
        Company cmp = getLoggedCompany();

        Date finalDateFrom = dateFrom;
        Date finalDateTo = dateTo;
        int totalTrips = (int) cmp.getTrips().stream()
                .filter(t -> t.getDeparture().after(finalDateFrom) && t.getDeparture().before(finalDateTo)).count();


        BigDecimal totalRevenue = cmp.getRevenue(dateFrom, dateTo);
        List<DriverDataDto> driverData = new ArrayList<>();

        cmp.getEmployees().forEach(e -> {
            if (e.getRole().getName().equals("User")) {
                List<Trip> driverTrips = cmp.getTrips().stream()
                        .filter(t -> t.getDriver().equals(e))
                        .filter(t -> t.getDeparture().after(finalDateFrom) && t.getDeparture().before(finalDateTo))
                        .collect(Collectors.toList());
                int tripCount = driverTrips.size();
                BigDecimal driverRevenue = driverTrips.stream()
                        .filter(t -> t.getDeparture().after(finalDateFrom) && t.getDeparture().before(finalDateTo))
                        .map(Trip::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

                driverData.add(new DriverDataDto(e.getId(), e.getUsername(), tripCount, driverRevenue));
            }
        });
        return ResponseEntity.ok(new CompanyReportDto(totalTrips, totalRevenue, driverData));
    }
}
