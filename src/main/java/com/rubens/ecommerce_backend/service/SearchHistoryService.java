package com.rubens.ecommerce_backend.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.ClickEventDTO;
import com.rubens.ecommerce_backend.dto.SearchHistoryDTO;
import com.rubens.ecommerce_backend.dto.SearchRequestDTO;
import com.rubens.ecommerce_backend.model.ClickEvent;
import com.rubens.ecommerce_backend.model.SearchHistory;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.repository.SearchHistoryRepository;
import com.rubens.ecommerce_backend.repository.UserRepository;

@Service
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository, UserRepository userRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
        this.userRepository = userRepository;
    }

    public List<SearchHistory> getLastSearches(String userId) {
        return searchHistoryRepository
                .findTop5ByUserIdOrderByCreatedAtDesc(userId);
    }

    public SearchHistoryDTO registerSearch(String query, String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        SearchHistory search = new SearchHistory(user, query);

        SearchHistory savedSearch = searchHistoryRepository.save(search);

        return new SearchHistoryDTO(
            savedSearch.getId(),
            user.getId()
        );
    }
}