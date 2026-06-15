package com.hth.marketplace.dto;

import com.hth.marketplace.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderResponse {
    private UUID id;
    private SellerResponse buyer;
    private SellerResponse seller;
    private ProductResponse product;
    private BigDecimal amount;
    private UUID transactionId;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
