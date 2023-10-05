package com.jws.transcomp.api.repository;

import com.jws.transcomp.api.models.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);

    Page<Company> findAll(Specification<Company> spec, Pageable pageable);

    void deleteById(Long id);


}
