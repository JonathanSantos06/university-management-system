package mx.edu.sgu.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Si el cliente no envía X-Request-Id, el gateway genera uno y lo agrega tanto a la
 * request reenviada a los microservicios como a la respuesta — útil para correlacionar
 * logs entre auth-service, student-service, academic-service, document-service y admin-service.
 */
@Component
public class CorrelationIdGlobalFilter implements GlobalFilter, Ordered {

    public static final String HEADER = "X-Request-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String existing = exchange.getRequest().getHeaders().getFirst(HEADER);
        String requestId = (existing == null || existing.isBlank()) ? UUID.randomUUID().toString() : existing;

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(HEADER, requestId)
                .build();

        exchange.getResponse().getHeaders().add(HEADER, requestId);

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return -2; // antes que AuthPresenceGlobalFilter, para que el 401 también lleve el header
    }
}
