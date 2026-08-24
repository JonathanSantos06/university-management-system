package mx.edu.sgu.student.dto.response;

import mx.edu.sgu.student.domain.Student;
import mx.edu.sgu.student.domain.StudentStatus;

import java.util.UUID;

public record StudentSummaryResponse(
        UUID id,
        String studentCode,
        String fullName,
        UUID careerId,
        short currentSemester,
        StudentStatus status
) {
    public static StudentSummaryResponse from(Student s) {
        String fullName = s.getPersonalData() == null ? "" :
                (s.getPersonalData().getFirstName() + " " + s.getPersonalData().getLastNamePaternal()
                        + " " + (s.getPersonalData().getLastNameMaternal() == null ? "" : s.getPersonalData().getLastNameMaternal())).trim();

        return new StudentSummaryResponse(s.getId(), s.getStudentCode(), fullName, s.getCareerId(),
                s.getCurrentSemester(), s.getStatus());
    }
}
