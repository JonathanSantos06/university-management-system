package mx.edu.sgu.academic.dto.response;

import mx.edu.sgu.academic.domain.AcademicPeriod;

import java.time.LocalDate;
import java.util.UUID;

public record AcademicPeriodResponse(UUID id, String name, String code, LocalDate startDate, LocalDate endDate, boolean active) {
    public static AcademicPeriodResponse from(AcademicPeriod p) {
        return new AcademicPeriodResponse(p.getId(), p.getName(), p.getCode(), p.getStartDate(), p.getEndDate(), p.isActive());
    }
}
