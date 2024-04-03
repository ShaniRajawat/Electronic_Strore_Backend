package com.sr.electronic.store.Electronic_Store.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateRequest {
    private String orderStatus;

    private String paymentStatus;

    private String billingName;

    private String billingPhone;

    private String billingAddress;

    private Date deliveredDate;
}
