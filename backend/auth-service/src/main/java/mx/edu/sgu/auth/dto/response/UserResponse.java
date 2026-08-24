package mx.edu.sgu.auth.dto.response;

import mx.edu.sgu.auth.domain.Role;
import mx.edu.sgu.auth.domain.User;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserResponse(
        UUID id,
        String username,
        String email,
        boolean active,
        boolean locked,
        Set<String> roles,
        OffsetDateTime lastLoginAt,
        OffsetDateTime createdAt
) {
    public static UserResponse from(User user) {
        Set<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return new UserResponse(
                user.getId(), user.getUsername(), user.getEmail(),
                user.isActive(), user.isLocked(), roleNames,
                user.getLastLoginAt(), user.getCreatedAt()
        );
    }
}
