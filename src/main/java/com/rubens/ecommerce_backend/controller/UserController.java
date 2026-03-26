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
    public UserDTO registerUser(
            @RequestBody User user,
            @RequestHeader(value = "X-Admin-User", required = false) String performedBy
    ) {
        if (performedBy == null || performedBy.isBlank()) performedBy = "system";

        UserDTO created = userService.registerUserAdmin(user, performedBy);

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
    public void deleteUser(
            @PathVariable("id") String id,
            @RequestHeader(value = "X-Admin-User", required = false) String performedBy
    ) {
        if (performedBy == null || performedBy.isBlank()) performedBy = "system";

        userService.deleteUser(id, performedBy);

        webSocketService.notify(id, Map.of(
                "type", "USER_DELETED",
                "userId", id
        ));
    }

    // Funcionando
    @PatchMapping("/{id}")
    public UserDTO updateUser(
        @PathVariable("id") String id,
        @RequestBody UserDTO dto,
        @RequestHeader(value = "X-Admin-User", required = false) String performedBy
    ) {
        if (performedBy == null || performedBy.isBlank()) performedBy = "system";

        UserDTO updated = userService.updateUser(id, dto, performedBy);

        webSocketService.notify(updated.id(), Map.of(
                "type", "USER_UPDATED",
                "user", updated
        ));

        return updated;
    }
}