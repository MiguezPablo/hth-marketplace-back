package com.hth.marketplace.repository;

import com.hth.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAlias(String alias);
    boolean existsByEmail(String email);
    boolean existsByAlias(String alias);
}
