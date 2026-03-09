package com.rubens.ecommerce_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.ProductDTO;
import com.rubens.ecommerce_backend.dto.ProductRequestDTO;
import com.rubens.ecommerce_backend.dto.UserDTO;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Criar usuário (admin)
    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody User user) {
        return userService.registerUserAdmin(user);
    }

    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deletarUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    @PatchMapping("/edit/{id}")
    public UserDTO atualizarUsuario(
        @PathVariable String id,
        @RequestBody UserDTO dto
    ) {
        return userService.updateUser(id, dto);
    }

}