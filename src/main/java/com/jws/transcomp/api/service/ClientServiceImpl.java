package com.jws.transcomp.api.service;

import com.jws.transcomp.api.models.Client;
import com.jws.transcomp.api.models.dto.client.ClientDto;
import com.jws.transcomp.api.models.responses.PaginatedResponse;
import com.jws.transcomp.api.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository repo) {
        this.clientRepository = repo;
    }

    @Override
    public void save(Client client) {
        this.clientRepository.save(client);
    }

    @Override
    public boolean any() {
        return this.clientRepository.count() > 0;
    }

    @Override
    public boolean idExists(long id) {
        return this.clientRepository.existsById(id);
    }

    @Override
    public Client findById(long id) {
        return this.clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client with id " + id + " doesn't exist."));
    }

    @Override
    public PaginatedResponse getClients(Pageable pageable) {
        Page<Client> books = this.clientRepository.findAll(pageable);

        return new PaginatedResponse(PaginatedResponse.mapDto(books.getContent(), ClientDto.class),
                books.getTotalElements(), books.getTotalPages());
    }

    @Override
    public List<Client> getAll(int page, int size) {
        return this.clientRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public List<Client> getAll() {
        Page<Client> page = this.clientRepository.findAll(PageRequest.of(0, 50));
        List<Client> clientList = new ArrayList<>(page.getContent());

        while (page.hasNext()) {
            page = this.clientRepository.findAll(page.nextPageable());
            clientList.addAll(page.getContent());
        }

        return clientList;
    }

    @Override
    public long count() {
        return this.clientRepository.count();
    }
}
