package com.jws.transcomp.api.service.base;

import com.jws.transcomp.api.models.Client;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    void save(Client client);

    boolean any();

    boolean idExists(long id);

    Client findById(long id);

    PaginatedResponse getClients(Pageable pageable);


    List<Client> getAll();

    List<Client> getAll(int page, int size);

    long count();
}
