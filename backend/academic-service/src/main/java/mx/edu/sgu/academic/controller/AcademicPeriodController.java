package mx.edu.sgu.academic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.dto.request.AcademicPeriodRequest;
import mx.edu.sgu.academic.dto.response.AcademicPeriodResponse;
import mx.edu.sgu.academic.service.AcademicPeriodService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/academic-periods")
@RequiredArgsConstructor
@Tag(name = "Periodos académicos", description = "Catálogo de periodos escolares")
public class AcademicPeriodController {

    private final AcademicPeriodService academicPeriodService;

    @GetMapping
    @Operation(summary = "Lista todos los periodos académicos")
    public ResponseEntity<List<AcademicPeriodResponse>> findAll() {
        return ResponseEntity.ok(academicPeriodService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un periodo académico por id")
    public ResponseEntity<AcademicPeriodResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(academicPeriodService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea un periodo académico")
    public ResponseEntity<AcademicPeriodResponse> create(@Valid @RequestBody AcademicPeriodRequest request) {
        return ResponseEntity.status(201).body(academicPeriodService.create(request));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activa el periodo indicado y desactiva cualquier otro previamente activo")
    public ResponseEntity<AcademicPeriodResponse> activate(@PathVariable UUID id) {
        return ResponseEntity.ok(academicPeriodService.activate(id));
    }
}
