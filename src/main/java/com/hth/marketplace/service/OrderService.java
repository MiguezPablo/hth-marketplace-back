package com.hth.marketplace.service;

import com.hth.marketplace.dto.CreateOrderRequest;
import com.hth.marketplace.dto.OrderResponse;
import com.hth.marketplace.dto.ProductResponse;
import com.hth.marketplace.dto.SellerResponse;
import com.hth.marketplace.exception.BadRequestException;
import com.hth.marketplace.exception.NotFoundException;
import com.hth.marketplace.exception.UnauthorizedException;
import com.hth.marketplace.model.Order;
import com.hth.marketplace.model.OrderStatus;
import com.hth.marketplace.model.Product;
import com.hth.marketplace.model.ProductStatus;
import com.hth.marketplace.model.User;
import com.hth.marketplace.repository.OrderRepository;
import com.hth.marketplace.repository.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        User buyer = getCurrentUser();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new BadRequestException("Product is not available");
        }

        User seller = product.getSeller();
        if (buyer.getId().equals(seller.getId())) {
            throw new BadRequestException("You cannot buy your own product");
        }

        Order order = new Order();
        order.setBuyer(buyer);
        order.setSeller(seller);
        order.setProduct(product);
        order.setAmount(product.getPrice());
        order.setStatus(OrderStatus.CONFIRMED);

        product.setStatus(ProductStatus.SOLD);
        productRepository.save(product);

        return toResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getMyPurchases() {
        User buyer = getCurrentUser();
        return orderRepository.findByBuyerOrderByCreatedAtDesc(buyer)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<OrderResponse> getMySales() {
        User seller = getCurrentUser();
        return orderRepository.findBySellerOrderByCreatedAtDesc(seller)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                toSellerResponse(order.getBuyer()),
                toSellerResponse(order.getSeller()),
                toProductResponse(order.getProduct()),
                order.getAmount(),
                null,
                order.getStatus(),
                order.getCreatedAt());
    }

    private SellerResponse toSellerResponse(User user) {
        return new SellerResponse(user.getId(), user.getName(), user.getAlias(), user.getEmail());
    }

    private ProductResponse toProductResponse(Product product) {
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

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        throw new UnauthorizedException("User not authenticated");
    }
}
