package com.rubens.ecommerce_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void notify(String userId, Object message) {

        Map<String, Object> body = new HashMap<>();
        body.put("message", message);

        restTemplate.postForObject(
            "https://ecommerce-websocket.onrender.com/api/notify",
            body,
            String.class
        );
    }
}