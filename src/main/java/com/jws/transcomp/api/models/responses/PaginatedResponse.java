package com.jws.transcomp.api.models.responses;

import lombok.Data;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PaginatedResponse implements Serializable {
    private Long numberOfItems;
    private int numberOfPages;
    private List<?> itemList;

    public PaginatedResponse(List<?> itemList, Long numberOfItems, int numberOfPages) {
        this.itemList = itemList;
        this.numberOfItems = numberOfItems;
        this.numberOfPages = numberOfPages;
    }

    public static List<Object> mapDto(List<?> data, Class dtoType) {
        List<Object> result = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();

        data.forEach(ele -> {
            try {
                Object obj = dtoType.getConstructor().newInstance();
                mapper.map(ele, obj);
                result.add(obj);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
        return result;
    }
}
