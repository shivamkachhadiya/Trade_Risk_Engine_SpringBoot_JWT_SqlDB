package com.shivam.trade_risk_engine.repository;

import com.shivam.trade_risk_engine.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query: Apne aap banegi 'SELECT * FROM orders WHERE user_id = ?'
    List<Order> findByUserId(Long userId);
}
//
//
//Isme koi annotation (jaise @Repository) lagane ki zaroorat nahi hai, kyunki JpaRepository ko extend karte hi Spring ise automatically detect kar leta hai.
//
//JpaRepository<Order, Long> ka matlab hai: Ise Order entity manage karni hai, aur uski Primary Key (id) ka type Long hai.