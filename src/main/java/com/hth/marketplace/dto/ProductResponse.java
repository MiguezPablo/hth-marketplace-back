package com.hth.marketplace.dto;

import com.hth.marketplace.model.ProductStatus;
import com.hth.marketplace.model.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private ProductType type;
    private ProductStatus status;
    private SellerResponse seller;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
