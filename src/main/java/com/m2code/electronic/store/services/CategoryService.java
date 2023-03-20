package com.m2code.electronic.store.services;

import com.m2code.electronic.store.dtos.CategoryDto;
import com.m2code.electronic.store.dtos.PageableResponse;

public interface CategoryService {
    //    create
    CategoryDto create(CategoryDto categoryDto);

    //    update
    CategoryDto update(CategoryDto categoryDto, String categoryId);

    //    delete
    void delete(String categoryId);

    //    get all category
    PageableResponse<CategoryDto> getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir);

    //    get single category by id
    CategoryDto getCategoryById(String categoryId);
}
