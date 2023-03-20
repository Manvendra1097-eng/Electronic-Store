package com.m2code.electronic.store.dtos;

import com.m2code.electronic.store.validate.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/*
 * Data transfer object to transfer the data
 * Entities are used to work on data layer
 * Entities are directly mapped with database table,
 * so it may cause security glitch
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    @NotBlank(message = "Name can't be empty")
    private String name;
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Invalid email")
    private String email;
    @NotBlank(message = "Password can't be empty")
    private String password;
    private String about;
    @NotBlank(message = "Gender field required")
    private String gender;
    @ImageNameValid
    private String imageName;
}
