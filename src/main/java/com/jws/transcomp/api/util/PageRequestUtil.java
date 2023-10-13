package com.jws.transcomp.api.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PageRequestUtil {
    private PageRequestUtil() {
    }

    public static PageRequest createPageRequest(Pageable pageable, String sort) {
        String[] sortCriteria = sort.split(",");
        List<Sort.Order> orders = new ArrayList<>();

        for (String criteria : sortCriteria) {
            char dir = criteria.charAt(0);
            String attr = criteria.substring(1);
            Sort.Order order = new Sort.Order(dir == '+' ? Sort.Direction.ASC : Sort.Direction.DESC, attr);
            orders.add(order);
        }

        Sort sortObj = Sort.by(orders);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortObj);
    }
}
