package mx.edu.sgu.document.dto.response;

import mx.edu.sgu.document.domain.StudentDocument;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Forma esperada por admin-service (mx.edu.sgu.admin.client.PendingDocumentDto).
 * document-service no conoce el nombre del alumno (eso vive en student-service), por lo
 * que "studentFullName" simplemente no se incluye — Jackson lo deserializa como null en
 * admin-service sin error, ya que ese campo no es obligatorio en el contrato.
 */
public record PendingDocumentResponse(UUID id, UUID studentId, String documentTypeName, OffsetDateTime uploadedAt) {
    public static PendingDocumentResponse from(StudentDocument d) {
        return new PendingDocumentResponse(d.getId(), d.getStudentId(), d.getDocumentType().getName(), d.getUploadedAt());
    }
}
