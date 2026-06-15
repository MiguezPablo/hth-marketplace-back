package com.hth.marketplace.repository;

import com.hth.marketplace.model.RefreshToken;
import com.hth.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteAllByUser(User user);
}
