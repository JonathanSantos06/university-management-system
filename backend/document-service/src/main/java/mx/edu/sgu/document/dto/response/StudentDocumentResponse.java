package mx.edu.sgu.document.dto.response;

import mx.edu.sgu.document.domain.StudentDocument;

import java.time.OffsetDateTime;
import java.util.UUID;

public record StudentDocumentResponse(
        UUID id,
        UUID studentId,
        UUID documentTypeId,
        String documentTypeName,
        String fileName,
        String mimeType,
        StudentDocument.Status status,
        OffsetDateTime uploadedAt,
        UUID reviewedBy,
        OffsetDateTime reviewedAt,
        String rejectionReason
) {
    public static StudentDocumentResponse from(StudentDocument d) {
        return new StudentDocumentResponse(
                d.getId(), d.getStudentId(), d.getDocumentType().getId(), d.getDocumentType().getName(),
                d.getFileName(), d.getMimeType(), d.getStatus(), d.getUploadedAt(),
                d.getReviewedBy(), d.getReviewedAt(), d.getRejectionReason()
        );
    }
}
