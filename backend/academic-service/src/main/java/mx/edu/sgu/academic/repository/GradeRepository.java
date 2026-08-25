package mx.edu.sgu.academic.repository;

import mx.edu.sgu.academic.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GradeRepository extends JpaRepository<Grade, UUID> {

    List<Grade> findByEnrollmentSubjectIdOrderByPartialNumberAsc(UUID enrollmentSubjectId);

    Optional<Grade> findByEnrollmentSubjectIdAndPartialNumber(UUID enrollmentSubjectId, short partialNumber);
}
