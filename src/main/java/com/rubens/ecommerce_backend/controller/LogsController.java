package com.rubens.ecommerce_backend.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.rubens.ecommerce_backend.model.UserActivityLog;
import com.rubens.ecommerce_backend.repository.UserActivityLogRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogsController {

    private final UserActivityLogRepository logRepository;

    // Funcionando
    @GetMapping("/users")
    public List<UserActivityLog> getAllLogs() {
        return logRepository.findAllByOrderByTimestampDesc();
    }
}