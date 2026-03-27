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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/searchs")
@RequiredArgsConstructor
public class SearchController  {

    private final SearchService searchService;
    private final WebSocketService webSocketService;

    // Funcionando
    @PostMapping
    public SearchDTO createSearch(@RequestBody SearchRequestDTO request) {
        SearchDTO savedSearch = searchService.createSearch(request.getQuery(), request.getUserEmail(), "system");
        
        webSocketService.notify(savedSearch.id(), Map.of(
                "type", "SEARCH_CREATED",
                "search", savedSearch
        ));
        
        return savedSearch;
    }

    // Funcionando
    @GetMapping("/{id}")
    public List<LastSearchHistoryDTO> getLastSearches(@PathVariable("id") String id) {
        return searchService.getLastSearches(id);
    }
}
