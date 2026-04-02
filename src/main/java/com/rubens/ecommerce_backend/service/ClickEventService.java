package com.rubens.ecommerce_backend.service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.ClickEventDTO;
import com.rubens.ecommerce_backend.dto.ClicksPerMonthDTO;
import com.rubens.ecommerce_backend.dto.ClicksPerProductPerMonthDTO;
import com.rubens.ecommerce_backend.dto.MostClickedProductDTO;
import com.rubens.ecommerce_backend.dto.ProductRecommendationDTO;
import com.rubens.ecommerce_backend.dto.ProductRecommendationGroupDTO;
import com.rubens.ecommerce_backend.dto.UserRecommendationGroupDTO;
import com.rubens.ecommerce_backend.exception.ClickEventCreationException;
import com.rubens.ecommerce_backend.exception.InvalidLimitException;
import com.rubens.ecommerce_backend.exception.ProductNotFoundException;
import com.rubens.ecommerce_backend.exception.UserNotFoundException;
import com.rubens.ecommerce_backend.model.ClickEvent;
import com.rubens.ecommerce_backend.model.EventActivityLog;
import com.rubens.ecommerce_backend.model.Product;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.repository.ClickEventRepository;
import com.rubens.ecommerce_backend.repository.EventActivityLogRepository;
import com.rubens.ecommerce_backend.repository.ProductRepository;
import com.rubens.ecommerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClickEventService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ClickEventRepository clickEventRepository;
    private final EventActivityLogRepository eventActivityLogRepository;

    public ClickEventDTO createClick(String productId, String email, String performedBy) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        ClickEvent event = new ClickEvent(user, product);

        ClickEvent savedEvent;
        try {
            savedEvent = clickEventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new ClickEventCreationException("Click cannot be created.");
        }

        try {
            eventActivityLogRepository.save(EventActivityLog.builder()
                    .eventId(savedEvent.getId())
                    .performedBy(performedBy)
                    .action("CREATE")
                    .details("Evento de click criado pelo user: "
                            + savedEvent.getUser().getId()
                            + " ao produto: "
                            + savedEvent.getProduct().getId())
                    .timestamp(LocalDateTime.now())
                    .build()
            );
        } catch (Exception logError) {
            System.err.println("Erro ao salvar log: " + logError.getMessage());
        }

        return new ClickEventDTO(
            savedEvent.getId(),
            savedEvent.getUser(),
            savedEvent.getProduct()
        );
    }

    public List<MostClickedProductDTO> getMostClickedProductsByUser(String userId, int limit) {

        if (limit <= 0 || limit > 100) {
            throw new InvalidLimitException();
        }

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        Pageable pageable = PageRequest.of(0, limit);

        List<Object[]> results = clickEventRepository
                .findMostClickedProductsByUser(userId, pageable);

        return results.stream()
                .map(r -> new MostClickedProductDTO(
                        (Product) r[0],
                        ((Long) r[1])
                ))
                .toList();
    }

    public List<ClicksPerMonthDTO> getClicksPerMonthCurrentYear() {

        Year year = Year.now();

        LocalDateTime start = year.atDay(1).atStartOfDay();
        LocalDateTime end = year.plusYears(1).atDay(1).atStartOfDay();

        List<Object[]> results =
                clickEventRepository.countClicksPerMonth(start, end);

        return results.stream()
                .map(r -> new ClicksPerMonthDTO(
                        ((Integer) r[0]),
                        ((Long) r[1])
                ))
                .toList();
    }

    public List<ClicksPerProductPerMonthDTO> getClicksPerProductPerMonthCurrentYear() {

        Year year = Year.now();

        LocalDateTime start = year.atDay(1).atStartOfDay();
        LocalDateTime end = year.plusYears(1).atDay(1).atStartOfDay();

        return clickEventRepository
                .countClicksPerProductPerMonth(start, end);
    }
    
    public List<ProductRecommendationGroupDTO> getAllRecommendations() {
        return productRepository.findAll().stream()
            .map(p -> new ProductRecommendationGroupDTO(
                p.getName(),
                clickEventRepository.findTopRelatedProducts(p.getId())
            ))
            .toList();
    }

    public UserRecommendationGroupDTO getRecommendationsForUser(String userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        List<ProductRecommendationDTO> recommended =
            clickEventRepository.findRecommendedProductsForUser(userId);

        return new UserRecommendationGroupDTO(
            user.getId(),
            user.getName(),
            recommended
        );
    }
}