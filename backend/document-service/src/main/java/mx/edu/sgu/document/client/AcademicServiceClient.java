package mx.edu.sgu.document.client;

import lombok.extern.slf4j.Slf4j;
import mx.edu.sgu.document.exception.BusinessRuleException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@Slf4j
public class AcademicServiceClient {

    private final RestClient restClient;

    public AcademicServiceClient(RestClient academicServiceRestClient) {
        this.restClient = academicServiceRestClient;
    }

    public KardexDto fetchKardex(UUID studentId, String authorizationHeader) {
        try {
            return restClient.get()
                    .uri("/api/students/{studentId}/kardex", studentId)
                    .header("Authorization", authorizationHeader)
                    .retrieve()
                    .body(KardexDto.class);
        } catch (Exception ex) {
            log.warn("No se pudo obtener el kardex de academic-service para {}: {}", studentId, ex.getMessage());
            throw new BusinessRuleException("No fue posible obtener el historial académico del alumno en este momento");
        }
    }
}
