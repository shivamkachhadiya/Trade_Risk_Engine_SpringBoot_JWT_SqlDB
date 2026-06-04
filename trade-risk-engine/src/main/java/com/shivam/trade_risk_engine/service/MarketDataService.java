package com.shivam.trade_risk_engine.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarketDataService {
    private final RestTemplate restTemplate;

    @Value("${finnhub.api.key}")
    private String apiKey;
    @Value("${finnhub.api.url}")
    private String apiUrl;

    public BigDecimal getLivePrice(String symbol) {
        try {
            // URL banega: https://finnhub.io/api/v1/quote?symbol=AAPL&token=d8gmom...
            String url = apiUrl + symbol + "&token=" + apiKey;

            // RestTemplate = Spring Boot ka Postman. Ye seedha API ko hit karega.
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.get("c") != null) {
                // "c" tag mein current price hota hai. Usko BigDecimal mein convert kiya.
                String priceStr = response.get("c").toString();
                BigDecimal livePrice = new BigDecimal(priceStr);

                // Agar price 0.0 hai, iska matlab symbol galat hai
                if (livePrice.compareTo(BigDecimal.ZERO) > 0) {
                    return livePrice;
                }
            }
        } catch (Exception e) {
            System.out.println("Market API Call Failed: " + e.getMessage());
        }
        return null; // Agar kuch fail hua toh null bhej do
    }
}