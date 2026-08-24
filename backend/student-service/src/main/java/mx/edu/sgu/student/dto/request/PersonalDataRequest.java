package mx.edu.sgu.student.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record PersonalDataRequest(
        @NotBlank String firstName,
        @NotBlank String lastNamePaternal,
        String lastNameMaternal,
        @NotNull @Past(message = "La fecha de nacimiento debe ser en el pasado") LocalDate birthDate,
        String gender,
        @NotBlank @Pattern(regexp = "^[A-Z0-9]{18}$", message = "CURP inválida, debe tener 18 caracteres") String curp,
        String rfc,
        String phone,
        @Email String personalEmail
) {
}
