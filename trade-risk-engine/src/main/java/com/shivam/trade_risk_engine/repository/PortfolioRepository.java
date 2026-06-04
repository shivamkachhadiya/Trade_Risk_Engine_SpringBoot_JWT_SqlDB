package com.shivam.trade_risk_engine.repository;

import com.shivam.trade_risk_engine.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // Custom SQL Query: "Bhai, check karke bata kya is user ke paas pehle se ye share hai?"
    Optional<Portfolio> findByUserIdAndSymbol(Long userId, String symbol);

    List<Portfolio> findByUserId(Long userId);
}

//
//Tune ek bohot smart function banaya hai: findByUserIdAndSymbol.
//Jab bhi naya order aayega, humara system is clerk se puchega: "Kya User 1 ke paas pehle se RELIANCE ke shares hain?"
//        * Agar HAAN, toh hum existing quantity mein naye shares (+) kar denge.