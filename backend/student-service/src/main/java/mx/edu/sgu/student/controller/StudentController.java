package mx.edu.sgu.student.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.sgu.student.domain.StudentStatus;
import mx.edu.sgu.student.dto.request.*;
import mx.edu.sgu.student.dto.response.StudentResponse;
import mx.edu.sgu.student.dto.response.StudentSummaryResponse;
import mx.edu.sgu.student.security.CurrentUser;
import mx.edu.sgu.student.service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Alumnos", description = "Expediente académico del alumno: datos personales, domicilios y contactos de emergencia")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Busca/lista alumnos (paginado) por nombre, matrícula, carrera o estatus")
    public ResponseEntity<Page<StudentSummaryResponse>> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) UUID careerId,
            @RequestParam(required = false) StudentStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(studentService.search(query, careerId, status, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Obtiene el expediente completo de un alumno por id")
    public ResponseEntity<StudentResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ALUMNO')")
    @Operation(summary = "Obtiene el expediente propio del alumno autenticado")
    public ResponseEntity<StudentResponse> me(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(studentService.findByUserId(currentUser.userId()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Crea el expediente de un alumno (alta) con datos personales")
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody CreateStudentRequest request) {
        return ResponseEntity.status(201).body(studentService.create(request));
    }

    @PutMapping("/{id}/personal-data")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Actualiza los datos personales del alumno")
    public ResponseEntity<StudentResponse> updatePersonalData(@PathVariable UUID id, @Valid @RequestBody PersonalDataRequest request) {
        return ResponseEntity.ok(studentService.updatePersonalData(id, request));
    }

    @PostMapping("/{id}/addresses")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Agrega un domicilio (actual o permanente) al alumno")
    public ResponseEntity<StudentResponse> addAddress(@PathVariable UUID id, @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(studentService.addAddress(id, request));
    }

    @PostMapping("/{id}/emergency-contacts")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Agrega un contacto de emergencia al alumno")
    public ResponseEntity<StudentResponse> addEmergencyContact(@PathVariable UUID id, @Valid @RequestBody EmergencyContactRequest request) {
        return ResponseEntity.ok(studentService.addEmergencyContact(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Cambia el estatus del alumno (ACTIVO, BAJA_TEMPORAL, BAJA_DEFINITIVA, EGRESADO, TITULADO)")
    public ResponseEntity<StudentResponse> updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(studentService.updateStatus(id, request.status()));
    }

    @PatchMapping("/{id}/semester")
    @PreAuthorize("hasAnyRole('ADMIN', 'PERSONAL_ADMINISTRATIVO')")
    @Operation(summary = "Actualiza el semestre actual del alumno (lo normal es que lo dispare academic-service al cerrar un periodo)")
    public ResponseEntity<StudentResponse> updateSemester(@PathVariable UUID id, @Valid @RequestBody UpdateSemesterRequest request) {
        return ResponseEntity.ok(studentService.updateSemester(id, request.currentSemester()));
    }
}
