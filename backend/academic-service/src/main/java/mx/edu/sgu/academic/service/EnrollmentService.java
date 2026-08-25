package mx.edu.sgu.academic.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.domain.*;
import mx.edu.sgu.academic.dto.request.CreateEnrollmentRequest;
import mx.edu.sgu.academic.dto.response.EnrollmentResponse;
import mx.edu.sgu.academic.exception.BusinessRuleException;
import mx.edu.sgu.academic.exception.DuplicateResourceException;
import mx.edu.sgu.academic.exception.ResourceNotFoundException;
import mx.edu.sgu.academic.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentSubjectRepository enrollmentSubjectRepository;
    private final AcademicPeriodRepository academicPeriodRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectPrerequisiteRepository prerequisiteRepository;

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByStudent(UUID studentId) {
        return enrollmentRepository.findByStudentIdOrderByEnrollmentDateDesc(studentId).stream()
                .map(EnrollmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public EnrollmentResponse findById(UUID id) {
        return EnrollmentResponse.from(getOrThrow(id));
    }

    @Transactional
    public EnrollmentResponse create(CreateEnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndAcademicPeriodId(request.studentId(), request.academicPeriodId())) {
            throw new DuplicateResourceException("El alumno ya tiene una inscripción para este periodo");
        }

        AcademicPeriod period = academicPeriodRepository.findById(request.academicPeriodId())
                .orElseThrow(() -> new ResourceNotFoundException("Periodo académico no encontrado: " + request.academicPeriodId()));

        // Materias ya aprobadas por el alumno (histórico), para validar prerrequisitos
        Set<UUID> approvedSubjectIds = enrollmentSubjectRepository.findAllByStudentId(request.studentId()).stream()
                .filter(es -> es.getStatus() == EnrollmentSubject.Status.APROBADA)
                .map(es -> es.getSubject().getId())
                .collect(Collectors.toSet());

        Enrollment enrollment = Enrollment.builder()
                .studentId(request.studentId())
                .academicPeriod(period)
                .status(Enrollment.EnrollmentStatus.ACTIVA)
                .build();
        enrollment = enrollmentRepository.save(enrollment);

        for (CreateEnrollmentRequest.EnrollmentSubjectItem item : request.subjects()) {
            Subject subject = subjectRepository.findById(item.subjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada: " + item.subjectId()));

            validatePrerequisites(subject, approvedSubjectIds);

            EnrollmentSubject enrollmentSubject = EnrollmentSubject.builder()
                    .enrollment(enrollment)
                    .subject(subject)
                    .groupCode(item.groupCode() == null || item.groupCode().isBlank() ? "A" : item.groupCode())
                    .status(EnrollmentSubject.Status.CURSANDO)
                    .build();
            enrollmentSubjectRepository.save(enrollmentSubject);
            enrollment.getSubjects().add(enrollmentSubject);
        }

        return EnrollmentResponse.from(enrollment);
    }

    @Transactional
    public EnrollmentResponse updateStatus(UUID enrollmentId, Enrollment.EnrollmentStatus status) {
        Enrollment enrollment = getOrThrow(enrollmentId);
        enrollment.setStatus(status);
        return EnrollmentResponse.from(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public EnrollmentResponse updateSubjectStatus(UUID enrollmentId, UUID enrollmentSubjectId, EnrollmentSubject.Status status) {
        Enrollment enrollment = getOrThrow(enrollmentId);
        EnrollmentSubject enrollmentSubject = enrollmentSubjectRepository.findById(enrollmentSubjectId)
                .filter(es -> es.getEnrollment().getId().equals(enrollmentId))
                .orElseThrow(() -> new ResourceNotFoundException("La materia inscrita no pertenece a esta inscripción"));
        enrollmentSubject.setStatus(status);
        enrollmentSubjectRepository.save(enrollmentSubject);
        return EnrollmentResponse.from(enrollment);
    }

    private void validatePrerequisites(Subject subject, Set<UUID> approvedSubjectIds) {
        List<SubjectPrerequisite> prerequisites = prerequisiteRepository.findBySubjectId(subject.getId());
        List<String> missing = prerequisites.stream()
                .filter(p -> !approvedSubjectIds.contains(p.getPrerequisiteSubject().getId()))
                .map(p -> p.getPrerequisiteSubject().getCode() + " - " + p.getPrerequisiteSubject().getName())
                .toList();

        if (!missing.isEmpty()) {
            throw new BusinessRuleException("No se puede inscribir " + subject.getCode()
                    + " (" + subject.getName() + "): faltan prerrequisitos aprobados: " + String.join(", ", missing));
        }
    }

    @Transactional(readOnly = true)
    public long countByStatus(Enrollment.EnrollmentStatus status) {
        return enrollmentRepository.countByStatus(status);
    }

    Enrollment getOrThrow(UUID id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada: " + id));
    }
}
