package mx.edu.sgu.academic.dto.response;

import mx.edu.sgu.academic.domain.Enrollment;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EnrollmentResponse(
        UUID id,
        UUID studentId,
        UUID academicPeriodId,
        String academicPeriodName,
        Enrollment.EnrollmentStatus status,
        OffsetDateTime enrollmentDate,
        List<EnrollmentSubjectResponse> subjects
) {
    public static EnrollmentResponse from(Enrollment e) {
        List<EnrollmentSubjectResponse> subjects = e.getSubjects() == null ? List.of() :
                e.getSubjects().stream().map(EnrollmentSubjectResponse::from).toList();
        return new EnrollmentResponse(
                e.getId(), e.getStudentId(), e.getAcademicPeriod().getId(), e.getAcademicPeriod().getName(),
                e.getStatus(), e.getEnrollmentDate(), subjects
        );
    }
}
