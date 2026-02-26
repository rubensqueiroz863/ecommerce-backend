package com.rubens.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rubens.ecommerce_backend.dto.ClickEventDTO;
import com.rubens.ecommerce_backend.dto.ClickRequestDTO;
import com.rubens.ecommerce_backend.dto.MostClickedProductDTO;
import com.rubens.ecommerce_backend.service.ClickEventService;
import org.springframework.security.core.Authentication;

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

    @GetMapping("/analytics/users/{userId}/most-clicked")
    public List<MostClickedProductDTO> getMostClickedProductsByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit) {

        return clickEventService.getMostClickedProductsByUser(userId, limit);
    }
    
}