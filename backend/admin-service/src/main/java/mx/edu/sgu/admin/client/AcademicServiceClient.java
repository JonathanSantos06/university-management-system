package mx.edu.sgu.admin.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class AcademicServiceClient {

    private final RestClient restClient;

    public AcademicServiceClient(@Qualifier("academicServiceRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    /** Devuelve la lista completa de carreras, o vacío si academic-service no responde. */
    public Optional<List<CareerDto>> listCareers(String authorizationHeader) {
        try {
            List<CareerDto> careers = restClient.get()
                    .uri("/api/careers")
                    .header("Authorization", authorizationHeader)
                    .retrieve()
                    .body(new org.springframework.core.ParameterizedTypeReference<List<CareerDto>>() {
                    });
            return Optional.ofNullable(careers);
        } catch (Exception ex) {
            log.warn("No se pudo consultar academic-service para el listado de carreras: {}", ex.getMessage());
            return Optional.empty();
        }
    }

    /** Cuenta las inscripciones con el estatus indicado (ej. "ACTIVA"), o vacío si no responde. */
    public Optional<Long> countEnrollmentsByStatus(String authorizationHeader, String status) {
        try {
            Long count = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/enrollments/count").queryParam("status", status).build())
                    .header("Authorization", authorizationHeader)
                    .retrieve()
                    .body(Long.class);
            return Optional.ofNullable(count);
        } catch (Exception ex) {
            log.warn("No se pudo consultar academic-service para el conteo de inscripciones: {}", ex.getMessage());
            return Optional.empty();
        }
    }
}
