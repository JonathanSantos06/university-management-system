package mx.edu.sgu.academic.repository;

import mx.edu.sgu.academic.domain.AcademicPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AcademicPeriodRepository extends JpaRepository<AcademicPeriod, UUID> {
    Optional<AcademicPeriod> findByCode(String code);
    boolean existsByCode(String code);
    List<AcademicPeriod> findByActiveTrue();
}
