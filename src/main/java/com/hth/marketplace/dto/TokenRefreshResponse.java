package com.hth.marketplace.dto;

import lombok.Getter;

@Getter
public class TokenRefreshResponse {

    private final String accessToken;
    private final String refreshToken;
    private final String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
