package com.hth.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SellerResponse {
    private UUID id;
    private String name;
    private String alias;
    private String email;
}
