package mx.edu.sgu.document.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/** Espejo de mx.edu.sgu.academic.dto.response.KardexResponse (solo lo que se necesita para el PDF). */
public record KardexDto(
        UUID studentId,
        int totalCreditsApproved,
        int totalCreditsInCareer,
        List<KardexEntryDto> entries
) {
    public record KardexEntryDto(
            String periodName,
            String subjectCode,
            String subjectName,
            short credits,
            BigDecimal finalGrade,
            String status
    ) {
    }
}
