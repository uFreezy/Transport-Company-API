package com.jws.transcomp.api.repository;

import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Role;
import com.jws.transcomp.api.models.base.LiscenceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUsername(String username);

    Optional<Employee> findById(Long userId);

    List<Employee> findByRole(Role role);

    Page<Employee> findAll(Pageable pageable);

    Page<Employee> findAllByCompanyId(Long companyId, Pageable pageable);

    Page<Employee> findAllByCompanyIdAndSalaryGreaterThan(Long companyId, BigDecimal salary, Pageable pageable);

    Page<Employee> findAllByCompanyIdAndSalaryLessThan(Long companyId, BigDecimal salary, Pageable pageable);

    Page<Employee> findAllByCompanyIdAndSalaryGreaterThanAndSalaryLessThan(Long companyId, BigDecimal salaryFrom, BigDecimal salaryTo, Pageable pageable);

    Page<Employee> findAllByCompanyIdAndLicensesIn(Long companyId, Set<LiscenceType> licenses, Pageable pageable);

    Page<Employee> findAllByCompanyIdAndLicensesInAndSalaryGreaterThan(Long companyId, Set<LiscenceType> licenses, BigDecimal salaryFrom, Pageable pageable);

    Page<Employee> findAllByCompanyIdAndLicensesInAndSalaryLessThan(Long companyId, Set<LiscenceType> licenses, BigDecimal salaryTo, Pageable pageable);

    Page<Employee> findAllByCompanyIdAndLicensesInAndSalaryGreaterThanAndSalaryLessThan(Long companyId, Set<LiscenceType> licenses, BigDecimal salaryFrom, BigDecimal salaryTo, Pageable pageable);


    void deleteById(Long userId);
}
