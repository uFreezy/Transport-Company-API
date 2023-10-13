package com.jws.transcomp.api.service.impl;

import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Role;
import com.jws.transcomp.api.models.base.LiscenceType;
import com.jws.transcomp.api.models.dto.employee.EmployeeDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import com.jws.transcomp.api.repository.EmployeeRepository;
import com.jws.transcomp.api.repository.RoleRepository;
import com.jws.transcomp.api.service.base.EmployeeService;
import com.jws.transcomp.api.service.specs.EmployeeSpecifications;
import com.jws.transcomp.api.util.PageRequestUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Employee save(Employee employee) {
        return this.employeeRepository.save(employee);
    }

    @Override
    public boolean delete(Employee employee) {
        if (this.employeeRepository.existsById(employee.getId())) {
            this.employeeRepository.delete(employee);
            return true;
        }

        return false;
    }

    @Override
    public Employee findByUsername(String username) {
        return this.employeeRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with username + " + username));
    }

    @Override
    public Employee findById(Long id) {
        return this.employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Invalid employee id."));
    }

    @Override
    public Employee getByIndex(int indx) {
        return null;
    }

    @Override
    public List<Employee> findByRole(String roleName) {
        Role role = this.roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("No role exists with name: " + roleName));

        return this.employeeRepository.findByRole(role);
    }

    @Override
    public PaginatedResponse<EmployeeDto> findAll(Pageable pageable) {
        Page<Employee> employees = this.employeeRepository.findAll(pageable);

        return new PaginatedResponse<>(PaginatedResponse.mapDto(employees.getContent(), EmployeeDto.class),
                employees.getTotalElements(),
                employees.getTotalPages());
    }

    @Override
    public PaginatedResponse<EmployeeDto> findAllByCompany(Long companyId, String sortBy, Pageable pageable) {
        if (sortBy != null) {
            pageable = PageRequestUtil.createPageRequest(pageable, sortBy);
        }

        Page<Employee> employees = this.employeeRepository.findAllByCompanyId(companyId, pageable);

        return new PaginatedResponse<>(PaginatedResponse.mapDto(employees.getContent(), EmployeeDto.class),
                employees.getTotalElements(),
                employees.getTotalPages());
    }

    @Override
    public PaginatedResponse<EmployeeDto> filterEmployees(Long companyId, List<LiscenceType> licenses, BigDecimal salaryFrom, BigDecimal salaryTo, String sortBy, Pageable pageable) {
        if (companyId == null) {
            throw new IllegalArgumentException("Company id is empty.");
        }

        if (salaryFrom != null && salaryTo != null && salaryFrom.compareTo(salaryTo) > 0) {
            throw new IllegalArgumentException("salary_from cannot be larger than salary_to");
        }

        Specification<Employee> spec = Specification.where(EmployeeSpecifications.hasCompanyId(companyId));

        if (licenses != null && !licenses.isEmpty()) {
            spec = spec.and(EmployeeSpecifications.hasLicenses(licenses));
        }

        if (salaryFrom != null) {
            spec = spec.and(EmployeeSpecifications.hasSalaryGreaterThan(salaryFrom));
        }

        if (salaryTo != null) {
            spec = spec.and(EmployeeSpecifications.hasSalaryLessThan(salaryTo));
        }

        if (sortBy != null) {
            pageable = PageRequestUtil.createPageRequest(pageable, sortBy);
        }

        Page<Employee> employees = this.employeeRepository.findAll(spec, pageable);

        return new PaginatedResponse<>(PaginatedResponse.mapDto(employees.getContent(), EmployeeDto.class),
                employees.getTotalElements(),
                employees.getTotalPages());
    }

    @Override
    public boolean any() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public long count() {
        return this.employeeRepository.count();
    }
}
