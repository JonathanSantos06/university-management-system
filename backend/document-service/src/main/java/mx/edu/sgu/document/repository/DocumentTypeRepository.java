package mx.edu.sgu.document.repository;

import mx.edu.sgu.document.domain.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, UUID> {
    Optional<DocumentType> findByCode(String code);
    boolean existsByCode(String code);
}
