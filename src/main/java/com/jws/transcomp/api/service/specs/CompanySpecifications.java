package com.jws.transcomp.api.service.specs;

import com.jws.transcomp.api.models.Company;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public interface CompanySpecifications {

    static Specification<Company> hasName(String name) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }

    static Specification<Company> hasRevenueGreaterThan(BigDecimal revenue) {
        return (root, query, builder) -> builder.greaterThan(root.get("revenue"), revenue);
    }

    static Specification<Company> hasRevenueLessThan(BigDecimal revenue) {
        return (root, query, builder) -> builder.lessThan(root.get("revenue"), revenue);
    }
}
