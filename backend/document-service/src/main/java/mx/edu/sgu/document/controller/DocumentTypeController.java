package mx.edu.sgu.document.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.document.dto.request.DocumentTypeRequest;
import mx.edu.sgu.document.dto.response.DocumentTypeResponse;
import mx.edu.sgu.document.service.DocumentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-types")
@RequiredArgsConstructor
@Tag(name = "Tipos de documento", description = "Catálogo de documentos requeridos en el expediente digital")
public class DocumentTypeController {

    private final DocumentTypeService documentTypeService;

    @GetMapping
    @Operation(summary = "Lista los tipos de documento (catálogo)")
    public ResponseEntity<List<DocumentTypeResponse>> findAll() {
        return ResponseEntity.ok(documentTypeService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea un tipo de documento en el catálogo")
    public ResponseEntity<DocumentTypeResponse> create(@Valid @RequestBody DocumentTypeRequest request) {
        return ResponseEntity.status(201).body(documentTypeService.create(request));
    }
}
