package mx.edu.sgu.academic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.domain.Enrollment;
import mx.edu.sgu.academic.domain.EnrollmentSubject;
import mx.edu.sgu.academic.dto.request.CreateEnrollmentRequest;
import mx.edu.sgu.academic.dto.response.EnrollmentResponse;
import mx.edu.sgu.academic.dto.response.KardexResponse;
import mx.edu.sgu.academic.service.EnrollmentService;
import mx.edu.sgu.academic.service.KardexService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Inscripciones", description = "Inscripción de alumnos a materias por periodo, y kardex")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final KardexService kardexService;

    @PostMapping("/api/enrollments")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Crea una inscripción de un alumno a un periodo con una o más materias, validando prerrequisitos")
    public ResponseEntity<EnrollmentResponse> create(@Valid @RequestBody CreateEnrollmentRequest request) {
        return ResponseEntity.status(201).body(enrollmentService.create(request));
    }

    @GetMapping("/api/enrollments/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO', 'ALUMNO')")
    @Operation(summary = "Obtiene una inscripción por id, con sus materias y calificaciones")
    public ResponseEntity<EnrollmentResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(enrollmentService.findById(id));
    }

    /**
     * NOTA: este servicio no valida que {studentId} pertenezca al alumno autenticado
     * (el JWT solo trae el userId de auth-service, no el student.id). En esta entrega
     * se confía en que el frontend pasa el id correcto (obtenido de student-service /me).
     * Para un endurecimiento real, academic-service debería resolver studentId -> userId
     * llamando a student-service, o cachear esa relación vía eventos.
     */
    @GetMapping("/api/students/{studentId}/enrollments")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO', 'ALUMNO')")
    @Operation(summary = "Lista el historial de inscripciones de un alumno")
    public ResponseEntity<List<EnrollmentResponse>> findByStudent(@PathVariable UUID studentId) {
        return ResponseEntity.ok(enrollmentService.findByStudent(studentId));
    }

    @GetMapping("/api/students/{studentId}/kardex")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO', 'ALUMNO')")
    @Operation(summary = "Kardex académico consolidado del alumno (créditos, materias e historial)")
    public ResponseEntity<KardexResponse> kardex(@PathVariable UUID studentId) {
        return ResponseEntity.ok(kardexService.build(studentId));
    }

    @GetMapping("/api/enrollments/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Cuenta inscripciones por estatus (usado por admin-service para dashboards)")
    public ResponseEntity<Long> countByStatus(@RequestParam Enrollment.EnrollmentStatus status) {
        return ResponseEntity.ok(enrollmentService.countByStatus(status));
    }

    @PatchMapping("/api/enrollments/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Cambia el estatus de la inscripción (ACTIVA, CERRADA, CANCELADA)")
    public ResponseEntity<EnrollmentResponse> updateStatus(@PathVariable UUID id, @RequestParam Enrollment.EnrollmentStatus status) {
        return ResponseEntity.ok(enrollmentService.updateStatus(id, status));
    }

    @PatchMapping("/api/enrollments/{id}/subjects/{enrollmentSubjectId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Cambia el estatus de una materia inscrita (ej. dar de BAJA)")
    public ResponseEntity<EnrollmentResponse> updateSubjectStatus(@PathVariable UUID id, @PathVariable UUID enrollmentSubjectId,
                                                                   @RequestParam EnrollmentSubject.Status status) {
        return ResponseEntity.ok(enrollmentService.updateSubjectStatus(id, enrollmentSubjectId, status));
    }
}
