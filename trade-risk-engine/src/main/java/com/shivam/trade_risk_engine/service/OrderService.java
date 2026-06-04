package com.shivam.trade_risk_engine.service;

import com.shivam.trade_risk_engine.dto.OrderRequest;
import com.shivam.trade_risk_engine.model.Order;
import com.shivam.trade_risk_engine.model.OrderType; // Naya Import!
import com.shivam.trade_risk_engine.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal; // Naya Import!
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final RiskEngineService riskEngineService;
    private final MarketDataService marketDataService; // 1. Naya API Scout bula liya!

    public String placeOrder(OrderRequest request) {

        // Default price wahi hai jo user ne bheja (LIMIT orders ke liye)
        BigDecimal executionPrice = request.getPrice();

        //  THE ANTI-CHEAT LOGIC
        // Agar order MARKET hai, toh user ka price mat maano! Live API se check karo.
        if (request.getOrderType() == OrderType.MARKET) {

            // API Scout ko bola: Jaa aur Live Price le kar aa
            BigDecimal livePrice = marketDataService.getLivePrice(request.getSymbol());

            if (livePrice != null) {
                executionPrice = livePrice; // Hacker ka price cut, Real price set!
            } else {
                // Agar Finnhub fail ho gaya ya symbol galat hai, toh order wahin rok do
                return "Error: Could not fetch live market price for " + request.getSymbol() + ". Please check the symbol.";
            }
        }

        // 2. Factory ko call kiya NAYE verified executionPrice ke sath
        Order newOrder = orderFactory.createOrder(
                request.getUserId(),
                request.getSymbol(),
                request.getOrderType(),
                request.getQuantity(),
                executionPrice, // Ab yahan postman ka nahi, balki checked price ja raha hai!
                request.getTradeSide()
        );

        // 3. Database clerk ko bol kar order table mein save karwa diya
        orderRepository.save(newOrder);

        // 4. Risk Engine ko order check aur process karne bheja
        riskEngineService.validateAndProcessOrder(newOrder);

        // 5. Final status return kiya (sath mein execution price bhi dikhaya)
        return "Order processed! Final Status: " + newOrder.getStatus() + " at Price: ₹" + executionPrice;
    }

    // Naya Function: User ki order history dekhne ke liye
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}