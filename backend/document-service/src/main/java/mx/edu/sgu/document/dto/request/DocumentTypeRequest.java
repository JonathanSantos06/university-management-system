package mx.edu.sgu.document.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DocumentTypeRequest(
        @NotBlank String name,
        @NotBlank String code,
        Boolean required,
        String description
) {
}
