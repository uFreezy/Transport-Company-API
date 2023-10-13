package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Role;
import com.jws.transcomp.api.models.base.LiscenceType;
import com.jws.transcomp.api.models.dto.employee.EmployeeCreateDto;
import com.jws.transcomp.api.models.dto.employee.EmployeeDto;
import com.jws.transcomp.api.models.dto.employee.EmployeeEditDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController extends BaseController {


    @GetMapping("/{username}")
    public ResponseEntity<Object> getEmployee(@PathVariable String username) {
        Employee employee = this.userService.findByUsername(username);

        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        if (employee.getCompany().equals(getLoggedCompany())) {
            return ResponseEntity.ok(employeeDto);
        } else {
            return ResponseEntity.badRequest().body("This employee doesn't belong to your company.");
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
            return ResponseEntity.ok(this.employeeService.filterEmployees(loggedUser.getCompany().getId(), licenses, salaryFrom, salaryTo, sortBy, pageable));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong with the employee search.");
    }

    @PostMapping
    public ResponseEntity<Object> addEmployee(@Validated @RequestBody EmployeeCreateDto employeeInfo) {
        Role role = this.roleService.findById(employeeInfo.getRoleId());
        Company comp = this.userService.findByUsername(this.securityService.findLoggedInUsername()).getCompany();

        Employee employee = new Employee(employeeInfo.getUsername(), employeeInfo.getAddress(), employeeInfo.getSalary(), employeeInfo.getLicenses(), role, comp);
        employee = this.userService.save(employee);

        return ResponseEntity.created(getLocation(employee.getId())).body("New employee created successfully.");
    }


    @PutMapping
    public ResponseEntity<Object> editEmployee(@Validated @RequestBody EmployeeEditDto eInfo) {
        Employee employee = userService.findById(eInfo.getId());
        if (employee.getCompany().equals(getLoggedCompany())) {
            if (eInfo.getRole() != null)
                eInfo.setRole(roleService.findById(eInfo.getRole().getId()));
            this.userService.save(employee);
        } else {
            return ResponseEntity.badRequest().body("This employee doesn't belong to your company.");
        }

        return ResponseEntity.ok("User edited successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable Long id) {
        if (this.userService.findById(id).getCompany().equals(getLoggedCompany())) {
            if (this.userService.delete(id))
                return ResponseEntity.ok("User deleted successfully!");
            else
                return ResponseEntity.unprocessableEntity().body("Something went wrong while deleting user.");
        } else {
            return ResponseEntity.badRequest().body("This employee doesn't belong to your company.");
        }
    }
}
