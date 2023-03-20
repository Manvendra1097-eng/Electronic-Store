package com.m2code.electronic.store.controllers;

import com.m2code.electronic.store.dtos.ApiResponseMessage;
import com.m2code.electronic.store.dtos.ImageApiResponse;
import com.m2code.electronic.store.dtos.PageableResponse;
import com.m2code.electronic.store.dtos.ProductDto;
import com.m2code.electronic.store.services.FileService;
import com.m2code.electronic.store.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final FileService fileService;

    @Value("${product.image.path}")
    private String productImagePath;

    public ProductController(ProductService productService, FileService fileService) {
        this.productService = productService;
        this.fileService = fileService;
    }

    //    create product
    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.create(productDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    //    update product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(@RequestBody ProductDto productDto, @PathVariable String productId) {
        ProductDto updatedProduct = productService.update(productDto, productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    //    delete product
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId) {
        productService.delete(productId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder().message("Product deleted").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    //    get all products
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        PageableResponse<ProductDto> pageableResponse = productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pageableResponse);
    }

    //    get all live product
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProduct(
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir
    ) {
        PageableResponse<ProductDto> pageableResponse = productService.getAllLiveProduct(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pageableResponse);
    }

    //    search product
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(@PathVariable String keyword,
                                                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                      @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                      @RequestParam(value = "sortBy", defaultValue = "title") String sortBy,
                                                                      @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        PageableResponse<ProductDto> productDtoList = productService.searchProduct(keyword, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(productDtoList);
    }

    //    get product by id
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String productId) {
        ProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.ok(productDto);
    }

    //    upload image
    @PostMapping("/upload/{productId}")
    public ResponseEntity<ImageApiResponse> uploadImage(@RequestParam("productImage") MultipartFile image, @PathVariable String productId) throws IOException {

        String productImageName = fileService.uploadFile(image, productImagePath);

        ProductDto productDto = productService.getProductById(productId);
        productDto.setProductImage(productImageName);
        productService.update(productDto, productId);

        ImageApiResponse imageApiResponse = ImageApiResponse.builder().fileName(productImageName).message("Product image uploaded").status(HttpStatus.CREATED).success(true).build();

        return new ResponseEntity<>(imageApiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/image/{productId}")
    public void serveImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        String productImageName = productService.getProductById(productId).getProductImage();
        InputStream inputStream = fileService.renderFile(productImagePath, productImageName);

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }

}

