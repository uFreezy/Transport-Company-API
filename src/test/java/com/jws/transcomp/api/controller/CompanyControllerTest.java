package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.controller.base.BaseTestController;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.dto.company.CompanyCreateDto;
import com.jws.transcomp.api.models.dto.company.CompanyEditDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
@WebAppConfiguration
class CompanyControllerTest extends BaseTestController {

    @MockBean
    ApplicationContext context;

    // "Mocking" revenues.
    BigDecimal[] revenues = {
            BigDecimal.valueOf(100),
            BigDecimal.valueOf(200),
            BigDecimal.valueOf(300)};
    private List<Company> companies = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.companies = this.generateCompanies();

        given(companyService.findAll()).willReturn(companies);

        // Mock logged username
        given(securityService.findLoggedInUsername())
                .willReturn("admin");
        // Mock employee
        given(userService.findByUsername("admin"))
                .willReturn(this.employee);
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getCompany_Successfully() throws Exception {
        Company company = this.companies.get(new Random().nextInt(this.companies.size()));

        given(companyService.findById(company.getId())).willReturn(company);

        mockMvc.perform(MockMvcRequestBuilders.get("/company?id=" + company.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(company.getName()));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getCompany_Invalid() throws Exception {
        given(companyService.findById(-999L)).willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/company?id=-999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getCompany_All_Successfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/company"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[2]").exists());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void searchCompanies_Successfully() throws Exception {
        // filter by name
        String name = "Kadabra";
        List<Company> filteredResponseName = this.companies.stream().filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        given(companyService.filterCompanies(name, null, null, null, DEFAULT_PAGE))
                .willReturn(new PaginatedResponse(filteredResponseName, 1L, 1));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/company/search?name=%s", name)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list").exists())
                .andExpect(jsonPath("$.item_list[0].name", containsString(name)));


        // filter by revenue_from
        BigDecimal revenueFrom = BigDecimal.valueOf(300);
        List<Company> filteredResponse = this.companies.stream().filter(c -> revenues[c.getId().intValue()].compareTo(revenueFrom) >= 0)
                .collect(Collectors.toList());
        given(companyService.filterCompanies(null, revenueFrom, null, null, DEFAULT_PAGE))
                .willReturn(new PaginatedResponse(filteredResponse, 3L, 1));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/company/search?revenue_from=%s", revenueFrom)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list").exists())
                .andExpect(jsonPath("$.item_list[0].id").value(filteredResponse.get(0).getId()));


        // filter by revenue_to
        BigDecimal revenueTo = BigDecimal.valueOf(200);
        List<Company> filteredResponseTo = this.companies.stream().filter(c -> revenues[c.getId().intValue()].compareTo(revenueTo) <= 0)
                .collect(Collectors.toList());
        given(companyService.filterCompanies(null, null, revenueTo, null, DEFAULT_PAGE))
                .willReturn(new PaginatedResponse(filteredResponseTo, 3L, 1));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/company/search?revenue_to=%s", revenueTo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list").exists())
                .andExpect(jsonPath("$.item_list[0].id").value(filteredResponseTo.get(0).getId()));


        // filter by all three
        String nameFull = "ab";
        BigDecimal revenueFromFull = BigDecimal.valueOf(100);
        BigDecimal revenueToFull = BigDecimal.valueOf(300);

        List<Company> filteredResponseFull = this.companies.stream()
                .filter(c -> c.getName().toLowerCase().contains(nameFull))
                .filter(c -> revenues[c.getId().intValue()].compareTo(revenueFromFull) >= 0)
                .filter(c -> revenues[c.getId().intValue()].compareTo(revenueToFull) <= 0)
                .collect(Collectors.toList());

        given(companyService.filterCompanies(nameFull, revenueFromFull, revenueToFull, null, DEFAULT_PAGE))
                .willReturn(new PaginatedResponse(filteredResponseFull, 3L, 1));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/company/search?name=%s&revenue_from=%s&revenue_to=%s", nameFull, revenueFromFull, revenueToFull)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list").exists())
                .andExpect(jsonPath("$.item_list[0].id").value(filteredResponseFull.get(0).getId()));
        // sort

        String sortCriteria = "-name";

        List<Company> filteredResponseSort = new ArrayList<>(this.companies);

        filteredResponseSort.sort(Comparator.comparing(Company::getName).reversed());

        given(companyService.filterCompanies("dummy", null, null, sortCriteria, DEFAULT_PAGE))
                .willReturn(new PaginatedResponse(filteredResponseSort, 3L, 1));

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/company/search?name=dummy&sort_by=%s", sortCriteria)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list").exists())
                .andExpect(jsonPath("$.item_list[0].id").value(filteredResponseSort.get(0).getId()));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void searchCompanies_NoFilters() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/company/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void searchCompanies_InvalidSort() throws Exception {
        String sortCriteria = "-zoomzoom";

        List<Company> filteredResponseSort = new ArrayList<>(this.companies);

        filteredResponseSort.sort(Comparator.comparing(Company::getName).reversed());

        given(companyService.filterCompanies("dummy", null, null, sortCriteria, DEFAULT_PAGE))
                .willThrow(PropertyReferenceException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/company/search?name=dummy&sort_by=%s", sortCriteria)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void addCompany_Successfully() throws Exception {

        Company comp = new Company("Ekont");

        mockMvc.perform(MockMvcRequestBuilders.post("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(comp)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void addCompany_Invalid() throws Exception {
        // company with name already exists
        Company cmp = this.companies.get(0);
        CompanyCreateDto cmpDto = new CompanyCreateDto();

        modelMapper.map(cmp, cmpDto);

        given(this.companyService.existsByName(cmp.getName()))
                .willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(cmpDto)))
                .andExpect(status().isBadRequest());

        // empty company
        mockMvc.perform(MockMvcRequestBuilders.post("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(new CompanyCreateDto())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void editCompany_Successfully() throws Exception {
        given(companyService.findById(0L)).willReturn(this.companies.get(0));

        CompanyEditDto cmp = new CompanyEditDto(0L, "diff");

        mockMvc.perform(MockMvcRequestBuilders.put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(cmp)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void editCompany_Invalid() throws Exception {
        given(companyService.findById(0L)).willReturn(this.companies.get(0));

        CompanyEditDto cmp = new CompanyEditDto(-9L, "diff");

        // Wrong id
        mockMvc.perform(MockMvcRequestBuilders.put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(cmp)))
                .andExpect(status().isBadRequest());

        // Name too long

        CompanyEditDto cmp2 = new CompanyEditDto(0L, "different name");

        mockMvc.perform(MockMvcRequestBuilders.put("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(cmp2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void deleteCompany_Successfully() throws Exception {
        given(companyService.existsById(0L)).willReturn(true);
        given(companyService.deleteById(0L)).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/company?id=0"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void deleteCompany_Invalid() throws Exception {
        given(companyService.existsById(0L)).willReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/company?id=0"))
                .andExpect(status().isBadRequest());

        given(companyService.existsById(0L)).willReturn(true);
        given(companyService.deleteById(0L)).willReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/company?id=0"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getCompanyReport_Successfully() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/company/report"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getCompany_Report_Invalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/company/report?date_from=01/01/1990&date_to=01/01/1980"))
                .andExpect(status().isBadRequest());
    }

}
