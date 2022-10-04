package com.jws.transcomp.api.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestUtil {
    private PageRequestUtil() {
    }

    public static PageRequest createPageRequest(Pageable pageable, String sort) {
        Sort sortObj = null;
        String[] sortCriteria = sort.split(",");

        for (String criteria : sortCriteria) {
            char dir = criteria.charAt(0);
            String attr = criteria.substring(1);

            if (sortObj == null) {
                if (dir == '+')
                    sortObj = Sort.by(attr).ascending();
                else if (dir == '-')
                    sortObj = Sort.by(attr).descending();
                else
                    sortObj = Sort.by(attr);
            } else {
                if (dir == '+')
                    sortObj = sortObj.and(Sort.by(attr).ascending());
                else if (dir == '-')
                    sortObj = sortObj.and(Sort.by(attr).descending());
                else
                    sortObj = sortObj.and(Sort.by(attr));
            }
        }

        assert sortObj != null;
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortObj);
    }
}
