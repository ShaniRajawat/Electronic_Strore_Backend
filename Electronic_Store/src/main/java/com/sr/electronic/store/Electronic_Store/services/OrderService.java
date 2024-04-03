package com.sr.electronic.store.Electronic_Store.services;

import com.sr.electronic.store.Electronic_Store.dtos.CreateOrderRequest;
import com.sr.electronic.store.Electronic_Store.dtos.OrderDto;
import com.sr.electronic.store.Electronic_Store.dtos.OrderUpdateRequest;
import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {

    //create order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //remove order
    void removeOrder(String orderId);

    //get orders of users
    List<OrderDto> getOrdersOfUser(String userId);

    //get a order
    OrderDto getOrder(String orderId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    OrderDto updateOrder(String orderId, OrderUpdateRequest request);
    OrderDto updateOrder(String orderId, OrderDto request);
}
