package mx.edu.sgu.student.dto.response;

import mx.edu.sgu.student.domain.EmergencyContact;

import java.util.UUID;

public record EmergencyContactResponse(
        UUID id,
        String fullName,
        String relationship,
        String phone,
        String email
) {
    public static EmergencyContactResponse from(EmergencyContact ec) {
        return new EmergencyContactResponse(ec.getId(), ec.getFullName(), ec.getRelationship(), ec.getPhone(), ec.getEmail());
    }
}
