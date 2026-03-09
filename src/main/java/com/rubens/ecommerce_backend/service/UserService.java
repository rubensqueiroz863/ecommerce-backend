package com.rubens.ecommerce_backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.UserDTO;
import com.rubens.ecommerce_backend.model.Role;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.repository.ClickEventRepository;
import com.rubens.ecommerce_backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClickEventRepository clickEventRepository;

    public UserDTO registerUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return toDTO(savedUser);
    }

    public UserDTO registerUserAdmin(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Email é obrigatório.");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Senha é obrigatória.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        // Define role padrão se não vier
        if (user.getRole() == null) {
            user.setRole(Role.ROLE_USER);
        }

        // Criptografar senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return toDTO(savedUser);
    }

    @Transactional
    public void deleteUser(String id) {

        clickEventRepository.deleteByUserId(id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        userRepository.delete(user);
    }

    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
            .stream()
            .map(this::toDTO)
            .toList();
    }

    public UserDTO getUser(String id) {

        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return toDTO(user);
    }

    public UserDTO updateUser(String id, UserDTO dto) {

        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.name() != null && !dto.name().isBlank()) {
            user.setName(dto.name());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            user.setEmail(dto.email());
        }

        if (dto.role() != null && !dto.role().isBlank()) {
            user.setRole(Role.valueOf(dto.role().toUpperCase()));
        }

        User updatedUser = userRepository.save(user);

        return toDTO(updatedUser);
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getEmail(),
            null, // nunca retornar senha
            user.getRole().name(),
            user.getName()
        );
    }
}