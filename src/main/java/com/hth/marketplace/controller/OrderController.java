package com.hth.marketplace.controller;

import com.hth.marketplace.dto.CreateOrderRequest;
import com.hth.marketplace.dto.OrderResponse;
import com.hth.marketplace.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/marketplace/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/buyer")
    public List<OrderResponse> getMyPurchases() {
        return orderService.getMyPurchases();
    }

    @GetMapping("/seller")
    public List<OrderResponse> getMySales() {
        return orderService.getMySales();
    }
}
