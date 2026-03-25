package com.rubens.ecommerce_backend.controller;

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
    public ClickEventDTO createClick(@RequestBody ClickRequestDTO request) {
        return clickEventService.createClick(
            request.productId(),
            request.userEmail()
        );
    }

    // Funcionando
    @GetMapping("/analytics/clicks/monthly")
    public List<ClicksPerMonthDTO> getMonthlyClicks() {
        return clickEventService.getClicksPerMonthCurrentYear();
    }

    // Funcionando
    @GetMapping("/analytics/products/monthly")
    public List<ClicksPerProductPerMonthDTO> getMonthlyClicksByProduct() {
        return clickEventService.getClicksPerProductPerMonthCurrentYear();
    }

    // Funcionando
    @GetMapping("/recommendations/products")
    public List<ProductRecommendationGroupDTO> getProductRecommendations() {
        return clickEventService.getAllRecommendations();
    }

    // Funcionando
    @GetMapping("/users/{userId}/top-clicks")
    public List<MostClickedProductDTO> getUserTopClickedProducts(
            @PathVariable("userId") String userId,
            @RequestParam(name = "limit", defaultValue = "10") int limit) {

        return clickEventService.getMostClickedProductsByUser(userId, limit);
    }

    // Funcionando
    @GetMapping("/users/{userId}/recommendations")
    public UserRecommendationGroupDTO getUserRecommendations(
            @PathVariable("userId") String userId
    ) {
        return clickEventService.getRecommendationsForUser(userId);
    }
}