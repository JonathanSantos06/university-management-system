package mx.edu.sgu.student.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import mx.edu.sgu.student.domain.Address;

public record AddressRequest(
        @NotNull Address.AddressType addressType,
        @NotBlank String street,
        String extNumber,
        String intNumber,
        String neighborhood,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String postalCode,
        String country
) {
}
