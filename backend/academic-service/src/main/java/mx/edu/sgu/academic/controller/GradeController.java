package mx.edu.sgu.academic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.dto.request.RecordGradeRequest;
import mx.edu.sgu.academic.dto.response.GradeResponse;
import mx.edu.sgu.academic.security.CurrentUser;
import mx.edu.sgu.academic.service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/enrollment-subjects/{enrollmentSubjectId}/grades")
@RequiredArgsConstructor
@Tag(name = "Calificaciones", description = "Captura y consulta de calificaciones por parcial")
public class GradeController {

    private final GradeService gradeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO', 'ALUMNO')")
    @Operation(summary = "Lista las calificaciones (por parcial) de una materia inscrita")
    public ResponseEntity<List<GradeResponse>> findAll(@PathVariable UUID enrollmentSubjectId) {
        return ResponseEntity.ok(gradeService.findByEnrollmentSubject(enrollmentSubjectId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Registra la calificación de un parcial (1-4). El parcial final actualiza el estatus de la materia")
    public ResponseEntity<GradeResponse> record(@PathVariable UUID enrollmentSubjectId,
                                                 @Valid @RequestBody RecordGradeRequest request,
                                                 @AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.status(201).body(gradeService.recordGrade(enrollmentSubjectId, request, currentUser));
    }
}
