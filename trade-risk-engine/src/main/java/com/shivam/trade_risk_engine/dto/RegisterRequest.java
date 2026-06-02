package com.shivam.trade_risk_engine.dto;

import com.shivam.trade_risk_engine.model.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private java.math.BigDecimal availableBalance;
}
