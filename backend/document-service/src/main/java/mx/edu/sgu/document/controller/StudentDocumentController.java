package mx.edu.sgu.document.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.document.domain.StudentDocument;
import mx.edu.sgu.document.dto.request.RejectDocumentRequest;
import mx.edu.sgu.document.dto.response.PendingDocumentResponse;
import mx.edu.sgu.document.dto.response.StudentDocumentResponse;
import mx.edu.sgu.document.security.CurrentUser;
import mx.edu.sgu.document.service.StudentDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Documentos", description = "Carga, consulta, validación y rechazo de documentos del expediente digital")
public class StudentDocumentController {

    private final StudentDocumentService studentDocumentService;

    /**
     * NOTA (misma limitación que student-service/academic-service): no se valida que
     * {studentId} pertenezca al usuario autenticado cuando el rol es ALUMNO — el JWT solo
     * trae el userId de auth-service. Se confía en que el frontend pasa el id correcto.
     */
    @GetMapping("/api/students/{studentId}/documents")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO', 'ALUMNO')")
    @Operation(summary = "Lista los documentos cargados por un alumno")
    public ResponseEntity<List<StudentDocumentResponse>> findByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(studentDocumentService.findByStudent(studentId));
    }

    @PostMapping(value = "/api/students/{studentId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO', 'ALUMNO')")
    @Operation(summary = "Sube un documento (multipart/form-data) para un alumno",
            description = "Campos: 'file' (el archivo) y 'documentTypeId' (uuid del tipo de documento)")
    public ResponseEntity<StudentDocumentResponse> upload(@PathVariable UUID studentId,
                                                           @RequestParam UUID documentTypeId,
                                                           @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(201).body(studentDocumentService.upload(studentId, documentTypeId, file));
    }

    @GetMapping("/api/documents/{id}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO', 'ALUMNO')")
    @Operation(summary = "Descarga el archivo de un documento")
    public ResponseEntity<Resource> download(@PathVariable UUID id) {
        StudentDocument document = studentDocumentService.getOrThrow(id);
        Resource resource = studentDocumentService.download(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(document.getFileName()).build().toString())
                .body(resource);
    }

    @GetMapping("/api/documents")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Lista documentos pendientes de validación en todo el sistema (usado por admin-service)")
    public ResponseEntity<List<PendingDocumentResponse>> pending(@RequestParam(defaultValue = "PENDIENTE") String status) {
        // Por ahora solo se soporta el filtro PENDIENTE (es el único caso de uso actual).
        return ResponseEntity.ok(studentDocumentService.findPending());
    }

    @PatchMapping("/api/documents/{id}/validate")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Valida (aprueba) un documento")
    public ResponseEntity<StudentDocumentResponse> validate(@PathVariable UUID id, @AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(studentDocumentService.validate(id, currentUser));
    }

    @PatchMapping("/api/documents/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Rechaza un documento indicando el motivo")
    public ResponseEntity<StudentDocumentResponse> reject(@PathVariable UUID id, @Valid @RequestBody RejectDocumentRequest request,
                                                            @AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(studentDocumentService.reject(id, request.reason(), currentUser));
    }
}
