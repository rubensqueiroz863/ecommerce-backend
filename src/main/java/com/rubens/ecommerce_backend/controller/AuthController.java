package com.rubens.ecommerce_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.UserDTO;
import com.rubens.ecommerce_backend.model.AuthResponse;
import com.rubens.ecommerce_backend.model.LoginRequest;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.repository.UserRepository;
import com.rubens.ecommerce_backend.service.JwtService;
import com.rubens.ecommerce_backend.service.UserService;

import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    // ------------------------------
    // Dados do usuário logado
    // ------------------------------
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

    // ------------------------------
    // Registrar usuário
    // ------------------------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        try {
            // Aqui usamos "system" como performedBy, porque o registro não foi feito por um admin logado
            UserDTO savedUserResponse = userService.registerUser(user, "system");

            String token = jwtService.generateToken(user); // ou use o User salvo do repositório

            return ResponseEntity.ok(
                    new AuthResponse(token, savedUserResponse.id(), savedUserResponse.email(), savedUserResponse.name())
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ------------------------------
    // Login
    // ------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos."));

        if (!userService.passwordMatches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida");
        }

        String token = jwtService.generateToken(user);

        // Opcional: registrar log de login
        userService.logUserLogin(user.getId());

        return ResponseEntity.ok(
                new AuthResponse(token, user.getId(), user.getEmail(), user.getName())
        );
    }

}