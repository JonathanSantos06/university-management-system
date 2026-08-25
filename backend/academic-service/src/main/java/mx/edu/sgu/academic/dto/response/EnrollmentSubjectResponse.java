package mx.edu.sgu.academic.dto.response;

import mx.edu.sgu.academic.domain.EnrollmentSubject;

import java.util.List;
import java.util.UUID;

public record EnrollmentSubjectResponse(
        UUID id,
        UUID subjectId,
        String subjectCode,
        String subjectName,
        short credits,
        String groupCode,
        EnrollmentSubject.Status status,
        List<GradeResponse> grades
) {
    public static EnrollmentSubjectResponse from(EnrollmentSubject es) {
        List<GradeResponse> grades = es.getGrades() == null ? List.of() :
                es.getGrades().stream().map(GradeResponse::from).toList();
        return new EnrollmentSubjectResponse(
                es.getId(), es.getSubject().getId(), es.getSubject().getCode(), es.getSubject().getName(),
                es.getSubject().getCredits(), es.getGroupCode(), es.getStatus(), grades
        );
    }
}
