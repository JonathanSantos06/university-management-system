package mx.edu.sgu.academic.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateEnrollmentRequest(
        @NotNull UUID studentId,
        @NotNull UUID academicPeriodId,
        @NotEmpty(message = "Debe inscribir al menos una materia") List<EnrollmentSubjectItem> subjects
) {
    public record EnrollmentSubjectItem(@NotNull UUID subjectId, String groupCode) {
    }
}
