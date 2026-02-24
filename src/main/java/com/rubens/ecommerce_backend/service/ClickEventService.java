package com.rubens.ecommerce_backend.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.ClickEventDTO;
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

    public ClickEventDTO registerClick(String productId, String email) {

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
}