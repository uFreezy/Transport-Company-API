package com.jws.transcomp.api.models.dto.client;

import com.jws.transcomp.api.models.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientDto {
    @NotNull
    private Long id;
    @NotNull
    @Size(min = 3, max = 32)
    private String name;

    public Client toEntity() {
        return new Client(this.id, this.name);
    }
}
