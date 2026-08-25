package mx.edu.sgu.admin.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@Slf4j
public class StudentServiceClient {

    private final RestClient restClient;

    public StudentServiceClient(@Qualifier("studentServiceRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    /** Devuelve el total de alumnos registrados, o vacío si student-service no responde. */
    public Optional<Long> countStudents(String authorizationHeader) {
        try {
            StudentPageResponse page = restClient.get()
                    .uri("/api/students?page=0&size=1")
                    .header("Authorization", authorizationHeader)
                    .retrieve()
                    .body(StudentPageResponse.class);
            return page == null ? Optional.empty() : Optional.of(page.totalElements());
        } catch (Exception ex) {
            log.warn("No se pudo consultar student-service para el conteo de alumnos: {}", ex.getMessage());
            return Optional.empty();
        }
    }
}
