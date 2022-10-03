package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.controller.base.BaseTestController;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Role;
import com.jws.transcomp.api.models.base.LiscenceType;
import com.jws.transcomp.api.models.dto.employee.EmployeeCreateDto;
import com.jws.transcomp.api.models.dto.employee.EmployeeEditDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@WebAppConfiguration
public class EmployeeControllerTest extends BaseTestController {

    private List<Employee> employees = new ArrayList<>();

    @BeforeEach
    void setUp() {
        this.employees = this.generateEmployees();

        // employee from a different company
        Employee diffEmployee = new Employee(
                "diff_company",
                "1234",
                "1234",
                "admin's address",
                BigDecimal.valueOf(100),
                new HashSet<>(),
                new Role("Admin"),
                this.generateCompanies().get(1));

        diffEmployee.setId(99L);

        this.employees.add(diffEmployee);

        // Mock logged username
        given(securityService.findLoggedInUsername())
                .willReturn("admin");
        // Mock employee
        given(userService.findByUsername("admin"))
                .willReturn(this.employee);
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getEmployee_Successfully() throws Exception {

        Employee emp = this.employees.get(new Random().nextInt(this.employees.size()));

        emp.setCompany(userService.findByUsername("admin").getCompany());

        given(this.userService.findByUsername(emp.getUsername()))
                .willReturn(emp);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee?username=" + emp.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(emp.getUsername()));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getEmployee_Invalid() throws Exception {
        // doesn't belong to your company
        Employee emp = this.employees.get(new Random().nextInt(this.employees.size()));

        given(this.userService.findByUsername(emp.getUsername()))
                .willReturn(emp);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee?username=" + emp.getUsername()))
                .andExpect(status().isBadRequest());

        // invalid username
        given(this.userService.findByUsername(emp.getUsername()))
                .willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee?username=" + emp.getUsername()))
                .andExpect(status().isNotFound());

        // internal error
        given(this.userService.findByUsername("sample"))
                .willThrow(RuntimeException.class);


        mockMvc.perform(MockMvcRequestBuilders.get("/employee?username=sample"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getEmployees_Successfully() throws Exception {
        // filter salary
        Company mockCompany = userService.findByUsername("admin").getCompany();
        BigDecimal salaryFrom = new BigDecimal(200);
        BigDecimal salaryTo = new BigDecimal(300);

        List<Employee> resultEmps = employees.subList(1, 2);

        given(employeeService.filterEmployees(mockCompany.getId(), null, salaryFrom, salaryTo, null, DEFAULT_PAGE))
                .willReturn(new PaginatedResponse(resultEmps, (long) employees.size(), 1));


        mockMvc.perform(MockMvcRequestBuilders.get("/employee/all?salary_from=" + salaryFrom + "&salary_to=" + salaryTo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list").exists())
                .andExpect(jsonPath("$.item_list[0].username", containsString(resultEmps.get(0).getUsername())))
                .andExpect(jsonPath("$.item_list[2]").doesNotExist());

        // filter licenses
        List<LiscenceType> licences = new ArrayList<>();
        licences.add(LiscenceType.C);

        List<Employee> licenceFiltered = employees.stream()
                .filter(e -> e.getLicenses().contains(LiscenceType.C))
                .collect(Collectors.toList());

        given(employeeService.filterEmployees(mockCompany.getId(), licences, null, null, null, DEFAULT_PAGE))
                .willReturn(new PaginatedResponse(licenceFiltered, (long) licenceFiltered.size(), 1));


        mockMvc.perform(MockMvcRequestBuilders.get("/employee/all?licenses=" + LiscenceType.C))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list").exists())
                .andExpect(jsonPath("$.item_list[0].username", containsString(licenceFiltered.get(0).getUsername())))
                .andExpect(jsonPath("$.item_list[1]").doesNotExist());

        // sort
        String sortCriteria = "+salary";

        this.employees.sort(Comparator.comparing(Employee::getSalary));

        given(employeeService.filterEmployees(mockCompany.getId(), null, null, null, sortCriteria, DEFAULT_PAGE))
                .willReturn(new PaginatedResponse(this.employees, (long) this.employees.size(), 1));


        mockMvc.perform(MockMvcRequestBuilders.get("/employee/all?sort_by=" + sortCriteria))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item_list").exists())
                .andExpect(jsonPath("$.item_list[0].salary").value(this.employees.get(0).getSalary()));
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void getEmployees_Invalid() throws Exception {

        // filter invalid salary
        Company mockCompany = userService.findByUsername("admin").getCompany();
        BigDecimal salaryFrom = new BigDecimal(10000);
        BigDecimal salaryTo = new BigDecimal(1);

        given(employeeService.filterEmployees(mockCompany.getId(), null, salaryFrom, salaryTo, null, DEFAULT_PAGE))
                .willThrow(IllegalArgumentException.class);


        mockMvc.perform(MockMvcRequestBuilders.get("/employee/all?salary_from=" + salaryFrom + "&salary_to=" + salaryTo))
                .andExpect(status().isBadRequest());

        // filter invalid sort
        String sortCriteria = "+invalid_attr";


        given(employeeService.filterEmployees(mockCompany.getId(), null, null, null, sortCriteria, DEFAULT_PAGE))
                .willThrow(PropertyReferenceException.class);


        mockMvc.perform(MockMvcRequestBuilders.get("/employee/all?sort_by=" + sortCriteria))
                .andExpect(status().isBadRequest());

        // internal server error
        given(employeeService.filterEmployees(mockCompany.getId(), null, null, null, null, DEFAULT_PAGE))
                .willThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/all"))
                .andExpect(status().is5xxServerError());

    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void addEmployee_Successfully() throws Exception {
        Role role = new Role("Admin", new HashSet<>());
        role.setId(1L);

        given(this.roleService.findById(1L))
                .willReturn(role);

        EmployeeCreateDto createDto = new EmployeeCreateDto("diego", "palm street", BigDecimal.valueOf(123), new HashSet<>(), 1L, new Company("Kadabra"));

        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(createDto)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void addEmployee_Invalid() throws Exception {
        // invalid name
        EmployeeCreateDto createDto = new EmployeeCreateDto("-------", "palm street", BigDecimal.valueOf(123), new HashSet<>(), 1L, new Company("Kadabra"));

        given(this.roleService.findById(1L))
                .willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void editEmployee_Successfully() throws Exception {
        Employee employee = this.employees.get(new Random().nextInt(4));
        employee.setCompany(this.employee.getCompany());

        given(this.userService.findById(employee.getId()))
                .willReturn(employee);
        given(this.roleService.findById(employee.getRole().getId()))
                .willReturn(employee.getRole());

        EmployeeEditDto editDto = new EmployeeEditDto(employee.getId(), "new_name", "new address", new BigDecimal(1000), new HashSet<>(), new Role("brand new role"));

        mockMvc.perform(MockMvcRequestBuilders.put("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(editDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void editEmployee_Invalid() throws Exception {
        // not your company
        Employee employee = this.employees.get(new Random().nextInt(4));
        employee.setCompany(new Company("invalid"));

        given(this.userService.findById(employee.getId()))
                .willReturn(employee);
        given(this.roleService.findById(employee.getRole().getId()))
                .willReturn(employee.getRole());

        EmployeeEditDto editDto = new EmployeeEditDto(employee.getId(), "new_name", "new address", new BigDecimal(1000), new HashSet<>(), new Role("brand new role"));

        mockMvc.perform(MockMvcRequestBuilders.put("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(editDto)))
                .andExpect(status().isBadRequest());

        // Invalid id
        given(this.userService.findById(Long.MAX_VALUE))
                .willThrow(IllegalArgumentException.class);

        EmployeeEditDto invalidDto = new EmployeeEditDto(Long.MAX_VALUE, "new_name", "new address", new BigDecimal(1000), new HashSet<>(), new Role("brand new role"));

        mockMvc.perform(MockMvcRequestBuilders.put("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objToJson(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void deleteEmployee_Successfully() throws Exception {
        Employee emp = this.employees.get(0);
        emp.setCompany(this.employee.getCompany());

        given(this.userService.findById(emp.getId()))
                .willReturn(emp);
        given(this.userService.delete(emp.getId())).willReturn(true);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders.delete("/employee?id=" + emp.getId()));
        res.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"Admin"})
    void deleteEmployee_Invalid() throws Exception {
        // invalid id
        given(this.userService.findById(1000L))
                .willThrow(IllegalArgumentException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/employee?id=1000"))
                .andExpect(status().isBadRequest());

        // not your company

        Employee emp = this.employees.get(new Random().nextInt(this.employees.size()));
        emp.setCompany(new Company("invalid"));

        given(this.userService.findById(emp.getId()))
                .willReturn(emp);
        mockMvc.perform(MockMvcRequestBuilders.delete("/employee?id=" + emp.getId()))
                .andExpect(status().isBadRequest());

        // unprocessable entity
        Employee unp = this.employees.get(0);
        unp.setCompany(this.employee.getCompany());

        given(this.userService.findById(unp.getId()))
                .willReturn(unp);
        given(this.userService.delete(unp.getId()))
                .willReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete("/employee?id=" + unp.getId()))
                .andExpect(status().isUnprocessableEntity());

    }

}
