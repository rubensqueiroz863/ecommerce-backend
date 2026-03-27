package com.rubens.ecommerce_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.UserDTO;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.service.UserService;
import com.rubens.ecommerce_backend.service.WebSocketService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final WebSocketService webSocketService;

    // Funcionando
    @PostMapping
    public UserDTO registerUser(@RequestBody User user) {
        UserDTO created = userService.registerUserAdmin(user, "system");

        webSocketService.notify(created.id(), Map.of(
                "type", "USER_CREATED",
                "user", created
        ));

        return created;
    }

    // Funcionando
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Funcionando
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") String id) {
        return userService.getUser(id);
    }

    // Funcionando
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id, "system");

        webSocketService.notify(id, Map.of(
                "type", "USER_DELETED",
                "userId", id
        ));
    }

    // Funcionando
    @PatchMapping("/{id}")
    public UserDTO updateUser(
        @PathVariable("id") String id,
        @RequestBody UserDTO dto
    ) {
        UserDTO updatedUser = userService.updateUser(id, dto, "system");

        webSocketService.notify(updatedUser.id(), Map.of(
                "type", "USER_UPDATED",
                "user", updatedUser
        ));

        return updatedUser;
    }
}