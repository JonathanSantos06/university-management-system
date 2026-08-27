package mx.edu.sgu.document.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.document.domain.DocumentType;
import mx.edu.sgu.document.dto.request.DocumentTypeRequest;
import mx.edu.sgu.document.dto.response.DocumentTypeResponse;
import mx.edu.sgu.document.exception.DuplicateResourceException;
import mx.edu.sgu.document.exception.ResourceNotFoundException;
import mx.edu.sgu.document.repository.DocumentTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    @Transactional(readOnly = true)
    public List<DocumentTypeResponse> findAll() {
        return documentTypeRepository.findAll().stream().map(DocumentTypeResponse::from).toList();
    }

    @Transactional
    public DocumentTypeResponse create(DocumentTypeRequest request) {
        if (documentTypeRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("Ya existe un tipo de documento con el código: " + request.code());
        }
        DocumentType documentType = DocumentType.builder()
                .name(request.name())
                .code(request.code())
                .required(request.required() == null || request.required())
                .description(request.description())
                .build();
        return DocumentTypeResponse.from(documentTypeRepository.save(documentType));
    }

    DocumentType getOrThrow(UUID id) {
        return documentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento no encontrado: " + id));
    }
}
