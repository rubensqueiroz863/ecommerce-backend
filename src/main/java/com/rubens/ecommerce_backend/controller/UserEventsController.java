package com.rubens.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rubens.ecommerce_backend.dto.ClickEventDTO;
import com.rubens.ecommerce_backend.dto.ClickRequestDTO;
import com.rubens.ecommerce_backend.service.ClickEventService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}