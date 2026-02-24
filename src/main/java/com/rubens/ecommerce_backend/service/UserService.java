package com.rubens.ecommerce_backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.UserDTO;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
          throw new RuntimeException("Email já cadastrado.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return new UserDTO(
          savedUser.getId(),
          savedUser.getEmail(),
          savedUser.getPassword(),
          savedUser.getName()
        );
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
