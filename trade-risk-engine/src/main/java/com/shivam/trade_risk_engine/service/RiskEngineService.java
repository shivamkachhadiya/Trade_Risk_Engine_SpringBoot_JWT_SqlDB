package com.shivam.trade_risk_engine.service;

import com.shivam.trade_risk_engine.model.Order;
import com.shivam.trade_risk_engine.model.OrderStatus;
import com.shivam.trade_risk_engine.model.User;
import com.shivam.trade_risk_engine.repository.OrderRepository;
import com.shivam.trade_risk_engine.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RiskEngineService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Transactional // Ye guarantee deta hai ki koi data aadha save nahi hoga
    public void validateAndProcessOrder(Order order){
        // 1. Database se user ka current balance nikalo
        User user=userRepository.findById(order.getUserId()).orElseThrow(()->new RuntimeException("User not found"));

        // 2. Order ki total cost nikalo (Price * Quantity)
        BigDecimal totalCost=order.getPrice().multiply(new BigDecimal(order.getQuantity()));

        // 3. Risk Check (Balance >= Total Cost)
        if(user.getAvailableBalance().compareTo(totalCost)>=0){
            // Balance sufficient hai -> Paise kaato aur EXECUTED mark karo
            user.setAvailableBalance(user.getAvailableBalance().subtract(totalCost));
            order.setStatus(OrderStatus.EXECUTED);
            userRepository.save(user);
        }else{
            // Balance kam hai -> REJECTED mark karo
            order.setStatus(OrderStatus.REJECTED);
        }
        // 4. Order ka final status save karo
        orderRepository.save(order);
    }
}
