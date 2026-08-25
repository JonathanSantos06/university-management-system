package mx.edu.sgu.admin.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

/**
 * Cliente hacia document-service. NOTA: document-service todavía no está construido en esta
 * entrega — este cliente ya define el contrato esperado (GET /api/documents?status=PENDIENTE)
 * y degrada de forma controlada (Optional.empty()) si el servicio no responde, para que
 * admin-service funcione hoy sin depender de que document-service ya exista.
 */
@Component
@Slf4j
public class DocumentServiceClient {

    private final RestClient restClient;

    public DocumentServiceClient(@Qualifier("documentServiceRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public Optional<List<PendingDocumentDto>> listPendingDocuments(String authorizationHeader) {
        try {
            List<PendingDocumentDto> documents = restClient.get()
                    .uri("/api/documents?status=PENDIENTE")
                    .header("Authorization", authorizationHeader)
                    .retrieve()
                    .body(new org.springframework.core.ParameterizedTypeReference<List<PendingDocumentDto>>() {
                    });
            return Optional.ofNullable(documents);
        } catch (Exception ex) {
            log.warn("No se pudo consultar document-service para documentos pendientes (¿aún no está desplegado?): {}", ex.getMessage());
            return Optional.empty();
        }
    }
}
