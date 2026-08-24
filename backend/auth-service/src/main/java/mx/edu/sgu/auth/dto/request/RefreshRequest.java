package mx.edu.sgu.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "El refreshToken es requerido") String refreshToken
) {
}
