package com.rubens.ecommerce_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.LastSearchHistoryDTO;
import com.rubens.ecommerce_backend.dto.SearchHistoryDTO;
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

    public List<LastSearchHistoryDTO> getLastSearches(String userId) {
        return searchHistoryRepository
                .findTop5ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(s -> new LastSearchHistoryDTO(s.getQuery()))
                .toList();
    }

    public SearchHistoryDTO registerSearch(String query, String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        SearchHistory search = searchHistoryRepository
            .findByUserIdAndQuery(user.getId(), query)
            .orElse(null);

        if (search != null) {
            search.setCreatedAt(LocalDateTime.now());
        } else {
            search = new SearchHistory(user, query);
        }

        searchHistoryRepository.save(search);

        return new SearchHistoryDTO(
            search.getId(),
            user.getId()
        );
    }
}