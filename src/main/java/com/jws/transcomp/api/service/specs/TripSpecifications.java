package com.jws.transcomp.api.service.specs;

import com.jws.transcomp.api.models.Trip;
import com.jws.transcomp.api.models.base.TripType;
import org.springframework.data.jpa.domain.Specification;

public interface TripSpecifications {

    static Specification<Trip> hasType(TripType type) {
        return (root, query, builder) -> builder.equal(root.get("type"), type);
    }

    static Specification<Trip> hasDestination(String destination) {
        return (root, query, builder) -> builder.equal(root.get("endingPoint"), destination);
    }

    static Specification<Trip> hasCompanyId(Long companyId) {
        return (root, query, builder) -> builder.equal(root.get("companyId"), companyId);
    }
}
