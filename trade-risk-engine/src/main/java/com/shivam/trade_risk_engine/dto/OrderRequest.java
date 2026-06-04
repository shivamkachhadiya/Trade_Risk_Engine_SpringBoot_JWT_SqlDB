package com.shivam.trade_risk_engine.dto;

import com.shivam.trade_risk_engine.model.OrderType;
import com.shivam.trade_risk_engine.model.TradeSide;
import lombok.Data;
import java.math.BigDecimal;

@Data // <-- YE SABSE ZAROORI HAI
public class OrderRequest {
    private Long userId;
    private String symbol;
    private OrderType orderType;
    private Integer quantity;
    private BigDecimal price;
    // Baaki fields ke sath ye nayi field daal de
    private TradeSide tradeSide;
}