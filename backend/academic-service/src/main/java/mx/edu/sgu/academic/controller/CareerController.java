package mx.edu.sgu.academic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.dto.request.CareerRequest;
import mx.edu.sgu.academic.dto.response.CareerResponse;
import mx.edu.sgu.academic.service.CareerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/careers")
@RequiredArgsConstructor
@Tag(name = "Carreras", description = "Catálogo de carreras")
public class CareerController {

    private final CareerService careerService;

    @GetMapping
    @Operation(summary = "Lista todas las carreras (cualquier rol autenticado)")
    public ResponseEntity<List<CareerResponse>> findAll() {
        return ResponseEntity.ok(careerService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una carrera por id")
    public ResponseEntity<CareerResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(careerService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crea una carrera")
    public ResponseEntity<CareerResponse> create(@Valid @RequestBody CareerRequest request) {
        return ResponseEntity.status(201).body(careerService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualiza una carrera")
    public ResponseEntity<CareerResponse> update(@PathVariable UUID id, @Valid @RequestBody CareerRequest request) {
        return ResponseEntity.ok(careerService.update(id, request));
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activa o desactiva una carrera")
    public ResponseEntity<CareerResponse> setActive(@PathVariable UUID id, @RequestParam boolean active) {
        return ResponseEntity.ok(careerService.setActive(id, active));
    }
}
