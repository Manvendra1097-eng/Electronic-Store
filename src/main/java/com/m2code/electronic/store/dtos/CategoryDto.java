package com.m2code.electronic.store.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private String id;

    @NotBlank
    @Size(min = 4, message = "Minimum length 4 required")
    private String title;

    @NotBlank(message = "Description required")
    private String description;
    private String coverImage;
}
