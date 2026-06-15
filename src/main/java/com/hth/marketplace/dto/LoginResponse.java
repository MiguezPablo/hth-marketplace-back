package com.hth.marketplace.dto;

import com.hth.marketplace.model.User;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class LoginResponse {

    private final String accessToken;
    private final String refreshToken;
    private final String tokenType = "Bearer";
    private final UserDto user;

    public LoginResponse(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = new UserDto(user);
    }

    @Getter
    public static class UserDto {
        private final UUID id;
        private final String name;
        private final String email;
        private final String alias;
        private final BigDecimal balance;

        public UserDto(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
            this.alias = user.getAlias();
            this.balance = user.getBalance();
        }
    }
}
