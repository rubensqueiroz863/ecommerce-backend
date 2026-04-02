package com.rubens.ecommerce_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.PageResponse;
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
    // @PreAuthorize("hasRole('ADMIN')") imḉementação futura
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {

        UserDTO created = userService.registerUserAdmin(user, "system");

        try {
            webSocketService.notify(created.id(), Map.of(
                    "type", "USER_CREATED",
                    "user", created
            ));
        } catch (Exception e) {
            System.err.println("Erro ao enviar websocket: " + e.getMessage());
        }

        return ResponseEntity.status(201).body(created);
    }

    // Funcionando
    @GetMapping
    public PageResponse<UserDTO> getAllUsers(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return userService.getAllUsers(page, size);
    }

    // Funcionando
    @GetMapping("/search")
    public PageResponse<UserDTO> getAllUsersByName(
        @RequestParam(name = "name", defaultValue = "") String name,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return userService.getAllUsersByName(name, page, size);
    }

    // Funcionando
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    // Funcionando
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {

        userService.deleteUser(id, "system");

        try {
            webSocketService.notify(id, Map.of(
                    "type", "USER_DELETED",
                    "userId", id
            ));
        } catch (Exception e) {
            System.err.println("Erro ao enviar websocket: " + e.getMessage());
        }

        return ResponseEntity.noContent().build();
    }

    // Funcionando
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
        @PathVariable("id") String id,
        @RequestBody UserDTO dto
    ) {
        UserDTO updatedUser = userService.updateUser(id, dto, "system");

        try {
            webSocketService.notify(updatedUser.id(), Map.of(
                    "type", "USER_UPDATED",
                    "user", updatedUser
            ));
        } catch (Exception e) {
            System.err.println("Erro ao enviar websocket: " + e.getMessage());
        }

        return ResponseEntity.ok(updatedUser);
}
}