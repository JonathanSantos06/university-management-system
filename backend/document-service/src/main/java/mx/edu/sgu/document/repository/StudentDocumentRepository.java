package mx.edu.sgu.document.repository;

import mx.edu.sgu.document.domain.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudentDocumentRepository extends JpaRepository<StudentDocument, UUID> {

    List<StudentDocument> findByStudentIdOrderByUploadedAtDesc(UUID studentId);

    List<StudentDocument> findByStatusOrderByUploadedAtAsc(StudentDocument.Status status);
}
