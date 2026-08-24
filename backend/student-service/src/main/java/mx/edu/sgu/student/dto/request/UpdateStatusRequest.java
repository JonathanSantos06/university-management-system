package mx.edu.sgu.student.dto.request;

import jakarta.validation.constraints.NotNull;
import mx.edu.sgu.student.domain.StudentStatus;

public record UpdateStatusRequest(@NotNull StudentStatus status) {
}
