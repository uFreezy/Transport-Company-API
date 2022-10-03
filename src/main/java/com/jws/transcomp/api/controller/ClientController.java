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
@RequestMapping("client")
public class ClientController extends BaseController {

    @GetMapping
    public ResponseEntity<Object> getClient(@RequestParam(name = "id") long clientId) {
        try {
            Client client = this.clientService.findById(clientId);

            ClientDto clientDto = modelMapper.map(client, ClientDto.class);
            return ResponseEntity.ok(clientDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
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
        try {
            if (this.clientService.idExists(clientDto.getId())) {
                Client client = this.clientService.findById(clientDto.getId());

                if (client.getCompanies().contains(getLoggedCompany())) {
                    throw new IllegalArgumentException("This person is already a client of your company.");
                }

                client.addCompany(getLoggedCompany());
                return ResponseEntity.ok("Existing client has been added to your company");
            } else {
                this.clientService.save(clientDto.toEntity());
                return ResponseEntity.ok("New client added successfully!");
            }

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/remove")
    public ResponseEntity<Object> removeFromCompany(@RequestParam(name = "id") Long clientId) {
        try {
            Client client = this.clientService.findById(clientId);

            if (client.removeCompany(getLoggedCompany())) {
                return ResponseEntity.ok("This person is no longer a client of your company");
            } else {
                return ResponseEntity.badRequest().body("This person isn't a client of your company.");
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
