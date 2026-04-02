package com.rubens.ecommerce_backend.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rubens.ecommerce_backend.dto.PageResponse;
import com.rubens.ecommerce_backend.dto.UserDTO;
import com.rubens.ecommerce_backend.exception.EmailAlreadyExistsException;
import com.rubens.ecommerce_backend.exception.InvalidEmailException;
import com.rubens.ecommerce_backend.exception.InvalidLimitException;
import com.rubens.ecommerce_backend.exception.InvalidNameException;
import com.rubens.ecommerce_backend.exception.InvalidPageException;
import com.rubens.ecommerce_backend.exception.InvalidRoleException;
import com.rubens.ecommerce_backend.exception.UserDeletionException;
import com.rubens.ecommerce_backend.exception.UserNotFoundException;
import com.rubens.ecommerce_backend.exception.WeakPasswordException;
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
    private final UserActivityLogRepository userActivityLogRepository;

    public UserDTO registerUser(User user, String performedBy) {

        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidNameException();
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidEmailException("Enter your email");
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidEmailException("Invalid email.");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new WeakPasswordException("Enter your password.");
        }

        if (user.getPassword().length() < 8) {
            throw new WeakPasswordException("Password has to be at least 8 characters.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        if (user.getRole() == null) {
            user.setRole(Role.ROLE_USER);
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            try {
                userActivityLogRepository.save(UserActivityLog.builder()
                        .userId(savedUser.getId())
                        .performedBy(performedBy)
                        .action("CREATE")
                        .details("Usuário criado com role: " + savedUser.getRole().name())
                        .timestamp(LocalDateTime.now())
                        .build()
                );
            } catch (Exception logError) {
                System.err.println("Erro ao salvar log: " + logError.getMessage());
            }

            return toDTO(savedUser);

        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException();
        }
    }

    public UserDTO registerUserAdmin(User user, String performedBy) {

        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidNameException();
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidEmailException("Enter your email");
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidEmailException("Invalid email.");
        }

        if (user.getRole() != Role.ROLE_USER && user.getRole() != Role.ROLE_ADMIN) {
            throw new InvalidRoleException("Invalid role.");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new WeakPasswordException("Enter your password.");
        }

        if (user.getPassword().length() < 8) {
            throw new WeakPasswordException("Password has to be at least 8 characters.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        if (user.getRole() == null) {
            throw new InvalidRoleException("Enter a role");
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            try {
                userActivityLogRepository.save(UserActivityLog.builder()
                        .userId(savedUser.getId())
                        .performedBy(performedBy)
                        .action("CREATE")
                        .details("Usuário admin criado com role: " + savedUser.getRole().name())
                        .timestamp(LocalDateTime.now())
                        .build()
                );
            } catch (Exception logError) {
                System.err.println("Erro ao salvar log: " + logError.getMessage());
            }

            return toDTO(savedUser);

        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException();
        }
    }

    @Transactional
    public void deleteUser(String id, String performedBy) {

        User user = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        try {
            clickEventRepository.deleteByUserId(id);

            userRepository.delete(user);

        } catch (DataIntegrityViolationException e) {
            throw new UserDeletionException("The user could not be deleted.");
        }

        try {
            userActivityLogRepository.save(UserActivityLog.builder()
                    .userId(user.getId())
                    .performedBy(performedBy)
                    .action("DELETE")
                    .details("Usuário deletado: " + user.getName() + ", email: " + user.getEmail() + ", role: " + user.getRole())
                    .timestamp(LocalDateTime.now())
                    .build()
            );
        } catch (Exception logError) {
            System.err.println("Erro ao salvar log: " + logError.getMessage());
        }
    }

    public void logUserLogin(String userId) {
        try {
            userActivityLogRepository.save(UserActivityLog.builder()
                .userId(userId)
                .performedBy(userId)
                .action("LOGIN")
                .details("Usuário efetuou login")
                .timestamp(LocalDateTime.now())
                .build()
            );
        } catch (Exception e) {
            System.err.println("Erro ao salvar log de login: " + e.getMessage());
        }
    }

    @Transactional
    public UserDTO updateUser(String id, UserDTO dto, String performedBy) {

        User user = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        StringBuilder details = new StringBuilder();

        if (dto.name() != null && !dto.name().isBlank()) {
            details.append("Nome: ")
                .append(user.getName())
                .append(" -> ")
                .append(dto.name())
                .append("; ");

            user.setName(dto.name());
        }

        if (dto.email() != null && !dto.email().isBlank()) {

            if (!dto.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new InvalidEmailException("Invalid email.");
            }

            details.append("Email: ")
                .append(user.getEmail())
                .append(" -> ")
                .append(dto.email())
                .append("; ");

            user.setEmail(dto.email());
        }

        // 🔹 Role
        if (dto.role() != null && !dto.role().isBlank()) {
            try {
                Role newRole = Role.valueOf(dto.role().toUpperCase());

                details.append("Role: ")
                    .append(user.getRole())
                    .append(" -> ")
                    .append(newRole)
                    .append("; ");

                user.setRole(newRole);

            } catch (IllegalArgumentException e) {
                throw new InvalidRoleException("Invalid role.");
            }
        }

        // Password
        if (dto.password() != null && !dto.password().isBlank()) {

            if (dto.password().length() < 8) {
                throw new WeakPasswordException("Password has to be at least 8 characters.");
            }

            details.append("Password: ")
                .append("Confidential")
                .append(" -> ")
                .append("Confidential")
                .append("; ");

            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        try {
            User updatedUser = userRepository.save(user);
            try {
                userActivityLogRepository.save(UserActivityLog.builder()
                        .userId(user.getId())
                        .performedBy(performedBy)
                        .action("UPDATE")
                        .details(details.toString())
                        .timestamp(LocalDateTime.now())
                        .build()
                );
            } catch (Exception logError) {
                System.err.println("Erro ao salvar log: " + logError.getMessage());
            }

            return toDTO(updatedUser);

        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException();
        }
    }

    public PageResponse<UserDTO> getAllUsers(int page, int size) {
        
        if (page < 0) {
            throw new InvalidPageException();
        }

        if (size <= 0 || size > 100) {
            throw new InvalidLimitException();
        }
        
        PageRequest pageable = PageRequest.of(page, size);
        Page<User> result = userRepository.findAll(pageable);
        return toPageResponse(result);
    }

    public PageResponse<UserDTO> getAllUsersByName(String name, int page, int size) {
        
        if (page < 0) {
            throw new InvalidPageException();
        }

        if (size <= 0 || size > 100) {
            throw new InvalidLimitException();
        }

        PageRequest pageable = PageRequest.of(page, size);
        Page<User> result = userRepository.findByNameContainingIgnoreCase(name, pageable);
        return toPageResponse(result);
    }

    private PageResponse<UserDTO> toPageResponse(Page<User> page) {
        return new PageResponse<>(
            page.getContent()
                .stream()
                .map(this::toDTO)
                .toList(),
            page.hasNext()
        );
    }

    public UserDTO getUser(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

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