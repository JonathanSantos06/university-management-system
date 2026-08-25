package mx.edu.sgu.admin.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.admin.client.AcademicServiceClient;
import mx.edu.sgu.admin.client.DocumentServiceClient;
import mx.edu.sgu.admin.client.StudentServiceClient;
import mx.edu.sgu.admin.dto.response.AdminDashboardResponse;
import mx.edu.sgu.admin.dto.response.PendingDocumentResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Agrega información de student-service, academic-service y document-service para
 * armar el dashboard administrativo. admin-service no persiste nada: cada método
 * dispara llamadas HTTP en el momento y degrada de forma controlada si algún
 * servicio downstream no responde (los KPIs afectados salen en null + un warning).
 */
@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final StudentServiceClient studentServiceClient;
    private final AcademicServiceClient academicServiceClient;
    private final DocumentServiceClient documentServiceClient;

    public AdminDashboardResponse buildDashboard(String authorizationHeader) {
        List<String> warnings = new ArrayList<>();

        Long totalStudents = studentServiceClient.countStudents(authorizationHeader)
                .orElseGet(() -> {
                    warnings.add("No se pudo obtener el total de alumnos (student-service no respondió)");
                    return null;
                });

        Long activeCareers = academicServiceClient.listCareers(authorizationHeader)
                .map(careers -> careers.stream().filter(c -> c.active()).count())
                .orElseGet(() -> {
                    warnings.add("No se pudo obtener el catálogo de carreras (academic-service no respondió)");
                    return null;
                });

        Long activeEnrollments = academicServiceClient.countEnrollmentsByStatus(authorizationHeader, "ACTIVA")
                .orElseGet(() -> {
                    warnings.add("No se pudo obtener el conteo de inscripciones activas (academic-service no respondió)");
                    return null;
                });

        Long pendingDocuments = documentServiceClient.listPendingDocuments(authorizationHeader)
                .map(docs -> (long) docs.size())
                .orElseGet(() -> {
                    warnings.add("No se pudo obtener documentos pendientes (document-service no está disponible todavía)");
                    return null;
                });

        return new AdminDashboardResponse(totalStudents, activeCareers, activeEnrollments, pendingDocuments, warnings);
    }

    public List<PendingDocumentResponse> pendingDocuments(String authorizationHeader) {
        return documentServiceClient.listPendingDocuments(authorizationHeader)
                .map(docs -> docs.stream().map(PendingDocumentResponse::from).toList())
                .orElseGet(List::of);
    }
}
