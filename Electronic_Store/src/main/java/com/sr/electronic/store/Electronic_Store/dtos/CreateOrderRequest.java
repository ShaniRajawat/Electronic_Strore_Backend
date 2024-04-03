package com.sr.electronic.store.Electronic_Store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

    @NotBlank(message = "Cart Id is required")
    private String cartId;
    @NotBlank(message = "User Id is required")
    private String userId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOT_PAID";
    @NotBlank(message = "Address is required")
    private String billingAddress;
    @NotBlank(message = "Phone is required")
    private String billingPhone;
    @NotBlank(message = "Name is required")
    private String billingName;
}
