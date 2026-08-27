package mx.edu.sgu.document.dto.response;

import mx.edu.sgu.document.domain.DocumentType;

import java.util.UUID;

public record DocumentTypeResponse(UUID id, String name, String code, boolean required, String description) {
    public static DocumentTypeResponse from(DocumentType dt) {
        return new DocumentTypeResponse(dt.getId(), dt.getName(), dt.getCode(), dt.isRequired(), dt.getDescription());
    }
}
