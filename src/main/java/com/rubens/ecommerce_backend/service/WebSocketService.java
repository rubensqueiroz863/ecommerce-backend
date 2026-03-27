package com.rubens.ecommerce_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    private final RestTemplate restTemplate;

    @Autowired
    public WebSocketService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public void notify(String id, Object message) {
        Map<String, Object> body = new HashMap<>();
        body.put("id", id);
        body.put("message", message);

        try {
            restTemplate.postForObject(
                    "https://ecommerce-websocket.onrender.com/api/notify",
                    body,
                    String.class
            );
            logger.info("Notificação enviada para WebSocket: id={}", id);
        } catch (RestClientException e) {
            logger.error("Falha ao enviar notificação para WebSocket: id={}, erro={}", id, e.getMessage());
        }
    }
}