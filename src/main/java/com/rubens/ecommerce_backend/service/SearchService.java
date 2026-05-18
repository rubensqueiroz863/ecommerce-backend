package com.rubens.ecommerce_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.LastSearchHistoryDTO;
import com.rubens.ecommerce_backend.dto.SearchDTO;
import com.rubens.ecommerce_backend.exception.InvalidSearchQueryException;
import com.rubens.ecommerce_backend.exception.SearchCreationException;
import com.rubens.ecommerce_backend.exception.UserNotFoundException;
import com.rubens.ecommerce_backend.model.SearchActivityLog;
import com.rubens.ecommerce_backend.model.Search;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.repository.SearchActivityLogRepository;
import com.rubens.ecommerce_backend.repository.SearchRepository;
import com.rubens.ecommerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;
    private final UserRepository userRepository;
    private final SearchActivityLogRepository searchActivityLogRepository;

    public List<LastSearchHistoryDTO> getLastSearches(String userId) {

        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be null or empty");
        }

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        return searchRepository
                .findTop5ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(s -> new LastSearchHistoryDTO(s.getQuery()))
                .toList();
    }

    public SearchDTO createSearch(String query, String email, String performedBy) {

        if (query == null || query.isBlank()) {
            throw new InvalidSearchQueryException("Query cannot be empty.");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);

        Search search = searchRepository
            .findByUserIdAndQuery(user.getId(), query)
            .orElse(null);

        if (search != null) {
            search.setCreatedAt(LocalDateTime.now());
        } else {
            search = new Search(user, query);
        }

        try {
            searchRepository.save(search);
        } catch (DataIntegrityViolationException e) {
            throw new SearchCreationException("Search cannot be created.");
        }

        try {
            searchActivityLogRepository.save(SearchActivityLog.builder()
                    .searchId(search.getId())
                    .performedBy(performedBy)
                    .action("CREATE")
                    .details("Pesquisa: " + search.getQuery()
                            + " criada pelo user: " + search.getUser().getId())
                    .timestamp(LocalDateTime.now())
                    .build()
            );
        } catch (Exception e) {
            System.err.println("Erro ao salvar log: " + e.getMessage());
        }

        return new SearchDTO(
            search.getId(),
            user.getId()
        );
    }
}