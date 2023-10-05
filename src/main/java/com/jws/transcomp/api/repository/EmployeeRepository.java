package com.jws.transcomp.api.repository;

import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUsername(String username);

    List<Employee> findByRole(Role role);

    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

    Page<Employee> findAllByCompanyId(Long companyId, Pageable pageable);

    void deleteById(Long userId);
}
