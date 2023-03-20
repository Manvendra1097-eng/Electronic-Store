package com.m2code.electronic.store.dtos;

import com.m2code.electronic.store.entities.CartItem;
import com.m2code.electronic.store.entities.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private int cartId;
    private Date createdAt;
    private UserDto user;
    private List<CartItemDto> cartItems = new ArrayList<>();
}
