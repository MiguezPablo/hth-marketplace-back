package com.hth.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String description;

    @NotNull
    @Positive
    private BigDecimal price;

    private String imageUrl;

    @NotBlank
    private String type;

    @NotBlank
    private String status;
}
