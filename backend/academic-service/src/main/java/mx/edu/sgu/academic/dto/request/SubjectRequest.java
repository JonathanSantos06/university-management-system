package mx.edu.sgu.academic.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubjectRequest(
        @NotBlank String name,
        @NotBlank String code,
        @NotNull @Min(0) Short credits,
        Short hoursTheory,
        Short hoursPractice
) {
}
