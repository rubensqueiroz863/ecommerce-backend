package com.rubens.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rubens.ecommerce_backend.dto.ClickEventDTO;
import com.rubens.ecommerce_backend.dto.ClickRequestDTO;
import com.rubens.ecommerce_backend.dto.ClicksPerMonthDTO;
import com.rubens.ecommerce_backend.dto.ClicksPerProductPerMonthDTO;
import com.rubens.ecommerce_backend.dto.MostClickedProductDTO;
import com.rubens.ecommerce_backend.dto.ProductRecommendationDTO;
import com.rubens.ecommerce_backend.dto.ProductRecommendationGroupDTO;
import com.rubens.ecommerce_backend.dto.UserRecommendationGroupDTO;
import com.rubens.ecommerce_backend.service.ClickEventService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class UserEventsController {

    private final ClickEventService clickEventService;

    @PostMapping("/click")
    public ClickEventDTO registerClickEvent(@RequestBody ClickRequestDTO request) {

        return clickEventService.registerClick(
            request.productId(),
            request.userEmail()
        );
    }

    @GetMapping("/all-clicks/monthly")
    public List<ClicksPerMonthDTO> getClicksPerMonthCurrentYear() {
        return clickEventService.getClicksPerMonthCurrentYear();
    }

    @GetMapping("/all-clicks/products/monthly")
    public List<ClicksPerProductPerMonthDTO> getClicksPerProductPerMonthCurrentYear() {
        return clickEventService.getClicksPerProductPerMonthCurrentYear();
    }

    @GetMapping("/recommendations")
    public List<ProductRecommendationGroupDTO> getRecommendationsByClick() {
        return clickEventService.getAllRecommendations();
    }

    @GetMapping("/analytics/users/{userId}/most-clicked")
    public List<MostClickedProductDTO> getMostClickedProductsByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit) {

        return clickEventService.getMostClickedProductsByUser(userId, limit);
    }

    @GetMapping("/user/recommendations/{userId}")
    public UserRecommendationGroupDTO getRecommendationsForUser(
        @PathVariable String userId
    ) {
        return clickEventService.getRecommendationsForUser(userId);
    }
}