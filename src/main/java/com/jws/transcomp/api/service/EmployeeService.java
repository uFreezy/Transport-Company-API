package com.jws.transcomp.api.service;

import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.base.LiscenceType;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    void save(Employee employee);

    boolean delete(Employee employee);

    Employee findByUsername(String username);

    Optional<Employee> findById(Long id);

    Employee getByIndex(int indx);

    List<Employee> findByRole(String roleName);

    PaginatedResponse findAll(Pageable pageable);

    PaginatedResponse findAllByCompany(Long companyId, String sortBy, Pageable pageable);

    PaginatedResponse filterEmployees(Long companyId, List<LiscenceType> licenses, BigDecimal salaryFrom, BigDecimal salaryTo, String sortBy, Pageable pageable);

    boolean any();

    long count();
}
