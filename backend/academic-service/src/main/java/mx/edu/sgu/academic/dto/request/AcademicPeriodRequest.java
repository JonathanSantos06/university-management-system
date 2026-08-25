package mx.edu.sgu.academic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AcademicPeriodRequest(
        @NotBlank String name,
        @NotBlank String code,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
