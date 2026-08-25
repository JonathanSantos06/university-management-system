package mx.edu.sgu.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.admin.dto.response.AdminDashboardResponse;
import mx.edu.sgu.admin.dto.response.PendingDocumentResponse;
import mx.edu.sgu.admin.service.AdminDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Administración", description = "Dashboards y reportes que agregan datos de otros microservicios")
public class AdminController {

    private final AdminDashboardService dashboardService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "KPIs agregados: alumnos, carreras activas, inscripciones activas y documentos pendientes",
            description = "Si algún microservicio downstream no responde, el KPI correspondiente viene en null y se explica en 'warnings'.")
    public ResponseEntity<AdminDashboardResponse> dashboard(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(dashboardService.buildDashboard(authorization));
    }

    @GetMapping("/documents/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Lista los documentos pendientes de validación en todo el sistema (proxy a document-service)")
    public ResponseEntity<List<PendingDocumentResponse>> pendingDocuments(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(dashboardService.pendingDocuments(authorization));
    }
}
