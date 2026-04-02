package com.rubens.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rubens.ecommerce_backend.dto.LastSearchHistoryDTO;
import com.rubens.ecommerce_backend.dto.SearchDTO;
import com.rubens.ecommerce_backend.dto.SearchRequestDTO;
import com.rubens.ecommerce_backend.service.SearchService;
import com.rubens.ecommerce_backend.service.WebSocketService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/searches")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final WebSocketService webSocketService;

    @PostMapping
    public ResponseEntity<SearchDTO> createSearch(@RequestBody SearchRequestDTO request) {

        SearchDTO savedSearch = searchService.createSearch(
                request.getQuery(),
                request.getUserEmail(),
                "system"
        );

        // 🔹 websocket não pode quebrar resposta
        try {
            webSocketService.notify(savedSearch.id(), Map.of(
                    "type", "SEARCH_CREATED",
                    "search", savedSearch
            ));
        } catch (Exception e) {
            System.err.println("Erro ao notificar websocket: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSearch);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<LastSearchHistoryDTO>> getLastSearches(
            @PathVariable("userId") String userId
    ) {
        return ResponseEntity.ok(
                searchService.getLastSearches(userId)
        );
    }
}
