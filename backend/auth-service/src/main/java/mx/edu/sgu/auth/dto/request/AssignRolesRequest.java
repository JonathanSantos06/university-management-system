package mx.edu.sgu.auth.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record AssignRolesRequest(
        @NotEmpty(message = "Debe indicar al menos un rol") Set<String> roles
) {
}
