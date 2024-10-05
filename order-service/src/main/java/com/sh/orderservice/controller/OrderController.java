package com.sh.orderservice.controller;

import com.sh.orderservice.dto.OrderDto;
import com.sh.orderservice.service.OrderService;
import com.sh.orderservice.vo.RequestOrder;
import com.sh.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final Environment env;
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable(name = "userId") String userId, @RequestBody RequestOrder requestOrder) {
        log.info("======> qty: {}",requestOrder.getQty());
        OrderDto orderDto = modelMapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto order = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = modelMapper.map(order, ResponseOrder.class);

        return ResponseEntity.ok().body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId) {
        List<ResponseOrder> responseOrders = orderService.getAllOrdersByUserId(userId).stream()
                .map(order -> modelMapper.map(order, ResponseOrder.class))
                .toList();

        return ResponseEntity.ok().body(responseOrders);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseOrder> getOrder(@PathVariable("orderId") String orderId) {
        OrderDto orderDto = orderService.getOrderByOrderId(orderId);
        ResponseOrder responseOrder = modelMapper.map(orderDto, ResponseOrder.class);

        return ResponseEntity.ok().body(responseOrder);
    }
}
