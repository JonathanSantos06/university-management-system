package mx.edu.sgu.student.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateSemesterRequest(@NotNull @Min(1) @Max(20) Short currentSemester) {
}
