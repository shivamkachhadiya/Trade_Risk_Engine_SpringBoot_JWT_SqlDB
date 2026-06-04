package com.shivam.trade_risk_engine.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long userId; // Kis user ka portfolio hai?

    @Column(nullable = false)
    private String symbol; // Kaunsa share hai? (Jaise: RELIANCE, TCS)

    @Column(nullable = false)
    private Integer totalQuantity; // Kitne shares pade hain?
}


//Tere paas ek simple table banegi jisme likha hoga ki User ID 1 ke paas TCS ke 10 shares hain.