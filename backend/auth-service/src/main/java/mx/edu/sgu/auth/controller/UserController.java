package mx.edu.sgu.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.auth.dto.request.AssignRolesRequest;
import mx.edu.sgu.auth.dto.request.ChangePasswordRequest;
import mx.edu.sgu.auth.dto.request.CreateUserRequest;
import mx.edu.sgu.auth.dto.response.UserResponse;
import mx.edu.sgu.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios y roles (ADMIN)")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lista todos los usuarios")
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtiene un usuario por id")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea un usuario con uno o más roles")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(201).body(userService.create(request));
    }

    @PatchMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reasigna los roles de un usuario")
    public ResponseEntity<UserResponse> assignRoles(@PathVariable UUID id, @Valid @RequestBody AssignRolesRequest request) {
        return ResponseEntity.ok(userService.assignRoles(id, request));
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activa o desactiva un usuario")
    public ResponseEntity<UserResponse> setActive(@PathVariable UUID id, @RequestParam boolean active) {
        return ResponseEntity.ok(userService.setActive(id, active));
    }

    @PatchMapping("/{id}/locked")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bloquea o desbloquea un usuario")
    public ResponseEntity<UserResponse> setLocked(@PathVariable UUID id, @RequestParam boolean locked) {
        return ResponseEntity.ok(userService.setLocked(id, locked));
    }

    @PostMapping("/{id}/change-password")
    @Operation(summary = "Cambia la contraseña del propio usuario")
    public ResponseEntity<Void> changePassword(@PathVariable UUID id, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }
}
