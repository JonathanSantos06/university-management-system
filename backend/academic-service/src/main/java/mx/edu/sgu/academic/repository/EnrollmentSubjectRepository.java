package mx.edu.sgu.academic.repository;

import mx.edu.sgu.academic.domain.EnrollmentSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentSubjectRepository extends JpaRepository<EnrollmentSubject, UUID> {

    List<EnrollmentSubject> findByEnrollmentId(UUID enrollmentId);

    Optional<EnrollmentSubject> findByEnrollmentIdAndSubjectId(UUID enrollmentId, UUID subjectId);

    boolean existsByEnrollmentIdAndSubjectId(UUID enrollmentId, UUID subjectId);

    @org.springframework.data.jpa.repository.Query("""
            SELECT es FROM EnrollmentSubject es
            JOIN es.enrollment e
            WHERE e.studentId = :studentId
            ORDER BY e.enrollmentDate DESC
            """)
    List<EnrollmentSubject> findAllByStudentId(@org.springframework.data.repository.query.Param("studentId") UUID studentId);
}
