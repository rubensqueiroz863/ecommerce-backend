package com.rubens.ecommerce_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.LastSearchHistoryDTO;
import com.rubens.ecommerce_backend.dto.SearchDTO;
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

    private final SearchRepository searchHistoryRepository;
    private final UserRepository userRepository;
    private final SearchActivityLogRepository searchActivityLogRepository;

    public List<LastSearchHistoryDTO> getLastSearches(String userId) {
        return searchHistoryRepository
                .findTop5ByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(s -> new LastSearchHistoryDTO(s.getQuery()))
                .toList();
    }

    public SearchDTO createSearch(String query, String email, String performedBy) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        Search search = searchHistoryRepository
            .findByUserIdAndQuery(user.getId(), query)
            .orElse(null);

        if (search != null) {
            search.setCreatedAt(LocalDateTime.now());
        } else {
            search = new Search(user, query);
        }

        searchHistoryRepository.save(search);

        searchActivityLogRepository.save(SearchActivityLog.builder()
                .searchId(search.getId())
                .performedBy("system")
                .action("CREATE")
                .details("Pesquisa: " + search.getQuery() + " Criada pelo user: " + search.getUser().getId())
                .timestamp(LocalDateTime.now())
                .build()
        );

        return new SearchDTO(
            search.getId(),
            user.getId()
        );
    }
}