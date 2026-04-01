package com.rubens.ecommerce_backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.model.EventActivityLog;
import com.rubens.ecommerce_backend.model.ProductActivityLog;
import com.rubens.ecommerce_backend.model.SearchActivityLog;
import com.rubens.ecommerce_backend.model.UserActivityLog;
import com.rubens.ecommerce_backend.repository.EventActivityLogRepository;
import com.rubens.ecommerce_backend.repository.ProductActivityLogRepository;
import com.rubens.ecommerce_backend.repository.SearchActivityLogRepository;
import com.rubens.ecommerce_backend.repository.UserActivityLogRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogsController {

    private final UserActivityLogRepository userActivityLogRepository;
    private final ProductActivityLogRepository productActivityLogRepository;
    private final EventActivityLogRepository eventActivityLogRepository;
    private final SearchActivityLogRepository searchActivityLogRepository;

    // Funcionando
    @GetMapping("/users")
    public Page<UserActivityLog> getAllUsersLogs(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userActivityLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    // Funcionando
    @GetMapping("/products")
    public Page<ProductActivityLog> getAllProductsLogs(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productActivityLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    // Funcionando
    @GetMapping("/events")
    public Page<EventActivityLog> getAllEventsLogs(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return eventActivityLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    // Funcionando
    @GetMapping("/searchs")
    public Page<SearchActivityLog> getAllSearchsLogs(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return searchActivityLogRepository.findAllByOrderByTimestampDesc(pageable);
    }
}