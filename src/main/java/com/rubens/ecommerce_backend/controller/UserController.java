package com.rubens.ecommerce_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.rubens.ecommerce_backend.dto.UserDTO;
import com.rubens.ecommerce_backend.model.User;
import com.rubens.ecommerce_backend.model.UserActivityLog;
import com.rubens.ecommerce_backend.repository.UserActivityLogRepository;
import com.rubens.ecommerce_backend.service.UserService;
import com.rubens.ecommerce_backend.service.WebSocketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserActivityLogRepository logRepository;
    private final WebSocketService webSocketService;

    // CREATE
    @PostMapping("/register")
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

    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deletarUser(
            @PathVariable String id,
            @RequestHeader(value = "X-Admin-User", required = false) String performedBy
    ) {
        if (performedBy == null || performedBy.isBlank()) performedBy = "system";

        userService.deleteUser(id, performedBy);

        webSocketService.notify(id, Map.of(
                "type", "USER_DELETED",
                "userId", id
        ));
    }

    @PatchMapping("/edit/{id}")
    public UserDTO atualizarUsuario(
            @PathVariable String id,
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

    @GetMapping("/activity")
    public List<UserActivityLog> getAllLogs() {
        return logRepository.findAllByOrderByTimestampDesc();
    }
}