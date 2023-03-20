package com.m2code.electronic.store.controllers;

import com.m2code.electronic.store.dtos.*;
import com.m2code.electronic.store.services.CategoryService;
import com.m2code.electronic.store.services.FileService;
import com.m2code.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final FileService fileService;

    @Value("${category.image.path}")
    private String coverPhotoPath;

    public CategoryController(CategoryService categoryService, ProductService productService, FileService fileService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.fileService = fileService;
    }

    //    create
    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.create(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    //    update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> update(@RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    //    delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String categoryId) {
        categoryService.delete(categoryId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().status(HttpStatus.CREATED).success(true).message("Category with id: " + categoryId + " deleted").build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    //    get all categories
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //    get single category
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(categoryDto);
    }

    //    upload cover image
    @PostMapping("/upload/{categoryId}")
    public ResponseEntity<ImageApiResponse> uploadImage(@PathVariable String categoryId, @RequestParam("categoryImage") MultipartFile image) throws IOException {

        String coverPhotoName = fileService.uploadFile(image, coverPhotoPath);

        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        categoryDto.setCoverImage(coverPhotoName);
        categoryService.update(categoryDto, categoryId);

        ImageApiResponse imageApiResponse = ImageApiResponse.builder().fileName(coverPhotoName).success(true).status(HttpStatus.CREATED).message("Cover image uploaded").build();
        return new ResponseEntity<>(imageApiResponse, HttpStatus.CREATED);
    }

    //    render image
    @GetMapping("/image/{categoryId}")
    public void renderImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
        InputStream inputStream = fileService.renderFile(coverPhotoPath, categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(inputStream, response.getOutputStream());
    }

    //    create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(@PathVariable String categoryId, @RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> setCategoryForProduct(@PathVariable String categoryId, @PathVariable String productId) {
        ProductDto createdProduct = productService.setCategoryForProduct(productId, categoryId);
        return new ResponseEntity<>(createdProduct, HttpStatus.OK);
    }

//    getProductByCategory

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductByCategory(
            @PathVariable String categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<ProductDto> pageableResponse = productService.getProductByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pageableResponse);
    }
}
