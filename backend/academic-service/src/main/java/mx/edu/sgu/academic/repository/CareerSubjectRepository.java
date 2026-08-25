package mx.edu.sgu.academic.repository;

import mx.edu.sgu.academic.domain.CareerSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CareerSubjectRepository extends JpaRepository<CareerSubject, UUID> {

    List<CareerSubject> findByCareerIdOrderBySemesterAsc(UUID careerId);

    Optional<CareerSubject> findByCareerIdAndSubjectId(UUID careerId, UUID subjectId);

    boolean existsByCareerIdAndSubjectId(UUID careerId, UUID subjectId);
}
