package com.m2code.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cartItems")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;
    private int totalPrice;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
