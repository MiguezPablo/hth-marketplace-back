package com.hth.marketplace.repository;

import com.hth.marketplace.model.Product;
import com.hth.marketplace.model.ProductStatus;
import com.hth.marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByStatusOrderByCreatedAtDesc(ProductStatus status);
    List<Product> findBySellerOrderByCreatedAtDesc(User seller);
    List<Product> findBySellerAliasAndStatusOrderByCreatedAtDesc(String alias, ProductStatus status);
}
