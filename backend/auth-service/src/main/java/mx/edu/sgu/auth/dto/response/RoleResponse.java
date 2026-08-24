package mx.edu.sgu.auth.dto.response;

import mx.edu.sgu.auth.domain.Role;

import java.util.UUID;

public record RoleResponse(UUID id, String name, String description) {
    public static RoleResponse from(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getDescription());
    }
}
