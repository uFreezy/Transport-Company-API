package com.jws.transcomp.api.repository;

import com.jws.transcomp.api.models.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Override
    Page<Client> findAll(Pageable pageable);
}
