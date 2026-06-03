package com.shivam.trade_risk_engine.service;


import com.shivam.trade_risk_engine.model.Order;
import com.shivam.trade_risk_engine.model.OrderStatus;
import com.shivam.trade_risk_engine.model.OrderType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class OrderFactory {

    public Order createOrder(Long userId, String symbol,
                             OrderType type, Integer quantity, BigDecimal price) {
        Order order = new Order();
        order.setUserId(userId);
        order.setSymbol(symbol);
        order.setOrderType(type);
        order.setQuantity(quantity);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        switch (type) {
            case MARKET -> order.setPrice(price);
            case LIMIT  -> order.setPrice(price);
            case STOP   -> order.setPrice(price);
        }

        return order;
    }
}
