package mx.edu.sgu.admin.client;

import java.util.UUID;

/** Espejo mínimo de CareerResponse de academic-service (solo los campos que admin-service necesita). */
public record CareerDto(UUID id, String name, String code, boolean active) {
}
