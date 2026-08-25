package mx.edu.sgu.academic.repository;

import mx.edu.sgu.academic.domain.Career;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CareerRepository extends JpaRepository<Career, UUID> {
    Optional<Career> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
