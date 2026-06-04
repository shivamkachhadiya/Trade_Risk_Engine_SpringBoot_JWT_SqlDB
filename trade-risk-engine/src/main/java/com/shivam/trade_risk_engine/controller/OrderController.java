package com.shivam.trade_risk_engine.controller;

import com.shivam.trade_risk_engine.dto.OrderRequest;
import com.shivam.trade_risk_engine.model.Order;
import com.shivam.trade_risk_engine.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // POST http://localhost:8080/api/orders
    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest request) {
        // Receptionist ne Manager (OrderService) ko raw data de diya
        String responseMessage = orderService.placeOrder(request);

        // Jo message wahan se aaya, wo customer (Postman) ko wapas de diya
        return ResponseEntity.ok(responseMessage);
    }

    // GET http://localhost:8080/api/orders/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }
}