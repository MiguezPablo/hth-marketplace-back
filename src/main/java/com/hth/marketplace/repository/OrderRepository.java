package com.hth.marketplace.repository;

import com.hth.marketplace.model.Order;
import com.hth.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByBuyerOrderByCreatedAtDesc(User buyer);
    List<Order> findBySellerOrderByCreatedAtDesc(User seller);
}
