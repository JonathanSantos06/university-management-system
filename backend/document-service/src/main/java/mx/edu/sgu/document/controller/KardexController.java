package mx.edu.sgu.document.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.document.service.KardexPdfService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Kardex", description = "Generación del kardex académico en PDF (agrega datos de academic-service)")
public class KardexController {

    private final KardexPdfService kardexPdfService;

    /**
     * Misma limitación conocida documentada en los demás servicios: no se valida que
     * {studentId} pertenezca al usuario ALUMNO autenticado.
     */
    @GetMapping("/api/students/{studentId}/kardex/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO', 'ALUMNO')")
    @Operation(summary = "Genera y descarga el kardex académico del alumno en PDF",
            description = "Obtiene el historial académico llamando a academic-service y lo renderiza como PDF con Apache PDFBox.")
    public ResponseEntity<ByteArrayResource> downloadKardex(@PathVariable UUID studentId,
                                                             @RequestHeader("Authorization") String authorization) {
        byte[] pdfBytes = kardexPdfService.generate(studentId, authorization);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("kardex-" + studentId + ".pdf").build().toString())
                .contentLength(pdfBytes.length)
                .body(resource);
    }
}
