package com.rubens.ecommerce_backend.controller;

import org.springframework.http.ResponseEntity;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.RegisterRequest;
import com.rubens.ecommerce_backend.dto.UserDTO;
import com.rubens.ecommerce_backend.exception.InvalidCredentialsException;
import com.rubens.ecommerce_backend.model.AuthResponse;
import com.rubens.ecommerce_backend.model.LoginRequest;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.repository.UserRepository;
import com.rubens.ecommerce_backend.service.JwtService;
import com.rubens.ecommerce_backend.service.UserService;
import com.rubens.ecommerce_backend.service.WebSocketService;

import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final WebSocketService webSocketService;

    // Funcionando
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(
            new AuthResponse(token, user.getId(), user.getEmail(), user.getName())
        );
    }

    // Funcionando
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(request.password());

        UserDTO savedUserDTO = userService.registerUser(user, "system");

        String token = jwtService.generateToken(user);

        webSocketService.notify(savedUserDTO.id(), Map.of(
                "type", "USER_CREATED",
                "user", savedUserDTO
        ));

        return ResponseEntity.ok(
            new AuthResponse(token, savedUserDTO.id(), savedUserDTO.email(), savedUserDTO.name())
        );
    }

    // Funcionando
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!userService.passwordMatches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);

        userService.logUserLogin(user.getId());

        return ResponseEntity.ok(
            new AuthResponse(token, user.getId(), user.getEmail(), user.getName())
        );
    }
}