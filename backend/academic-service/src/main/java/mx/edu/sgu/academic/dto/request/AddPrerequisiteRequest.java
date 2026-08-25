package mx.edu.sgu.academic.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddPrerequisiteRequest(@NotNull UUID prerequisiteSubjectId) {
}
