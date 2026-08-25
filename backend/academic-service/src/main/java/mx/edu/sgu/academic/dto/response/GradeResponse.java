package mx.edu.sgu.academic.dto.response;

import mx.edu.sgu.academic.domain.Grade;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record GradeResponse(UUID id, short partialNumber, BigDecimal gradeValue, UUID recordedBy, OffsetDateTime recordedAt) {
    public static GradeResponse from(Grade g) {
        return new GradeResponse(g.getId(), g.getPartialNumber(), g.getGradeValue(), g.getRecordedBy(), g.getRecordedAt());
    }
}
