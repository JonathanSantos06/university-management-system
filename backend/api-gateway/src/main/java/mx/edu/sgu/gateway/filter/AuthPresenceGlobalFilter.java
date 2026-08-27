package mx.edu.sgu.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Rechaza tempranamente (401, sin llegar al microservicio) cualquier request a una ruta
 * protegida que no traiga "Authorization: Bearer ...". No valida la firma/expiración del
 * JWT — esa validación criptográfica es responsabilidad exclusiva de cada microservicio
 * (todos comparten la misma clave HMAC de auth-service). Este filtro es una capa barata de
 * defensa en profundidad para no reenviar tráfico obviamente no autenticado.
 */
@Component
public class AuthPresenceGlobalFilter implements GlobalFilter, Ordered {

    private static final String BEARER_PREFIX = "Bearer ";

    /** Rutas que no requieren Authorization. Todo lo demás bajo /api/** sí lo requiere. */
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/auth/logout"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::equals) || !path.startsWith("/api/");
        if (isPublic) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
            byte[] body = ("{\"status\":401,\"error\":\"No autenticado\","
                    + "\"message\":\"Se requiere el encabezado Authorization: Bearer <token>\"}").getBytes();
            var buffer = exchange.getResponse().bufferFactory().wrap(body);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // se ejecuta antes que el enrutamiento
    }
}
