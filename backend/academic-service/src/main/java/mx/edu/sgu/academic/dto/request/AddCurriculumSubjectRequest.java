package mx.edu.sgu.academic.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddCurriculumSubjectRequest(
        @NotNull UUID subjectId,
        @NotNull @Min(1) @Max(20) Short semester,
        Boolean mandatory
) {
}
