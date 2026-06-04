package com.shivam.trade_risk_engine.service;

import com.shivam.trade_risk_engine.model.Order;
import com.shivam.trade_risk_engine.model.OrderStatus;
import com.shivam.trade_risk_engine.model.Portfolio;
import com.shivam.trade_risk_engine.model.TradeSide; // <-- Naya import joda hai!
import com.shivam.trade_risk_engine.model.User;
import com.shivam.trade_risk_engine.repository.OrderRepository;
import com.shivam.trade_risk_engine.repository.PortfolioRepository;
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
    private final PortfolioRepository portfolioRepository; // 1. Portfolio wale clerk ko bula liya

    @Transactional // Ye guarantee deta hai ki koi data aadha save nahi hoga
    public void validateAndProcessOrder(Order order){
        // 1. Database se user ka current balance nikalo
        // Ab hum User ko DB level par lock karke laa rahe hain taaki koi Race Condition na ho
        User user = userRepository.findByIdForUpdate(order.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Order ki total cost nikalo (Price * Quantity)
        BigDecimal totalCost=order.getPrice().multiply(new BigDecimal(order.getQuantity()));

        // ==============================
        //  BUY LOGIC
        // ==============================
        if(order.getTradeSide() == TradeSide.BUY) {
            // 3. Risk Check (Balance >= Total Cost)
            if(user.getAvailableBalance().compareTo(totalCost)>=0){
                // Balance sufficient hai -> Paise kaato aur EXECUTED mark karo
                user.setAvailableBalance(user.getAvailableBalance().subtract(totalCost));
                order.setStatus(OrderStatus.EXECUTED);
                userRepository.save(user);

                //Portfolio Logic
                // Database se pucho: Kya is user ke paas ye share pehle se hai?
                Portfolio userPortfolio=portfolioRepository.findByUserIdAndSymbol(user.getId(),order.getSymbol())
                        .orElseGet(()->{
                            Portfolio newPortfolio=new Portfolio();
                            newPortfolio.setUserId(user.getId());
                            newPortfolio.setSymbol(order.getSymbol());
                            newPortfolio.setTotalQuantity(0);
                            return newPortfolio;
                        });
//            Dhyan se dekh, maine .orElseGet(...) use kiya hai. Ye Java ka ek bohot advanced aur clean tareeqa hai
//            if-else ko chota karne ka. Iska seedha matlab hai: "Database mein dhundho, agar mil jaye toh theek,
//            warna (orElse) ek naya object bana kar de do."
//            Interview mein ye sab cheezein bohot solid impression dalti hain.

                // Naye shares ko purane shares mein (+) kar do
                userPortfolio.setTotalQuantity(userPortfolio.getTotalQuantity()+order.getQuantity());

                // Portfolio clerk ko bolo ise save kar de
                portfolioRepository.save(userPortfolio);

            }else{
                // Balance kam hai -> REJECTED mark karo
                order.setStatus(OrderStatus.REJECTED);
            }
        }
        // ==============================
        //  SELL LOGIC (Naya Banaya Hai)
        // ==============================
        else if (order.getTradeSide() == TradeSide.SELL) {
            // Check karo kya iske paas share hai bhi?
            Portfolio userPortfolio = portfolioRepository.findByUserIdAndSymbol(user.getId(), order.getSymbol())
                    .orElse(null);

            // Agar share exist karta hai AUR quantity bechne jitni ya usse zyada hai
            if (userPortfolio != null && userPortfolio.getTotalQuantity() >= order.getQuantity()) {

                // Portfolio se shares kaato (-)
                userPortfolio.setTotalQuantity(userPortfolio.getTotalQuantity() - order.getQuantity());

                // Tijori (Balance) mein paise add karo (+)
                user.setAvailableBalance(user.getAvailableBalance().add(totalCost));

                order.setStatus(OrderStatus.EXECUTED);

                portfolioRepository.save(userPortfolio);
                userRepository.save(user);
            } else {
                // Shares nahi hain ya fir kam hain toh Reject karo
                order.setStatus(OrderStatus.REJECTED);
            }
        }

        // 4. Order ka final status save karo
        orderRepository.save(order);
    }
}