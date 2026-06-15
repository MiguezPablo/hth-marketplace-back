package com.hth.marketplace.controller;

import com.hth.marketplace.dto.CreateProductRequest;
import com.hth.marketplace.dto.ProductResponse;
import com.hth.marketplace.dto.UpdateProductRequest;
import com.hth.marketplace.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/marketplace/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllActiveProducts();
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable UUID id) {
        return productService.getProduct(id);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my/listings")
    public List<ProductResponse> getMyProducts() {
        return productService.getMyProducts();
    }

    @GetMapping("/seller/{alias}")
    public List<ProductResponse> getProductsBySeller(@PathVariable String alias) {
        return productService.getProductsBySellerAlias(alias);
    }
}
