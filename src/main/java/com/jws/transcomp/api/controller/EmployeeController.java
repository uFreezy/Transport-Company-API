package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Role;
import com.jws.transcomp.api.models.base.LiscenceType;
import com.jws.transcomp.api.models.dto.employee.EmployeeCreateDto;
import com.jws.transcomp.api.models.dto.employee.EmployeeDto;
import com.jws.transcomp.api.models.dto.employee.EmployeeEditDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("employee")
public class EmployeeController extends BaseController {

    @GetMapping
    public ResponseEntity<Object> getEmployee(@RequestParam(name = "username") String username) {
        try {
            Employee employee = this.userService.findByUsername(username);

            EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

            if (employee.getCompany().equals(getLoggedCompany())) {
                return ResponseEntity.ok(employeeDto);
            } else {
                return ResponseEntity.badRequest().body("This employee doesn't belong to your company.");
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't find user with username: + " + username);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getEmployees(
            @RequestParam(value = "salary_from", required = false) BigDecimal salaryFrom,
            @RequestParam(value = "salary_to", required = false) BigDecimal salaryTo,
            @RequestParam(value = "licenses", required = false) List<LiscenceType> licenses,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            Pageable pageable) {

        Employee loggedUser = getLoggedUser();

        if (loggedUser.getRole().getName().equals("Admin") && loggedUser.getCompany() != null) {
            try {
                return ResponseEntity.ok(this.employeeService.filterEmployees(loggedUser.getCompany().getId(), licenses, salaryFrom, salaryTo, sortBy, pageable));
            } catch (PropertyReferenceException ex) {
                return ResponseEntity.badRequest().body("Invalid sorting columns provided: " + ex.getPropertyName());
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(ex.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong with the employee search.");
    }

    @PostMapping
    public ResponseEntity<Object> addEmployee(@Validated @RequestBody EmployeeCreateDto employeeInfo) {
        try {
            Role role = this.roleService.findById(employeeInfo.getRoleId());
            Company comp = this.userService.findByUsername(this.securityService.findLoggedInUsername()).getCompany();
            // TODO: use mapper
            Employee employee = new Employee(employeeInfo.getUsername(), employeeInfo.getAddress(), employeeInfo.getSalary(), employeeInfo.getLicenses(), role, comp);
            this.userService.save(employee);

            return ResponseEntity.ok("New employee created successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<Object> editEmployee(@Validated @RequestBody EmployeeEditDto eInfo) {
        try {
            Employee employee = userService.findById(eInfo.getId());
            if (employee.getCompany().equals(getLoggedCompany())) {
                if (eInfo.getRole() != null)
                    eInfo.setRole(roleService.findById(eInfo.getRole().getId()));
                this.userService.save(employee);
            } else {
                return ResponseEntity.badRequest().body("This employee doesn't belong to your company.");
            }

            return ResponseEntity.ok("User edited successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteEmployee(@RequestParam(name = "id") Long id) {
        try {
            if (this.userService.findById(id).getCompany().equals(getLoggedCompany())) {
                if (this.userService.delete(id))
                    return ResponseEntity.ok("User deleted successfully!");
                else
                    return ResponseEntity.unprocessableEntity().body("Something went wrong while deleting user.");
            } else {
                return ResponseEntity.badRequest().body("This employee doesn't belong to your company.");
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
