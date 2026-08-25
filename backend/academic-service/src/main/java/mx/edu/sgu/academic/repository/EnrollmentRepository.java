package mx.edu.sgu.academic.repository;

import mx.edu.sgu.academic.domain.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByStudentIdOrderByEnrollmentDateDesc(UUID studentId);

    Optional<Enrollment> findByStudentIdAndAcademicPeriodId(UUID studentId, UUID academicPeriodId);

    boolean existsByStudentIdAndAcademicPeriodId(UUID studentId, UUID academicPeriodId);

    long countByStatus(mx.edu.sgu.academic.domain.Enrollment.EnrollmentStatus status);
}
