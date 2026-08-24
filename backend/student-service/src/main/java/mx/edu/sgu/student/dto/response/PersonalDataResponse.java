package mx.edu.sgu.student.dto.response;

import mx.edu.sgu.student.domain.StudentPersonalData;

import java.time.LocalDate;

public record PersonalDataResponse(
        String firstName,
        String lastNamePaternal,
        String lastNameMaternal,
        LocalDate birthDate,
        String gender,
        String curp,
        String rfc,
        String nationality,
        String phone,
        String personalEmail
) {
    public static PersonalDataResponse from(StudentPersonalData pd) {
        return new PersonalDataResponse(
                pd.getFirstName(), pd.getLastNamePaternal(), pd.getLastNameMaternal(),
                pd.getBirthDate(), pd.getGender(), pd.getCurp(), pd.getRfc(),
                pd.getNationality(), pd.getPhone(), pd.getPersonalEmail()
        );
    }
}
