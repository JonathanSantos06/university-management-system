package mx.edu.sgu.student.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateStudentRequest(
        @NotNull UUID userId,
        @NotBlank String studentCode,
        @NotNull UUID careerId,
        @NotNull UUID admissionPeriodId,
        @Valid @NotNull PersonalDataRequest personalData,
        @Valid AddressRequest address,
        @Valid EmergencyContactRequest emergencyContact
) {
}
