package com.rubens.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rubens.ecommerce_backend.dto.LastSearchHistoryDTO;
import com.rubens.ecommerce_backend.dto.SearchHistoryDTO;
import com.rubens.ecommerce_backend.dto.SearchRequestDTO;
import com.rubens.ecommerce_backend.service.SearchHistoryService;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/search-history")
public class SearchHistoryController  {

    private final SearchHistoryService searchHistoryService;

    public SearchHistoryController(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    @PostMapping("/search")
    public SearchHistoryDTO registerSearch(@RequestBody SearchRequestDTO request) {

        return searchHistoryService.registerSearch(
            request.getQuery(),
            request.getUserEmail()
        );
    }

    @GetMapping("/last/{userId}")
    public List<LastSearchHistoryDTO> getLastSearches(@PathVariable String userId) {
        return searchHistoryService.getLastSearches(userId);
    }
}
