package mx.edu.sgu.academic.dto.response;

import mx.edu.sgu.academic.domain.Subject;

import java.util.UUID;

public record SubjectResponse(UUID id, String name, String code, short credits, short hoursTheory, short hoursPractice, boolean active) {
    public static SubjectResponse from(Subject s) {
        return new SubjectResponse(s.getId(), s.getName(), s.getCode(), s.getCredits(), s.getHoursTheory(), s.getHoursPractice(), s.isActive());
    }
}
