package mx.edu.sgu.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El usuario es requerido") String username,
        @NotBlank(message = "La contraseña es requerida") String password
) {
}
