package com.jws.transcomp.api.repository;

import com.jws.transcomp.api.models.Company;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);

    Optional<Company> findById(Long id);

    @Override
    <S extends Company> List<S> findAll(Example<S> example);

    Page<Company> findAllByNameContains(String name, Pageable pageable);

    Page<Company> findAllByRevenueGreaterThan(BigDecimal revenue, Pageable pageable);

    Page<Company> findAllByRevenueLessThan(BigDecimal revenue, Pageable pageable);

    Page<Company> findAllByRevenueGreaterThanAndRevenueLessThan(BigDecimal revenueFrom, BigDecimal revenueTo, Pageable pageable);

    Page<Company> findAllByNameContainsAndRevenueGreaterThan(String name, BigDecimal revenue, Pageable pageable);

    Page<Company> findAllByNameContainsAndRevenueLessThan(String name, BigDecimal revenue, Pageable pageable);

    Page<Company> findAllByNameContainsAndRevenueGreaterThanAndRevenueLessThan(String name, BigDecimal revenueFrom, BigDecimal revenueTo, Pageable pageable);


    void deleteById(Long id);
}
