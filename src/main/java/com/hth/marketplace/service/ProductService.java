package com.hth.marketplace.service;

import com.hth.marketplace.dto.CreateProductRequest;
import com.hth.marketplace.dto.ProductResponse;
import com.hth.marketplace.dto.SellerResponse;
import com.hth.marketplace.dto.UpdateProductRequest;
import com.hth.marketplace.exception.NotFoundException;
import com.hth.marketplace.exception.UnauthorizedException;
import com.hth.marketplace.model.Product;
import com.hth.marketplace.model.ProductStatus;
import com.hth.marketplace.model.ProductType;
import com.hth.marketplace.model.User;
import com.hth.marketplace.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllActiveProducts() {
        return productRepository.findByStatusOrderByCreatedAtDesc(ProductStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse getProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return toResponse(product);
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        User seller = getCurrentUser();

        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setType(ProductType.valueOf(request.getType().toUpperCase()));
        product.setStatus(ProductStatus.ACTIVE);
        product.setSeller(seller);

        return toResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(UUID id, UpdateProductRequest request) {
        User current = getCurrentUser();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (!product.getSeller().getId().equals(current.getId())) {
            throw new UnauthorizedException("Access denied");
        }

        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setType(ProductType.valueOf(request.getType().toUpperCase()));
        product.setStatus(ProductStatus.valueOf(request.getStatus().toUpperCase()));

        return toResponse(productRepository.save(product));
    }

    public void deleteProduct(UUID id) {
        User current = getCurrentUser();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (!product.getSeller().getId().equals(current.getId())) {
            throw new UnauthorizedException("Access denied");
        }

        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    public List<ProductResponse> getMyProducts() {
        User current = getCurrentUser();
        return productRepository.findBySellerOrderByCreatedAtDesc(current)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductResponse> getProductsBySellerAlias(String alias) {
        return productRepository.findBySellerAliasAndStatusOrderByCreatedAtDesc(alias, ProductStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getType(),
                product.getStatus(),
                toSellerResponse(product.getSeller()),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }

    private SellerResponse toSellerResponse(User user) {
        return new SellerResponse(user.getId(), user.getName(), user.getAlias(), user.getEmail());
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        throw new UnauthorizedException("User not authenticated");
    }
}
