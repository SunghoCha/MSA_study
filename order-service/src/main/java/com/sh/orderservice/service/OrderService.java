package com.sh.orderservice.service;

import com.sh.orderservice.dto.OrderDto;
import com.sh.orderservice.jpa.OrderEntity;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);
    List<OrderEntity> getAllOrdersByUserId(String userId);
}
