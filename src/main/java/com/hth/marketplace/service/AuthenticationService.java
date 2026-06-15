package com.hth.marketplace.service;

import com.hth.marketplace.dto.AuthResponse;
import com.hth.marketplace.dto.LoginRequest;
import com.hth.marketplace.dto.LoginResponse;
import com.hth.marketplace.dto.RegisterRequest;
import com.hth.marketplace.dto.TokenRefreshRequest;
import com.hth.marketplace.dto.TokenRefreshResponse;
import com.hth.marketplace.exception.BadRequestException;
import com.hth.marketplace.exception.UnauthorizedException;
import com.hth.marketplace.model.RefreshToken;
import com.hth.marketplace.model.User;
import com.hth.marketplace.repository.RefreshTokenRepository;
import com.hth.marketplace.repository.UserRepository;
import com.hth.marketplace.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${app.jwtRefreshExpirationMs}")
    private long refreshExpirationMs;

    public AuthenticationService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        String alias = request.getAlias().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already registered");
        }
        if (userRepository.existsByAlias(alias)) {
            throw new BadRequestException("Alias already registered");
        }

        User user = new User();
        user.setEmail(email);
        user.setName(request.getName().trim());
        user.setAlias(alias);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBalance(BigDecimal.ZERO);
        userRepository.save(user);

        return new AuthResponse(jwtUtil.generateToken(user.getId()));
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        refreshTokenRepository.deleteAllByUser(user);

        String accessToken = jwtUtil.generateToken(user.getId());
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
        refreshTokenRepository.save(refreshToken);

        return new LoginResponse(accessToken, refreshToken.getToken(), user);
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        if (request.getRefreshToken() == null || request.getRefreshToken().isBlank()) {
            throw new UnauthorizedException("Refresh token is required");
        }

        RefreshToken existingToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Refresh token not found"));

        if (existingToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(existingToken);
            throw new UnauthorizedException("Refresh token expired");
        }

        User user = existingToken.getUser();
        refreshTokenRepository.delete(existingToken);

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUser(user);
        newRefreshToken.setToken(UUID.randomUUID().toString());
        newRefreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationMs));
        refreshTokenRepository.save(newRefreshToken);

        return new TokenRefreshResponse(jwtUtil.generateToken(user.getId()), newRefreshToken.getToken());
    }
}
