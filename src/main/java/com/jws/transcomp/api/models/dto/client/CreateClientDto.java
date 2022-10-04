package com.jws.transcomp.api.models.dto.client;

import com.jws.transcomp.api.models.Client;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateClientDto {
    @NotNull
    private Long id;
    @NotNull
    @Size(min = 3, max = 32)
    private String name;

    public CreateClientDto() {

    }

    public CreateClientDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client toEntity() {
        return new Client(this.id, this.name);
    }
}
