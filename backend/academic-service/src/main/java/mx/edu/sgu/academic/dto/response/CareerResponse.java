package mx.edu.sgu.academic.dto.response;

import mx.edu.sgu.academic.domain.Career;

import java.util.UUID;

public record CareerResponse(UUID id, String name, String code, String description, short totalSemesters, boolean active) {
    public static CareerResponse from(Career c) {
        return new CareerResponse(c.getId(), c.getName(), c.getCode(), c.getDescription(), c.getTotalSemesters(), c.isActive());
    }
}
