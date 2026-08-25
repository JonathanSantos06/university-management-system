package mx.edu.sgu.admin.security;

import java.util.List;
import java.util.UUID;

/** Principal autenticado, poblado a partir de los claims del JWT emitido por auth-service. */
public record CurrentUser(UUID userId, String username, List<String> roles) {
}
