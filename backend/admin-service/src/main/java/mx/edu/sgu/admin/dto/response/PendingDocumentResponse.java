package mx.edu.sgu.admin.dto.response;

import mx.edu.sgu.admin.client.PendingDocumentDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PendingDocumentResponse(UUID id, UUID studentId, String studentFullName,
                                       String documentTypeName, OffsetDateTime uploadedAt) {
    public static PendingDocumentResponse from(PendingDocumentDto dto) {
        return new PendingDocumentResponse(dto.id(), dto.studentId(), dto.studentFullName(),
                dto.documentTypeName(), dto.uploadedAt());
    }
}
