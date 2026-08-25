package mx.edu.sgu.academic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.dto.request.AddPrerequisiteRequest;
import mx.edu.sgu.academic.dto.request.SubjectRequest;
import mx.edu.sgu.academic.dto.response.CurriculumSubjectResponse;
import mx.edu.sgu.academic.dto.response.SubjectResponse;
import mx.edu.sgu.academic.service.CurriculumService;
import mx.edu.sgu.academic.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@Tag(name = "Materias", description = "Catálogo de materias y prerrequisitos")
public class SubjectController {

    private final SubjectService subjectService;
    private final CurriculumService curriculumService;

    @GetMapping
    @Operation(summary = "Lista todas las materias")
    public ResponseEntity<List<SubjectResponse>> findAll() {
        return ResponseEntity.ok(subjectService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una materia por id")
    public ResponseEntity<SubjectResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(subjectService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea una materia")
    public ResponseEntity<SubjectResponse> create(@Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.status(201).body(subjectService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualiza una materia")
    public ResponseEntity<SubjectResponse> update(@PathVariable UUID id, @Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(subjectService.update(id, request));
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activa o desactiva una materia")
    public ResponseEntity<SubjectResponse> setActive(@PathVariable UUID id, @RequestParam boolean active) {
        return ResponseEntity.ok(subjectService.setActive(id, active));
    }

    @GetMapping("/{id}/prerequisites")
    @Operation(summary = "Lista los prerrequisitos de una materia")
    public ResponseEntity<List<CurriculumSubjectResponse.PrerequisiteResponse>> prerequisites(@PathVariable UUID id) {
        return ResponseEntity.ok(curriculumService.prerequisitesOf(id));
    }

    @PostMapping("/{id}/prerequisites")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agrega un prerrequisito a una materia")
    public ResponseEntity<Void> addPrerequisite(@PathVariable UUID id, @Valid @RequestBody AddPrerequisiteRequest request) {
        curriculumService.addPrerequisite(id, request);
        return ResponseEntity.status(201).build();
    }
}
