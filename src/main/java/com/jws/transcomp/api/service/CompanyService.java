package com.jws.transcomp.api.service;

import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CompanyService {
    void save(Company company);

    boolean delete(Company company);

    boolean deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByName(String name);

    Company findByName(String username);

    Company findById(Long id);

    List<Company> findAll();

    PaginatedResponse filterCompanies(String name, BigDecimal revenueFrom, BigDecimal revenueTo, String sortBy, Pageable pageable);

    boolean any();

    long count();
}
