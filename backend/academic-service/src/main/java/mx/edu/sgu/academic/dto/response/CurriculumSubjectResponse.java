package mx.edu.sgu.academic.dto.response;

import mx.edu.sgu.academic.domain.CareerSubject;

import java.util.List;
import java.util.UUID;

public record CurriculumSubjectResponse(
        UUID careerSubjectId,
        UUID subjectId,
        String subjectCode,
        String subjectName,
        short credits,
        short semester,
        boolean mandatory,
        List<PrerequisiteResponse> prerequisites
) {
    public static CurriculumSubjectResponse from(CareerSubject cs, List<PrerequisiteResponse> prerequisites) {
        return new CurriculumSubjectResponse(
                cs.getId(), cs.getSubject().getId(), cs.getSubject().getCode(), cs.getSubject().getName(),
                cs.getSubject().getCredits(), cs.getSemester(), cs.isMandatory(), prerequisites
        );
    }

    public record PrerequisiteResponse(UUID subjectId, String code, String name) {
    }
}
