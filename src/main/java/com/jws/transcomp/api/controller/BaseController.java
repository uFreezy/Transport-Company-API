package com.jws.transcomp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public abstract class BaseController {
    @Autowired
    ObjectMapper objectMapper;

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    SecurityService securityService;
    @Autowired
    UserService userService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ClientService clientService;

    @Autowired
    RoleService roleService;

    @Autowired
    CompanyService companyService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    TripService tripService;


    public Employee getLoggedUser() {
        return this.userService.findByUsername(this.securityService.findLoggedInUsername());
    }

    public Company getLoggedCompany() {
        return this.userService.findByUsername(this.securityService.findLoggedInUsername()).getCompany();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handle(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        return ResponseEntity.badRequest().body(fieldErrors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handle(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
