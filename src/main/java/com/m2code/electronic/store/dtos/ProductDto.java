package com.m2code.electronic.store.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m2code.electronic.store.entities.Category;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String productId;
    private String title;
    private String description;
    private int price;
    private int discountedPrice;
    private int quantity;
    @JsonFormat(pattern = "dd-MM-yyy")
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImage;
    private CategoryDto category;
}
