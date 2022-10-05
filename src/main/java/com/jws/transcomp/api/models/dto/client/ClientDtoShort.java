package com.jws.transcomp.api.models.dto.client;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class ClientDtoShort implements Serializable {
    @NotNull
    private Long id;
    @NotNull
    @Size(min = 3, max = 30)
    private String name;
}
