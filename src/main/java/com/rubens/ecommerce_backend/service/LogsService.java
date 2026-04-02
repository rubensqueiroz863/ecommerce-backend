package com.rubens.ecommerce_backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.model.EventActivityLog;
import com.rubens.ecommerce_backend.model.ProductActivityLog;
import com.rubens.ecommerce_backend.model.SearchActivityLog;
import com.rubens.ecommerce_backend.model.UserActivityLog;
import com.rubens.ecommerce_backend.repository.EventActivityLogRepository;
import com.rubens.ecommerce_backend.repository.ProductActivityLogRepository;
import com.rubens.ecommerce_backend.repository.SearchActivityLogRepository;
import com.rubens.ecommerce_backend.repository.UserActivityLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogsService {

    private final UserActivityLogRepository userActivityLogRepository;
    private final ProductActivityLogRepository productActivityLogRepository;
    private final EventActivityLogRepository eventActivityLogRepository;
    private final SearchActivityLogRepository searchActivityLogRepository;

    public Page<UserActivityLog> getUserLogs(Pageable pageable) {
        return userActivityLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    public Page<ProductActivityLog> getProductLogs(Pageable pageable) {
        return productActivityLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    public Page<EventActivityLog> getEventLogs(Pageable pageable) {
        return eventActivityLogRepository.findAllByOrderByTimestampDesc(pageable);
    }

    public Page<SearchActivityLog> getSearchLogs(Pageable pageable) {
        return searchActivityLogRepository.findAllByOrderByTimestampDesc(pageable);
    }
}