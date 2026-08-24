package mx.edu.sgu.student.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmergencyContactRequest(
        @NotBlank String fullName,
        @NotBlank String relationship,
        @NotBlank String phone,
        @Email String email
) {
}
