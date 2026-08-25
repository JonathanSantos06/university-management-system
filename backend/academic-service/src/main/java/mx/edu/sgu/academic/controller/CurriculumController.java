package mx.edu.sgu.academic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.dto.request.AddCurriculumSubjectRequest;
import mx.edu.sgu.academic.dto.response.CurriculumSubjectResponse;
import mx.edu.sgu.academic.service.CurriculumService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/careers/{careerId}/curriculum")
@RequiredArgsConstructor
@Tag(name = "Retículas", description = "Retícula (mapa curricular) de una carrera, por semestre")
public class CurriculumController {

    private final CurriculumService curriculumService;

    @GetMapping
    @Operation(summary = "Obtiene la retícula completa de una carrera, agrupable por semestre")
    public ResponseEntity<List<CurriculumSubjectResponse>> getCurriculum(@PathVariable UUID careerId) {
        return ResponseEntity.ok(curriculumService.getCurriculum(careerId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agrega una materia a la retícula de la carrera en un semestre específico")
    public ResponseEntity<CurriculumSubjectResponse> addSubject(@PathVariable UUID careerId,
                                                                 @Valid @RequestBody AddCurriculumSubjectRequest request) {
        return ResponseEntity.status(201).body(curriculumService.addSubjectToCurriculum(careerId, request));
    }

    @DeleteMapping("/{subjectId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Quita una materia de la retícula de la carrera")
    public ResponseEntity<Void> removeSubject(@PathVariable UUID careerId, @PathVariable UUID subjectId) {
        curriculumService.removeSubjectFromCurriculum(careerId, subjectId);
        return ResponseEntity.noContent().build();
    }
}
