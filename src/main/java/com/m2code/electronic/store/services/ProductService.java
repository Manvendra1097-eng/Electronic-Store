package com.m2code.electronic.store.services;

import com.m2code.electronic.store.dtos.PageableResponse;
import com.m2code.electronic.store.dtos.ProductDto;

import java.util.List;

public interface ProductService {
    //    create
    ProductDto create(ProductDto productDto);

    //    create product with category
    ProductDto createWithCategory(ProductDto productDto, String categoryId);

    //    update
    ProductDto update(ProductDto productDto, String productId);

    //    delete
    void delete(String productId);

    //    get all product
    PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    //    get all product are live
    PageableResponse<ProductDto> getAllLiveProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    //    get all product have keyword in title
    PageableResponse<ProductDto> searchProduct(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir);

    //    get product by productId
    ProductDto getProductById(String productId);

    //    set category for existing product
    ProductDto setCategoryForProduct(String productId, String categoryId);

//    get all products of particular category
    PageableResponse<ProductDto> getProductByCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir);
}
