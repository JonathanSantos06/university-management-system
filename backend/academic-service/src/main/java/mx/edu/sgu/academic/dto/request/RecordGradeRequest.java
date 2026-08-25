package mx.edu.sgu.academic.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RecordGradeRequest(
        @NotNull @Min(1) @Max(4) Short partialNumber,
        @NotNull @DecimalMin("0.0") @DecimalMax("10.0") BigDecimal gradeValue
) {
}
