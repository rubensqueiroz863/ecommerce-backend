package com.rubens.ecommerce_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.UserDTO;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.model.UserActivityLog;
import com.rubens.ecommerce_backend.repository.UserActivityLogRepository;
import com.rubens.ecommerce_backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserActivityLogRepository logRepository;

    // ------------------------------
    // Criar usuário (admin)
    // ------------------------------
    @PostMapping("/register")
    public UserDTO registerUser(
            @RequestBody User user,
            @RequestHeader(value = "X-Admin-User", required = false) String performedBy
    ) {
        if (performedBy == null || performedBy.isBlank()) performedBy = "system";
        return userService.registerUserAdmin(user, performedBy);
    }

    // ------------------------------
    // Listar todos os usuários
    // ------------------------------
    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // ------------------------------
    // Buscar usuário por ID
    // ------------------------------
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    // ------------------------------
    // Deletar usuário
    // ------------------------------
    @DeleteMapping("/delete/{id}")
    public void deletarUser(
            @PathVariable String id,
            @RequestHeader(value = "X-Admin-User", required = false) String performedBy
    ) {
        if (performedBy == null || performedBy.isBlank()) performedBy = "system";
        userService.deleteUser(id, performedBy);
    }

    // ------------------------------
    // Atualizar usuário
    // ------------------------------
    @PatchMapping("/edit/{id}")
    public UserDTO atualizarUsuario(
            @PathVariable String id,
            @RequestBody UserDTO dto,
            @RequestHeader(value = "X-Admin-User", required = false) String performedBy
    ) {
        if (performedBy == null || performedBy.isBlank()) performedBy = "system";
        return userService.updateUser(id, dto, performedBy);
    }

    // Pega todos os logs, mais recentes primeiro
    @GetMapping("/activity")
    public List<UserActivityLog> getAllLogs() {
        return logRepository.findAllByOrderByTimestampDesc();
    }
}