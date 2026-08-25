package mx.edu.sgu.academic.dto.response;

import mx.edu.sgu.academic.domain.EnrollmentSubject;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record KardexResponse(
        UUID studentId,
        int totalCreditsApproved,
        int totalCreditsInCareer,
        List<KardexEntry> entries
) {
    public record KardexEntry(
            String periodName,
            String subjectCode,
            String subjectName,
            short credits,
            BigDecimal finalGrade,
            EnrollmentSubject.Status status
    ) {
    }
}
