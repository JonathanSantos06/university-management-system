package mx.edu.sgu.student.dto.response;

import mx.edu.sgu.student.domain.Student;
import mx.edu.sgu.student.domain.StudentStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record StudentResponse(
        UUID id,
        UUID userId,
        String studentCode,
        UUID careerId,
        UUID admissionPeriodId,
        short currentSemester,
        StudentStatus status,
        PersonalDataResponse personalData,
        List<AddressResponse> addresses,
        List<EmergencyContactResponse> emergencyContacts,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static StudentResponse from(Student s) {
        PersonalDataResponse pd = s.getPersonalData() == null ? null : PersonalDataResponse.from(s.getPersonalData());
        List<AddressResponse> addresses = s.getAddresses() == null ? List.of() :
                s.getAddresses().stream().map(AddressResponse::from).toList();
        List<EmergencyContactResponse> contacts = s.getEmergencyContacts() == null ? List.of() :
                s.getEmergencyContacts().stream().map(EmergencyContactResponse::from).toList();

        return new StudentResponse(
                s.getId(), s.getUserId(), s.getStudentCode(), s.getCareerId(), s.getAdmissionPeriodId(),
                s.getCurrentSemester(), s.getStatus(), pd, addresses, contacts,
                s.getCreatedAt(), s.getUpdatedAt()
        );
    }
}
