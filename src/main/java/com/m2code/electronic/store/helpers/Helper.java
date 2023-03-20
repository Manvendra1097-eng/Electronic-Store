package com.m2code.electronic.store.helpers;

import com.m2code.electronic.store.dtos.PageableResponse;
import com.m2code.electronic.store.dtos.UserDto;
import com.m2code.electronic.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <U, V> PageableResponse<U> getPageableResponse(Page<V> page, Class<U> type) {
//        list of user
        List<V> entity = page.getContent();
//        total pages
        int totalPages = page.getTotalPages();
//        total elements
        long totalElements = page.getTotalElements();
//        is last page
        boolean lastPage = page.isLast();
//        page number
        int pageNumber = page.getNumber();
//        page size
        int pageSize = page.getSize();

//        all users on current page
        List<U> content = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
        PageableResponse<U> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(content);
        pageableResponse.setLastPage(lastPage);
        pageableResponse.setPageNumber(pageNumber);
        pageableResponse.setPageSize(pageSize);
        pageableResponse.setTotalPages(totalPages);
        pageableResponse.setTotalElements(totalElements);

        return pageableResponse;
    }
}
