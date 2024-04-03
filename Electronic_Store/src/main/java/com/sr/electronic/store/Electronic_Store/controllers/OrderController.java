package com.sr.electronic.store.Electronic_Store.controllers;

import com.sr.electronic.store.Electronic_Store.dtos.*;
import com.sr.electronic.store.Electronic_Store.services.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Tag(name = "Order Controller",description = "This is Order API for order Operation!!")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //create Order
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request){
        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    //remove order
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().status(HttpStatus.OK)
                .message("Order is removed !!")
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
    //get orders of User
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderOfUser(@PathVariable String userId){
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(ordersOfUser, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(@RequestParam(value = "pageNumber", defaultValue = "0", required = false)int pageNumber,
                                                                       @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = "orderedDate",required = false)String sortBy,
                                                                       @RequestParam(value = "sortDir", defaultValue = "0",required = false)String sortDir)
    {
        PageableResponse<OrderDto> orders = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable("orderId") String orderId,
            @RequestBody OrderUpdateRequest request
    ) {

        OrderDto dto = this.orderService.updateOrder(orderId,request);
        return ResponseEntity.ok(dto);

    }
}
