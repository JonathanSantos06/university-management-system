package mx.edu.sgu.academic.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import mx.edu.sgu.academic.config.JwtProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * academic-service NO emite tokens (eso es responsabilidad exclusiva de auth-service).
 * Solo valida la firma de los access tokens (JWT) usando la misma clave HMAC compartida.
 */
@Component
public class JwtValidator {

    private final SecretKey signingKey;

    public JwtValidator(JwtProperties properties) {
        byte[] keyBytes = Base64.getDecoder().decode(properties.secret());
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims parseAndValidate(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
