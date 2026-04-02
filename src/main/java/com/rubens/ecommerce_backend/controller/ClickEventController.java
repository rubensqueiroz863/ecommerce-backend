package com.rubens.ecommerce_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.*;
import com.rubens.ecommerce_backend.service.ClickEventService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class ClickEventController {

    private final ClickEventService clickEventService;

    // Funcionando
    @PostMapping("/clicks")
    public ResponseEntity<ClickEventDTO> createClick(@RequestBody ClickRequestDTO request) {

        ClickEventDTO event = clickEventService.createClick(
            request.productId(),
            request.userEmail(),
            "system"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    // Funcionando
    @GetMapping("/analytics/clicks/monthly")
    public ResponseEntity<List<ClicksPerMonthDTO>> getMonthlyClicks() {
        return ResponseEntity.ok(clickEventService.getClicksPerMonthCurrentYear());
    }

    // Funcionando
    @GetMapping("/analytics/products/monthly")
    public ResponseEntity<List<ClicksPerProductPerMonthDTO>> getMonthlyClicksByProduct() {
        return ResponseEntity.ok(clickEventService.getClicksPerProductPerMonthCurrentYear());
    }

    // Funcionando
    @GetMapping("/recommendations/products")
    public ResponseEntity<List<ProductRecommendationGroupDTO>> getProductRecommendations() {
        return ResponseEntity.ok(clickEventService.getAllRecommendations());
    }

    // Funcionando
    @GetMapping("/users/{userId}/top-clicks")
    public ResponseEntity<List<MostClickedProductDTO>> getUserTopClickedProducts(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit) {

        return ResponseEntity.ok(
            clickEventService.getMostClickedProductsByUser(userId, limit)
        );
    }

    // Funcionando
    @GetMapping("/users/{userId}/recommendations")
    public ResponseEntity<UserRecommendationGroupDTO> getUserRecommendations(
            @PathVariable String userId
    ) {
        return ResponseEntity.ok(
            clickEventService.getRecommendationsForUser(userId)
        );
    }
}