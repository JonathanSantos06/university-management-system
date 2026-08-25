package mx.edu.sgu.academic.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.domain.EnrollmentSubject;
import mx.edu.sgu.academic.domain.Grade;
import mx.edu.sgu.academic.dto.response.KardexResponse;
import mx.edu.sgu.academic.repository.EnrollmentSubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KardexService {

    private final EnrollmentSubjectRepository enrollmentSubjectRepository;

    @Transactional(readOnly = true)
    public KardexResponse build(UUID studentId) {
        List<EnrollmentSubject> all = enrollmentSubjectRepository.findAllByStudentId(studentId);

        List<KardexResponse.KardexEntry> entries = all.stream()
                .map(es -> new KardexResponse.KardexEntry(
                        es.getEnrollment().getAcademicPeriod().getName(),
                        es.getSubject().getCode(),
                        es.getSubject().getName(),
                        es.getSubject().getCredits(),
                        finalGradeOf(es),
                        es.getStatus()
                ))
                .sorted(Comparator.comparing(KardexResponse.KardexEntry::periodName))
                .toList();

        int approvedCredits = all.stream()
                .filter(es -> es.getStatus() == EnrollmentSubject.Status.APROBADA)
                .mapToInt(es -> es.getSubject().getCredits())
                .sum();

        int totalCreditsAttempted = all.stream()
                .mapToInt(es -> es.getSubject().getCredits())
                .sum();

        return new KardexResponse(studentId, approvedCredits, totalCreditsAttempted, entries);
    }

    private BigDecimal finalGradeOf(EnrollmentSubject es) {
        return es.getGrades().stream()
                .max(Comparator.comparingInt(Grade::getPartialNumber))
                .map(Grade::getGradeValue)
                .orElse(null);
    }
}
