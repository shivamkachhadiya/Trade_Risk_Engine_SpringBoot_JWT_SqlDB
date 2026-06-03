package com.shivam.trade_risk_engine.service;

import com.shivam.trade_risk_engine.dto.OrderRequest;
import com.shivam.trade_risk_engine.model.Order;
import com.shivam.trade_risk_engine.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;

    public String placeOrder(OrderRequest request) {
        // 1. Tere strict parameter wale design ke hisaab se Factory ko call kiya
        Order newOrder = orderFactory.createOrder(
                request.getUserId(),
                request.getSymbol(),
                request.getOrderType(),
                request.getQuantity(),
                request.getPrice()
        );

        // 2. Database clerk ko bol kar order table mein save karwa diya
        orderRepository.save(newOrder);

        return "Order placed successfully! Order ID: " + newOrder.getId() + " is PENDING.";
    }
}