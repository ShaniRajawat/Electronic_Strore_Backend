package com.sr.electronic.store.Electronic_Store.services.implementaions;

import com.sr.electronic.store.Electronic_Store.dtos.CreateOrderRequest;
import com.sr.electronic.store.Electronic_Store.dtos.OrderDto;
import com.sr.electronic.store.Electronic_Store.dtos.OrderUpdateRequest;
import com.sr.electronic.store.Electronic_Store.dtos.PageableResponse;
import com.sr.electronic.store.Electronic_Store.entities.*;
import com.sr.electronic.store.Electronic_Store.exceptions.BadApiRequestException;
import com.sr.electronic.store.Electronic_Store.exceptions.ResourceNOtFoundException;
import com.sr.electronic.store.Electronic_Store.helper.Helper;
import com.sr.electronic.store.Electronic_Store.repositories.CartRepository;
import com.sr.electronic.store.Electronic_Store.repositories.OrderRepository;
import com.sr.electronic.store.Electronic_Store.repositories.UserRepository;
import com.sr.electronic.store.Electronic_Store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImple implements OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();
        //fetch user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNOtFoundException("User with given Id not  found !!"));
        //fetch Cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNOtFoundException("No cart is found with given Id !!"));
        List<CartItem> cartItems = cart.getItems();
        if(cartItems.size()<=0){
            throw new BadApiRequestException("Invalid Number of items in cart !!");
        }
        //other checks

        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

//        orderItem, Amount

        AtomicReference<Integer> orderAmount =new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
//            CartItem-> Order item
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();
            orderAmount.set(cartItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        //
        cart.getItems().clear();
        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNOtFoundException("Order is not found with given Id !!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNOtFoundException("User is not found with the given Id !!"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
    }

    @Override
    public OrderDto getOrder(String orderId) {
        Order order = this.orderRepository.findById(orderId).orElseThrow(() -> new ResourceNOtFoundException("Order not found !!"));
        return this.modelMapper.map(order, OrderDto.class);
    }

    @Override
    public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);
        return Helper.getPageableResponse(page, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(String orderId, OrderUpdateRequest request) {
        //get the Order
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNOtFoundException("Order is not found with the Given Id!!"));
        order.setBillingName(request.getBillingName());
        order.setBillingPhone(request.getBillingPhone());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setOrderStatus(request.getOrderStatus());
        order.setDeliveredDate(request.getDeliveredDate());
        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(String orderId, OrderDto request) {
        //get the order
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new BadApiRequestException("Invalid update data"));
        order.setBillingName(request.getBillingName());
        order.setBillingPhone(request.getBillingPhone());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setOrderStatus(request.getOrderStatus());
        order.setDeliveredDate(request.getDeliveredDate());
        order.setRazorPayOrderId(request.getRazorPayOrderId());
        order.setPaymentId(request.getPaymentId());
        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, OrderDto.class);
    }

}
