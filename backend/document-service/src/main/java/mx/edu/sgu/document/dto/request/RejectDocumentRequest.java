package mx.edu.sgu.document.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RejectDocumentRequest(@NotBlank(message = "Debe indicar el motivo del rechazo") String reason) {
}
