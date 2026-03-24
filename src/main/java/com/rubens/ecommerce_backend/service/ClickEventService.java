package com.rubens.ecommerce_backend.service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
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
import com.rubens.ecommerce_backend.model.ClickEvent;
import com.rubens.ecommerce_backend.model.Product;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.repository.ClickEventRepository;
import com.rubens.ecommerce_backend.repository.ProductRepository;
import com.rubens.ecommerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClickEventService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ClickEventRepository clickEventRepository;

    public ClickEventDTO createClick(String productId, String email) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        Product product = productRepository.findById(productId)
            .orElseThrow();

        ClickEvent event = new ClickEvent(user, product);

        ClickEvent savedEvent = clickEventRepository.save(event);

        return new ClickEventDTO(
            savedEvent.getId(),
            savedEvent.getUser(),
            savedEvent.getProduct()
        );
    }

    public List<MostClickedProductDTO> getMostClickedProductsByUser(String userId, int limit) {

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
        List<Product> allProducts = productRepository.findAll();

        List<ProductRecommendationGroupDTO> allRecommendations = new ArrayList<>();

        for (Product p : allProducts) {
            List<ProductRecommendationDTO> related = clickEventRepository.findTopRelatedProducts(p.getId());

            ProductRecommendationGroupDTO group = new ProductRecommendationGroupDTO(
                p.getName(),
                related
            );

            allRecommendations.add(group);
        }

        return allRecommendations;
    }

    public UserRecommendationGroupDTO getRecommendationsForUser(String userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        List<ProductRecommendationDTO> recommended =
            clickEventRepository.findRecommendedProductsForUser(userId);

        return new UserRecommendationGroupDTO(
            user.getId(),
            user.getName(),
            recommended
        );
    }
}