package com.rubens.ecommerce_backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.UserDTO;
import com.rubens.ecommerce_backend.model.Role;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.model.UserActivityLog;
import com.rubens.ecommerce_backend.repository.ClickEventRepository;
import com.rubens.ecommerce_backend.repository.UserRepository;
import com.rubens.ecommerce_backend.repository.UserActivityLogRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
  
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClickEventRepository clickEventRepository;
    private final UserActivityLogRepository logRepository;

    // --- Registro de usuário normal ---
    public UserDTO registerUser(User user, String performedBy) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // log
        logRepository.save(UserActivityLog.builder()
                .userId(savedUser.getId())
                .performedBy(performedBy)
                .action("CREATE")
                .details("Usuário criado com role: " + savedUser.getRole())
                .timestamp(LocalDateTime.now())
                .build()
        );

        return toDTO(savedUser);
    }

    // --- Registro de usuário admin ---
    public UserDTO registerUserAdmin(User user, String performedBy) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Email é obrigatório.");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Senha é obrigatória.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        if (user.getRole() == null) {
            user.setRole(Role.ROLE_USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // log
        logRepository.save(UserActivityLog.builder()
                .userId(savedUser.getId())
                .performedBy(performedBy)
                .action("CREATE")
                .details("Usuário admin criado com role: " + savedUser.getRole())
                .timestamp(LocalDateTime.now())
                .build()
        );

        return toDTO(savedUser);
    }

    // --- Deletar usuário ---
    @Transactional
    public void deleteUser(String id, String performedBy) {

        clickEventRepository.deleteByUserId(id);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        userRepository.delete(user);

        // log
        logRepository.save(UserActivityLog.builder()
                .userId(user.getId())
                .performedBy(performedBy)
                .action("DELETE")
                .details("Usuário deletado: " + user.getName() + ", email: " + user.getEmail() + ", role: " + user.getRole())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    public void logUserLogin(String userId) {
        logRepository.save(UserActivityLog.builder()
            .userId(userId)
            .performedBy(userId)
            .action("LOGIN")
            .details("Usuário efetuou login")
            .timestamp(LocalDateTime.now())
            .build()
        );
    }

    // --- Atualizar usuário ---
    @Transactional
    public UserDTO updateUser(String id, UserDTO dto, String performedBy) {

        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        StringBuilder details = new StringBuilder();

        if (dto.name() != null && !dto.name().isBlank()) {
            details.append("Nome: ").append(user.getName()).append(" -> ").append(dto.name()).append("; ");
            user.setName(dto.name());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            details.append("Email: ").append(user.getEmail()).append(" -> ").append(dto.email()).append("; ");
            user.setEmail(dto.email());
        }

        if (dto.role() != null && !dto.role().isBlank()) {
            details.append("Role: ").append(user.getRole()).append(" -> ").append(dto.role()).append("; ");
            user.setRole(Role.valueOf(dto.role().toUpperCase()));
        }

        User updatedUser = userRepository.save(user);

        // log
        logRepository.save(UserActivityLog.builder()
                .userId(user.getId())
                .performedBy(performedBy)
                .action("UPDATE")
                .details(details.toString())
                .timestamp(LocalDateTime.now())
                .build()
        );

        return toDTO(updatedUser);
    }

    // --- Consultas ---
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }

    public UserDTO getUser(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return toDTO(user);
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getEmail(),
            null,
            user.getRole().name(),
            user.getName()
        );
    }
}