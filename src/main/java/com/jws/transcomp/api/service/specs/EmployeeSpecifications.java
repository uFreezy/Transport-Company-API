package com.jws.transcomp.api.service.specs;

import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.base.LiscenceType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeSpecifications {
    static Specification<Employee> hasCompanyId(Long companyId) {
        return (root, query, builder) -> builder.equal(root.get("companyId"), companyId);
    }

    static Specification<Employee> hasLicenses(List<LiscenceType> licenses) {
        return (root, query, builder) -> root.get("licenses").in(licenses);
    }

    static Specification<Employee> hasSalaryGreaterThan(BigDecimal salary) {
        return (root, query, builder) -> builder.greaterThan(root.get("salary"), salary);
    }

    static Specification<Employee> hasSalaryLessThan(BigDecimal salary) {
        return (root, query, builder) -> builder.lessThan(root.get("salary"), salary);
    }
}