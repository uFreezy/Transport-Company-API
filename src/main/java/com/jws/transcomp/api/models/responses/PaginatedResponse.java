package com.jws.transcomp.api.models.responses;

import lombok.Data;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PaginatedResponse<T> implements Serializable {
    private Long numberOfItems;
    private int numberOfPages;
    private List<T> itemList;

    public PaginatedResponse(List<T> itemList, Long numberOfItems, int numberOfPages) {
        this.itemList = itemList;
        this.numberOfItems = numberOfItems;
        this.numberOfPages = numberOfPages;
    }

    public static <T, D> List<D> mapDto(List<T> data, Class<D> dtoType) {
        List<D> result = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();

        data.forEach(element -> {
            D dto = mapper.map(element, dtoType);
            result.add(dto);
        });

        return result;
    }
}
