package com.jws.transcomp.api.service;

import com.jws.transcomp.api.models.Company;
import com.jws.transcomp.api.models.dto.company.CompanyDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import com.jws.transcomp.api.repository.CompanyRepository;
import com.jws.transcomp.api.util.PageRequestUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository repo) {
        this.companyRepository = repo;
    }

    @Override
    public void save(Company company) {
        this.companyRepository.save(company);
    }

    @Override
    public boolean delete(Company company) {
        if (this.companyRepository.existsById(company.getId())) {
            this.companyRepository.delete(company);
            return true;
        }

        return false;
    }


    @Override
    public boolean deleteById(Long id) {
        if (this.companyRepository.existsById(id)) {
            this.companyRepository.delete(this.companyRepository.getOne(id));
            return true;
        }

        return false;
    }

    @Override
    public boolean existsById(Long id) {
        return this.companyRepository.findById(id).isPresent();
    }

    @Override
    public boolean existsByName(String name) {
        return this.companyRepository.findByName(name).isPresent();
    }

    @Override
    public Company findByName(String name) {
        return this.companyRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company name."));
    }

    @Override
    public Company findById(Long id) {
        return this.companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company id."));
    }

    @Override
    public List<Company> findAll() {
        return this.companyRepository.findAll();
    }

    @Override
    public PaginatedResponse filterCompanies(String name, BigDecimal revenueFrom, BigDecimal revenueTo, String sortBy, Pageable pageable) {
        if (sortBy != null) {
            pageable = PageRequestUtil.createPageRequest(pageable, sortBy);
        }

        // TODO: For the love of god figure out a better way to do this.
        Page<Company> companies;
        if (name != null && revenueFrom != null && revenueTo != null) {
            companies = this.companyRepository.findAllByNameContainsAndRevenueGreaterThanAndRevenueLessThan(name, revenueFrom, revenueTo, pageable);
        } else if (name == null && revenueFrom != null && revenueTo != null) {
            companies = this.companyRepository.findAllByRevenueGreaterThanAndRevenueLessThan(revenueFrom, revenueTo, pageable);
        } else if (name != null && revenueFrom != null && revenueTo == null) {
            companies = this.companyRepository.findAllByNameContainsAndRevenueGreaterThan(name, revenueFrom, pageable);
        } else if (name != null && revenueFrom == null && revenueTo != null) {
            companies = this.companyRepository.findAllByNameContainsAndRevenueLessThan(name, revenueTo, pageable);
        } else if (name != null && revenueFrom == null && revenueTo == null) {
            companies = this.companyRepository.findAllByNameContains(name, pageable);
        } else if (name == null && revenueFrom != null && revenueTo == null) {
            companies = this.companyRepository.findAllByRevenueGreaterThan(revenueFrom, pageable);
        } else if (name == null && revenueFrom == null && revenueTo != null) {
            companies = this.companyRepository.findAllByRevenueLessThan(revenueTo, pageable);
        } else {
            throw new IllegalArgumentException("Invalid arguments passed for company filtration");
        }

        return new PaginatedResponse(PaginatedResponse.mapDto(companies.getContent(), CompanyDto.class),
                companies.getTotalElements(),
                companies.getTotalPages());
    }


    @Override
    public boolean any() {
        return this.companyRepository.count() > 0;
    }

    @Override
    public long count() {
        return this.companyRepository.count();
    }
}
