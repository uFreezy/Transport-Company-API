package com.jws.transcomp.api.controller;

import com.jws.transcomp.api.models.Client;
import com.jws.transcomp.api.models.Employee;
import com.jws.transcomp.api.models.dto.client.ClientDto;
import com.jws.transcomp.api.models.dto.client.CreateClientDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController extends BaseController {

    @GetMapping("/{client_id}")
    public ResponseEntity<Object> getClient(@PathVariable(name = "client_id") long clientId) {
        Client client = this.clientService.findById(clientId);

        ClientDto clientDto = modelMapper.map(client, ClientDto.class);
        return ResponseEntity.ok(clientDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getCompanyClients(Pageable pageable) {
        Employee loggedUser = getLoggedUser();

        if (loggedUser.getRole().getName().equals("Admin") && loggedUser.getCompany() != null) {
            return ResponseEntity.ok(this.clientService.getClients(pageable));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong with the employee search.");
    }

    @PostMapping
    public ResponseEntity<Object> addClient(@Validated @RequestBody CreateClientDto clientDto) {
        if (this.clientService.idExists(clientDto.getId())) {
            Client client = this.clientService.findById(clientDto.getId());

            if (client.getCompanies().contains(getLoggedCompany())) {
                throw new IllegalArgumentException("This person is already a client of your company.");
            }

            client.addCompany(getLoggedCompany());
            this.clientService.save(client);

            return ResponseEntity.ok("Existing client has been added to your company");
        } else {
            Client clientObj = clientDto.toEntity();
            clientObj.addCompany(getLoggedCompany());

            Client cl = this.clientService.save(clientObj);

            return ResponseEntity.created(getLocation(cl.getId())).body("New client added successfully!");
        }
    }

    @PutMapping("/remove/{clientId}")
    public ResponseEntity<Object> removeFromCompany(@PathVariable Long clientId) {
        Client client = this.clientService.findById(clientId);

        if (client.removeCompany(getLoggedCompany())) {
            this.clientService.save(client);

            return ResponseEntity.ok("This person is no longer a client of your company");
        } else {
            return ResponseEntity.badRequest().body("This person isn't a client of your company.");
        }
    }
}
