package mx.edu.sgu.admin.client;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Espejo mínimo del documento pendiente que se espera de document-service
 * (GET /api/documents?status=PENDIENTE). Contrato anticipado — se ajustará
 * cuando document-service quede construido.
 */
public record PendingDocumentDto(UUID id, UUID studentId, String studentFullName,
                                  String documentTypeName, OffsetDateTime uploadedAt) {
}
