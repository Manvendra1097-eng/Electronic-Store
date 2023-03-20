package com.m2code.electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageApiResponse {
    private String fileName;
    private String message;
    private boolean success;
    private HttpStatus status;
}
