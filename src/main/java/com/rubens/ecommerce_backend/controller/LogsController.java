package com.rubens.ecommerce_backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.exception.InvalidLimitException;
import com.rubens.ecommerce_backend.exception.InvalidPageException;
import com.rubens.ecommerce_backend.model.EventActivityLog;
import com.rubens.ecommerce_backend.model.ProductActivityLog;
import com.rubens.ecommerce_backend.model.SearchActivityLog;
import com.rubens.ecommerce_backend.model.UserActivityLog;
import com.rubens.ecommerce_backend.service.LogsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogsController {

    private final LogsService logsService;

    private Pageable buildPageable(int page, int size) {
        if (page < 0) {
            throw new InvalidPageException("Page must be >= 0");
        }

        if (size <= 0 || size > 100) {
            throw new InvalidLimitException("Size must be between 1 and 100");
        }

        return PageRequest.of(page, size);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserActivityLog>> getAllUsersLogs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
            logsService.getUserLogs(buildPageable(page, size))
        );
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductActivityLog>> getAllProductsLogs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
            logsService.getProductLogs(buildPageable(page, size))
        );
    }

    @GetMapping("/events")
    public ResponseEntity<Page<EventActivityLog>> getAllEventsLogs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
            logsService.getEventLogs(buildPageable(page, size))
        );
    }

    @GetMapping("/searches")
    public ResponseEntity<Page<SearchActivityLog>> getAllSearchLogs(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
            logsService.getSearchLogs(buildPageable(page, size))
        );
    }
}