package mx.edu.sgu.academic.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.config.AcademicProperties;
import mx.edu.sgu.academic.domain.EnrollmentSubject;
import mx.edu.sgu.academic.domain.Grade;
import mx.edu.sgu.academic.dto.request.RecordGradeRequest;
import mx.edu.sgu.academic.dto.response.GradeResponse;
import mx.edu.sgu.academic.exception.ResourceNotFoundException;
import mx.edu.sgu.academic.repository.EnrollmentSubjectRepository;
import mx.edu.sgu.academic.repository.GradeRepository;
import mx.edu.sgu.academic.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final EnrollmentSubjectRepository enrollmentSubjectRepository;
    private final AcademicProperties academicProperties;

    @Transactional(readOnly = true)
    public List<GradeResponse> findByEnrollmentSubject(UUID enrollmentSubjectId) {
        return gradeRepository.findByEnrollmentSubjectIdOrderByPartialNumberAsc(enrollmentSubjectId).stream()
                .map(GradeResponse::from)
                .toList();
    }

    /**
     * Registra (o sobrescribe) la calificación de un parcial. Si el parcial capturado es el
     * "final" (por defecto el 4), se actualiza automáticamente el estatus de la materia
     * inscrita a APROBADA o REPROBADA según sgu.academic.passing-grade.
     */
    @Transactional
    public GradeResponse recordGrade(UUID enrollmentSubjectId, RecordGradeRequest request, CurrentUser currentUser) {
        EnrollmentSubject enrollmentSubject = enrollmentSubjectRepository.findById(enrollmentSubjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Materia inscrita no encontrada: " + enrollmentSubjectId));

        Grade grade = gradeRepository.findByEnrollmentSubjectIdAndPartialNumber(enrollmentSubjectId, request.partialNumber())
                .orElse(Grade.builder().enrollmentSubject(enrollmentSubject).partialNumber(request.partialNumber()).build());

        grade.setGradeValue(request.gradeValue());
        grade.setRecordedBy(currentUser == null ? null : currentUser.userId());
        grade = gradeRepository.save(grade);

        if (request.partialNumber() == academicProperties.finalPartialNumber()) {
            boolean approved = request.gradeValue().compareTo(academicProperties.passingGrade()) >= 0;
            enrollmentSubject.setStatus(approved ? EnrollmentSubject.Status.APROBADA : EnrollmentSubject.Status.REPROBADA);
            enrollmentSubjectRepository.save(enrollmentSubject);
        }

        return GradeResponse.from(grade);
    }
}
